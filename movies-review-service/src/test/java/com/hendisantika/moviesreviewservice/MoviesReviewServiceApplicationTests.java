package com.hendisantika.moviesreviewservice;

import com.hendisantika.moviesreviewservice.domain.Review;
import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class MoviesReviewServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ReviewReactiveRepository repository;

    @BeforeEach
    void setup() {
        var reviews = List.of(
                new Review(null, 1L, "Awesome movie", 9.0),
                new Review(null, 1L, "Great movie", 8.0),
                new Review("specific-id", 2L, "Best movie", 9.0)
        );
        repository.saveAll(reviews).blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void addReview() {
        var review = new Review(null, 1L, "Good movie", 7.5);
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
        client
                .get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(3);
    }
}
