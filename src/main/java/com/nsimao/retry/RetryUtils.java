package com.nsimao.retry;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.function.Supplier;

@Log4j2
public class RetryUtils<T> {
    public T retry(Supplier<T> supplier, String logInfo, int retryCount, List<Class<? extends Throwable>> retryExceptions,
                   Class<? extends RuntimeException> exceptionWhenFailed) {
        Exception lastException = null;
        int maxAttempts = retryCount;
        while (maxAttempts > 0) {
            try {
                if (maxAttempts < retryCount)
                    log.info("Retry execute {}, attempts left: {}", logInfo, maxAttempts);
                return supplier.get();
            } catch (Exception ex) {
                lastException = ex;
                log.error("Execution Failed", ex);
                if (retryExceptions.stream().filter(i -> i.isAssignableFrom(ex.getClass())).findFirst().isPresent()) {
                    sleep();
                    maxAttempts -= 1;
                } else
                    break;
            }
        }
        log.error("Retry failed after {} attempts, throwing Exception. Last exception captured", retryCount, lastException);
        RuntimeException runtimeException = null;
        try {
            runtimeException = exceptionWhenFailed.getDeclaredConstructor().newInstance();
        } catch (Throwable t) {
            log.error("Failed to instantiate exceptionWhenFailed object", t);
        }
        if (runtimeException != null)
            throw runtimeException;
        else
            throw new RuntimeException("Retry failed", lastException);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            log.error("Error when sleeping", ex);
            Thread.currentThread().interrupt();
        }
    }
}
