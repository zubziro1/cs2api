package zub.cs2api.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zub.cs2api.model.FutureWithStartTime;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableScheduling
@SuppressWarnings("unused")
public class CacheFuture {
    private static final Logger log = LoggerFactory.getLogger(CacheFuture.class);

    private final Map<String, FutureWithStartTime> callbacks = new ConcurrentHashMap<>();

    public void put(String id, CompletableFuture<String> future) {
        callbacks.put(id, new FutureWithStartTime(LocalDateTime.now(), future));
    }

    public CompletableFuture<String> pop(String id) {
        var future = callbacks.get(id).getFuture();
        callbacks.remove(id);
        return future;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanup() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        log.debug("Running cleanup at: {}", LocalDateTime.now());

        callbacks.entrySet().removeIf(entry -> {
            LocalDateTime startTime = entry.getValue().getStartTime();
            boolean isExpired = startTime.isBefore(oneMinuteAgo);

            if (isExpired) {
                log.debug("Removing expired future: id={}, startTime={}", entry.getKey(), startTime);
                entry.getValue().getFuture().cancel(true); // Cancel the future if necessary
            }

            return isExpired;
        });

        log.debug("Cleanup complete. Remaining futures count: {}", callbacks.size());
    }
}
