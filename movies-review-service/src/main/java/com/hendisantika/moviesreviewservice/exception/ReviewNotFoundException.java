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
public class ReviewNotFoundException extends RuntimeException {
    private final String message;
    private Throwable ex;

    public ReviewNotFoundException(String message, Throwable ex) {
        super(message, ex);
        this.message = message;
        this.ex = ex;
    }

    public ReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
