package com.hendisantika.moviesreviewservice.handler;

import com.hendisantika.moviesreviewservice.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:44
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewReactiveRepository repository;
    private final Validator validator;
    private final Sinks.Many<Review> reviewSink = Sinks.many().replay().latest();
}
