package com.hendisantika.moviesservice.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:05
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
public class MoviesInfoClientException extends RuntimeException {
    private String message;
    private int status;

    public MoviesInfoClientException(String message, int status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
