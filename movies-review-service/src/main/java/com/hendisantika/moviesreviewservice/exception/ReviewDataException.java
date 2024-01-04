package com.hendisantika.moviesreviewservice.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:43
 * To change this template use File | Settings | File Templates.
 */
public class ReviewDataException extends RuntimeException {
    private final String message;

    public ReviewDataException(String message) {
        super(message);
        this.message = message;
    }
}
