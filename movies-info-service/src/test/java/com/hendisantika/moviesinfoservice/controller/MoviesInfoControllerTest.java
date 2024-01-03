package com.hendisantika.moviesinfoservice.controller;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.service.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null, "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        var savedMovieInfo = new MovieInfo("specific-id", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.addMovieInfo(ArgumentMatchers.isA(MovieInfo.class)))
                .thenReturn(Mono.just(savedMovieInfo));

        client
                .post()
                .uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var response = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(response.getId(), is(not(nullValue())));
                });
    }

    @Test
    void updateMovieInfo() {
        var movieInfo = new MovieInfo(null, "New Title", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        var updatedMovieInfo = new MovieInfo("specific-id", "New Title", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.updateMovieInfo(ArgumentMatchers.isA(MovieInfo.class), ArgumentMatchers.isA(String.class)))
                .thenReturn(Mono.just(updatedMovieInfo));

        client
                .put()
                .uri("/v1/movieinfos/{id}", "specific-id")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var response = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(response.getId(), equalTo("specific-id"));
                    assertThat(response.getName(), equalTo("New Title"));
                });
    }

    @Test
    void deleteMovieInfo() {
        when(moviesInfoServiceMock.deleteMovieInfo(ArgumentMatchers.isA(String.class)))
                .thenReturn(Mono.empty().ofType(Void.class));

        client
                .delete()
                .uri("/v1/movieinfos/{id}", "some-id")
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }

    @Test
    void addMovieInfo_validation_name() {
        var movieInfo = new MovieInfo(null, "", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.addMovieInfo(ArgumentMatchers.isA(MovieInfo.class)))
                .thenCallRealMethod();

        client
                .post()
                .uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    assertThat(response, equalTo("movieInfo.name must be present"));
                });
    }

    @Test
    void addMovieInfo_validation_year() {
        var movieInfo = new MovieInfo(null, "Dark Knight Rises", -2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.addMovieInfo(ArgumentMatchers.isA(MovieInfo.class)))
                .thenCallRealMethod();

        client
                .post()
                .uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    assertThat(response, equalTo("movieInfo.year must be a positive"));
                });
    }

    @Test
    void addMovieInfo_validation_name_year() {
        var movieInfo = new MovieInfo(null, "", -2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.addMovieInfo(ArgumentMatchers.isA(MovieInfo.class)))
                .thenCallRealMethod();

        client
                .post()
                .uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    assertThat(response, equalTo("movieInfo.name must be present, movieInfo.year must be a positive"));
                });
    }

    @Test
    void addMovieInfo_validation_cast() {
        var movieInfo = new MovieInfo(null, "Dark Knight Rises", 2012, List.of(""), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.addMovieInfo(ArgumentMatchers.isA(MovieInfo.class)))
                .thenCallRealMethod();

        client
                .post()
                .uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var response = stringEntityExchangeResult.getResponseBody();
                    assertThat(response, equalTo("movieInfo.cast must be present"));
                });
    }

    @Test
    void getMovieInfosByYear() {
        var movieInfo = new MovieInfo("specific-id", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"));
        when(moviesInfoServiceMock.getMovieInfosByYear(2012))
                .thenReturn(Flux.just(movieInfo));

        var uri = UriComponentsBuilder.fromUriString("/v1/movieinfos")
                .queryParam("year", 2012)
                .buildAndExpand().toUri();
        client
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }
}
