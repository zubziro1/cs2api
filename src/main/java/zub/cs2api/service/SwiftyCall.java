package zub.cs2api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import zub.cs2api.component.CacheFuture;
import zub.cs2api.config.SettingsProperties;
import zub.cs2api.dto.SwapiRequest;
import zub.cs2api.dto.SwapiResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class SwiftyCall {

    private final RconCall rconCall;
    private final CacheFuture cacheFuture;
    private final SettingsProperties settingsProperties;
    private final ObjectMapper om = new ObjectMapper();

    public SwiftyCall(RconCall rconCall, CacheFuture cacheFuture, SettingsProperties settingsProperties) {
        this.rconCall = rconCall;
        this.cacheFuture = cacheFuture;
        this.settingsProperties = settingsProperties;
    }

    public SwapiResponse call(String auth, SwapiRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        var requestId = UUID.randomUUID().toString();

        var future = new CompletableFuture<SwapiResponse>();
        cacheFuture.put(requestId, future);
        rconCall.send("sw_api" + " " + requestId + " " + auth + " " + Base64.getEncoder().encodeToString(settingsProperties.getBaseUrlCallback().getBytes(StandardCharsets.UTF_8)) + " " + Base64.getEncoder().encodeToString(om.writeValueAsBytes(request)));

        return future.get(settingsProperties.getWaitForCallbackTimeOut(), TimeUnit.MILLISECONDS);
    }
}
