package com.hendisantika.moviesinfoservice.controller;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.service.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:19
 * To change this template use File | Settings | File Templates.
 */
@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private MoviesInfoService moviesInfoServiceMock;

    @Test
    void getAllMoviesInfo() {
        var movieInfos = List.of(
                new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale"), LocalDate.parse("2008-07-18")),
                new MovieInfo("specific-id", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"))
        );
        when(moviesInfoServiceMock.getAllMovieInfos())
                .thenReturn(Flux.fromIterable(movieInfos));

        client.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        var movieInfo = new MovieInfo("specific-id", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.getMovieInfoById("specific-id"))
                .thenReturn(Mono.just(movieInfo));

        client
                .get()
                .uri("/v1/movieinfos/{id}", "specific-id")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var response = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(response.getId(), equalTo("specific-id"));
                    assertThat(response.getName(), equalTo("Dark Knight Rises"));
                });
    }
}
