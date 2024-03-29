package com.hendisantika.moviesreviewservice.router;

import com.hendisantika.moviesreviewservice.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:47
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler) {
        return route()
                .nest(path("/v1/reviews"), builder -> {
                    builder
                            .POST("", reviewHandler::addReview)
                            .GET("", reviewHandler::getReviews)
                            .PUT("/{id}", reviewHandler::updateReview)
                            .DELETE("/{id}", reviewHandler::deleteReview)
                            .GET("/stream", reviewHandler::getReviewsStream)
                    ;
                })

                .build();
    }
}
