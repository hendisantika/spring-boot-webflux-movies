package com.hendisantika.moviesservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.hendisantika.moviesservice.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:15
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
                "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
        })
public class MoviesControllerTest {

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setup() {
        WireMock.reset();
    }

    @Test
    void retrieveMovieById() {
        stubFor(get(urlEqualTo("/v1/movieinfos/123"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("movieInfo.json")));

        stubFor(get(urlEqualTo("/v1/reviews?movieInfoId=123"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("reviews.json")));


        client
                .get()
                .uri("/v1/movies/{id}", "123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Movie.class)
                .consumeWith(movieEntityExchangeResult -> {
                    var movie = movieEntityExchangeResult.getResponseBody();
                    assertThat(movie.getMovieInfo().getName(), equalTo("Batman Begins"));
                    assertThat(movie.getReviews().size(), equalTo(2));
                });
    }

    @Test
    void retrieveMovieById_movieInfo_404() {
        stubFor(get(urlEqualTo("/v1/movieinfos/123"))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        stubFor(get(urlEqualTo("/v1/reviews?movieInfoId=123"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("reviews.json")));


        client
                .get()
                .uri("/v1/movies/{id}", "123")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("No movie info for id 123");

        WireMock.verify(1, getRequestedFor(urlEqualTo("/v1/movieinfos/123")));
    }
}
