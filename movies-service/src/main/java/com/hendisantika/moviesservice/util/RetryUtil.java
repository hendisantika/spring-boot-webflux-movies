package com.hendisantika.moviesservice.util;

import reactor.core.Exceptions;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:08
 * To change this template use File | Settings | File Templates.
 */
public class RetryUtil {
    public static Retry retrySpec(Class<?> clazz) {
        return Retry.fixedDelay(3, Duration.ofMillis(500))
                .filter(ex -> clazz.isAssignableFrom(ex.getClass()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    return Exceptions.propagate(retrySignal.failure());
                });
    }
}
