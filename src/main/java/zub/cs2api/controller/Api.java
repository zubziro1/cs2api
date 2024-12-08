package zub.cs2api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import zub.cs2api.service.SwiftyCall;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class Api {
    private final Logger log = LoggerFactory.getLogger(Api.class);

    private final SwiftyCall swiftyCall;

    public Api(SwiftyCall swiftyCall) {
        this.swiftyCall = swiftyCall;
    }

    @PostMapping("/sw")
    public String sw(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestBody String body) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        return swiftyCall.call(authorizationHeader, body);
    }

}
