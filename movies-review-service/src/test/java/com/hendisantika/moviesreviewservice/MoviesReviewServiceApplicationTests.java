package com.hendisantika.moviesreviewservice;

import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class MoviesReviewServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ReviewReactiveRepository repository;

    @Test
    void contextLoads() {
    }

}
