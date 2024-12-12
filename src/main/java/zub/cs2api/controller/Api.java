package zub.cs2api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import zub.cs2api.dto.SwapiRequest;
import zub.cs2api.dto.SwapiRequestMap;
import zub.cs2api.dto.SwapiResponse;
import zub.cs2api.service.SwiftyCall;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class Api {
    private final Logger log = LoggerFactory.getLogger(Api.class);

    private final SwiftyCall swiftyCall;
    private final ObjectMapper om = new ObjectMapper();

    public Api(SwiftyCall swiftyCall) {
        this.swiftyCall = swiftyCall;
    }

    @PostMapping("/sw")
    public SwapiResponse sw(@RequestHeader(value = "Authorization") String auth, @RequestBody SwapiRequest req) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return swiftyCall.call(auth, req);
    }

    @GetMapping("/sw/players")
    public String swGetPlayers(@RequestHeader(value = "Authorization") String auth) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        var req = new SwapiRequest("getPlayers", null);
        var resp = swiftyCall.call(auth, req);
        return new String(Base64.getDecoder().decode(resp.getPayload()));
    }

    @PostMapping("/sw/map")
    public String swSetMap(@RequestHeader(value = "Authorization") String auth, @RequestBody SwapiRequestMap map) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        var req = new SwapiRequest("setMap", om.writeValueAsString(map));
        var resp = swiftyCall.call(auth, req);
        return new String(Base64.getDecoder().decode(resp.getPayload()));
    }

}
