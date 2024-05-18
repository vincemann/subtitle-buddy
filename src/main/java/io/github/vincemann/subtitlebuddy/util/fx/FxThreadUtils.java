package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FxThreadUtils {

    public static void runOnFxThreadAndWait(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                task.run();
                future.complete(null);  // Signal completion
            } catch (Exception e) {
                future.completeExceptionally(e);  // Propagate error
            }
        });

        try {
            future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());  // Throw the cause of the exception
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle thread interruption
            throw new RuntimeException("Interrupted while waiting", e);
        }
    }
}
