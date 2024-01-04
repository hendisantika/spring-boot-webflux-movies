package com.hendisantika.moviesservice.client;

import com.hendisantika.moviesservice.domain.MovieInfo;
import com.hendisantika.moviesservice.exception.MoviesInfoClientException;
import com.hendisantika.moviesservice.exception.MoviesInfoServerException;
import com.hendisantika.moviesservice.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
@Component
@RequiredArgsConstructor
public class MoviesInfoRestClient {

    private final WebClient client;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var uri = moviesInfoUrl.concat("/{id}");
        return client
                .get()
                .uri(uri, movieId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    HttpStatus httpStatus = clientResponse.statusCode();
                    if (httpStatus.equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MoviesInfoClientException("No movie info for id " + movieId, httpStatus.value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(message -> Mono.error(new MoviesInfoClientException(message, httpStatus.value())));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(message -> Mono.error(new MoviesInfoServerException(message)));
                })
                .bodyToMono(MovieInfo.class)
                .retryWhen(RetryUtil.retrySpec(MoviesInfoServerException.class))
                .log();
    }

    public Flux<MovieInfo> retrieveMovieInfoStream() {
        var uri = moviesInfoUrl.concat("/stream");
        return client
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(MovieInfo.class);
    }
}
