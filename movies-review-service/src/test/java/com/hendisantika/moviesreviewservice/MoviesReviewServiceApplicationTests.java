package com.hendisantika.moviesreviewservice;

import com.hendisantika.moviesreviewservice.domain.Review;
import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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
}
