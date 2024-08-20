package ntq.server.common.util;

import ntq.server.common.exception.NSTCompletionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiFunction;

public class CompletableFutures {
  private static final CompletableFuture<Object> EMPTY_COMPLETED = CompletableFuture.completedFuture(null);

  @SuppressWarnings("unchecked")
  public static <T> CompletableFuture<T> completedFuture() {
    return (CompletableFuture<T>) EMPTY_COMPLETED;
  }

  @SuppressWarnings("unchecked")
  public static <T, U> CompletableFuture<U> handleCompose(CompletableFuture<T> future, BiFunction<? super T, Throwable, CompletableFuture<? extends U>> fn) {
    return (CompletableFuture<U>) future.handle(fn)
        .thenCompose(v -> v);
  }

  public static CompletionException toCompletionException(Throwable t) {
    if (t instanceof CompletionException ce) {
      return ce;
    }
    return new NSTCompletionException(t);
  }

  private CompletableFutures() {
    throw new UnsupportedOperationException();
  }
}
