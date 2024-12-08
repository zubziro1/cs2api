package zub.cs2api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zub.cs2api.component.CacheFuture;

@RestController
@RequestMapping("/callback")
@SuppressWarnings("unused")
public class Callback {

    private final Logger log = LoggerFactory.getLogger(Callback.class);
    private final CacheFuture cacheFuture;

    public Callback(CacheFuture cacheFuture) {
        this.cacheFuture = cacheFuture;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> callback(@PathVariable String id, @RequestBody String body) {

        log.debug("handleCallback: {}", id);
        log.trace("body: {}", body);

        var future = cacheFuture.pop(id);
        if (future != null) {
            future.complete(body);
        }

        return ResponseEntity.ok().build();
    }
}
