package com.hendisantika.moviesservice.client;

import com.hendisantika.moviesservice.domain.Review;
import com.hendisantika.moviesservice.exception.ReviewsClientException;
import com.hendisantika.moviesservice.exception.ReviewsServerException;
import com.hendisantika.moviesservice.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:10
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class ReviewRestClient {

    private final WebClient client;

    @Value("${restClient.reviewsUrl}")
    private String reviewUrl;

    public Flux<Review> retrieveReviews(String movieId) {
        var uri = UriComponentsBuilder.fromHttpUrl(reviewUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand()
                .toUriString();

        return client
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.empty();
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(message -> Mono.error(new ReviewsClientException(message)));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(message -> Mono.error(new ReviewsServerException(message)));
                })
                .bodyToFlux(Review.class)
                .retryWhen(RetryUtil.retrySpec(ReviewsServerException.class))
                .log();
    }
}
