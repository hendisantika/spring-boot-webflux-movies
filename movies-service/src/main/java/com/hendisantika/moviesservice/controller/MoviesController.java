package com.hendisantika.moviesservice.controller;

import com.hendisantika.moviesservice.client.MoviesInfoRestClient;
import com.hendisantika.moviesservice.client.ReviewRestClient;
import com.hendisantika.moviesservice.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:12
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final MoviesInfoRestClient moviesInfoRestClient;
    private final ReviewRestClient reviewRestClient;

    @GetMapping("/{movieId}")
    public Mono<Movie> retrieveMovieById(@PathVariable String movieId) {
        return moviesInfoRestClient.retrieveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    var reviewsMono = reviewRestClient.retrieveReviews(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
