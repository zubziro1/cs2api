package zub.cs2api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FutureWithStartTime {
    private LocalDateTime startTime;
    private CompletableFuture<String> future;
}
