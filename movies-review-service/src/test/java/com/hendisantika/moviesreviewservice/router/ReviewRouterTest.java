package com.hendisantika.moviesreviewservice.router;

import com.hendisantika.moviesreviewservice.domain.Review;
import com.hendisantika.moviesreviewservice.exception.GlobalExceptionHandler;
import com.hendisantika.moviesreviewservice.handler.ReviewHandler;
import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:50
 * To change this template use File | Settings | File Templates.
 */
@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalExceptionHandler.class})
class ReviewRouterTest {

    @MockBean
    private ReviewReactiveRepository repository;

    @Autowired
    private WebTestClient client;

    @Test
    void addReview() {
        var review = new Review(null, 1L, "Good movie", 7.5);

        when(repository.save(review))
                .thenReturn(Mono.just(new Review("id", 1L, "Good movie", 7.5)));
        client
                .post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var response = reviewEntityExchangeResult.getResponseBody();
                    assertThat(response, is(not(nullValue())));
                });
    }

    @Test
    void getReviews() {
        when(repository.findAll())
                .thenReturn(Flux.just(new Review("id", 1L, "Good movie", 7.5)));
        client
                .get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(1);
    }

}
