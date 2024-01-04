package com.hendisantika.moviesservice.exception;

import lombok.Getter;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:06
 * To change this template use File | Settings | File Templates.
 */
@Getter
public class ReviewsClientException extends RuntimeException {
    private final String message;

    public ReviewsClientException(String message) {
        super(message);
        this.message = message;
    }
}
