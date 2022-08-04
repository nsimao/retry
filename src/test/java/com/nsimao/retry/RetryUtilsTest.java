package com.nsimao.retry;

import com.nsimao.retry.exception.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class RetryUtilsTest {
    @Test
    void testError() {
        RetryUtils<Integer> booleanRetryUtils = new RetryUtils<>();
        Assertions.assertThrows(ArithmeticException.class, () -> booleanRetryUtils.retry(() -> {
            int a = 0, b = 10;
            return b / a;
        }, "testError", 5, Collections.singletonList(ArithmeticException.class), ArithmeticException.class));
    }

    @Test
    void testErrorFailToInstantiateExceptionWhenFailedClass() {
        RetryUtils<Integer> booleanRetryUtils = new RetryUtils<>();
        Assertions.assertThrows(RuntimeException.class, () -> booleanRetryUtils.retry(() -> {
            int a = 0, b = 10;
            return b / a;
        }, "testErrorFailToInstantiateExceptionWhenFailedClass", 5, Collections.singletonList(ArithmeticException.class), CustomException.class));
    }

    @Test
    void testErrorRetryDoesNotExecuteBecauseExceptionNotMappedWasThrown() {
        RetryUtils<Integer> booleanRetryUtils = new RetryUtils<>();
        Assertions.assertThrows(ArithmeticException.class, () -> booleanRetryUtils.retry(() -> {
            int a = 0, b = 10;
            return b / a;
        }, "testErrorRetryDoesNotExecuteBecauseExceptionNotMappedWasThrown", 5, Collections.singletonList(ArrayIndexOutOfBoundsException.class), ArithmeticException.class));
    }

    @Test
    void testSuccessMethod() {
        RetryUtils<Integer> booleanRetryUtils = new RetryUtils<>();
        Assertions.assertEquals(5, booleanRetryUtils.retry(() -> {
            int a = 2, b = 10;
            return b / a;
        }, "testSuccessMethod", 5, Collections.singletonList(ArithmeticException.class), ArithmeticException.class));
    }
}