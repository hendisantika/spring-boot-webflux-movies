package com.hendisantika.moviesservice.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:07
 * To change this template use File | Settings | File Templates.
 */
public class ReviewsServerException extends RuntimeException {
    private final String message;

    public ReviewsServerException(String message) {
        super(message);
        this.message = message;
    }
}
