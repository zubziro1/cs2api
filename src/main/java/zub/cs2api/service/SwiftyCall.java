package zub.cs2api.service;

import org.springframework.stereotype.Service;
import zub.cs2api.component.CacheFuture;
import zub.cs2api.config.SettingsProperties;

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

    public SwiftyCall(RconCall rconCall, CacheFuture cacheFuture, SettingsProperties settingsProperties) {
        this.rconCall = rconCall;
        this.cacheFuture = cacheFuture;
        this.settingsProperties = settingsProperties;
    }

    public String call(String auth, String payload) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        var requestId = UUID.randomUUID().toString();

        var future = new CompletableFuture<String>();
        cacheFuture.put(requestId, future);
        rconCall.send("sw_api" + " " + requestId + " " + auth + " " + Base64.getEncoder().encodeToString(settingsProperties.getBaseUrlCallback().getBytes(StandardCharsets.UTF_8)) + " " + Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8)));

        return future.get(settingsProperties.getWaitForCallbackTimeOut(), TimeUnit.MILLISECONDS);
    }
}
