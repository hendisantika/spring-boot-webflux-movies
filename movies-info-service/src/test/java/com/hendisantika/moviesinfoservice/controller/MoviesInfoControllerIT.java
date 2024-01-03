package com.hendisantika.moviesinfoservice.controller;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:25
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
//@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@Testcontainers
class MoviesInfoControllerIT {

    @Autowired
    WebTestClient client;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.4"));


    @BeforeEach
    void setup() {
        var movieInfos = List.of(
                new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale"), LocalDate.parse("2008-07-18")),
                new MovieInfo("specific-id", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"))
        );
        movieInfoRepository.saveAll(movieInfos)
                .blockLast(); // to make sure data is saved before start of tests
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll()
                .block();
    }

    @Test
    void addMovieInfo() {
        var movie = new MovieInfo(null, "Movie Title", 2021, List.of("First Last"), LocalDate.parse("2021-01-11"));

        client.post()
                .uri("/v1/movieinfos")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saved = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(saved.getId(), is(not(nullValue())));
                });
    }

    @Test
    void getAllMovieInfos() {
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
        client.get()
                .uri("/v1/movieinfos/{id}", "specific-id")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }

    @Test
    void updateMovieInfo() {
        var updateInfo = new MovieInfo(null, "new movie", 2022, List.of("actor"), LocalDate.parse("2022-01-12"));
        client.put()
                .uri("/v1/movieinfos/{id}", "specific-id")
                .bodyValue(updateInfo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var response = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(response.getId(), is(not(nullValue())));
                    assertThat(response.getName(), equalTo("new movie"));
                    assertThat(response.getYear(), equalTo(2022));
                    assertThat(response.getCast().size(), equalTo(1));
                    assertThat(response.getReleaseDate(), equalTo(LocalDate.parse("2022-01-12")));
                });
    }

    @Test
    void deleteMovieInfo() {
        client.delete()
                .uri("/v1/movieinfos/{id}", "specific-id")
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }

    @Test
    void updateMovieInfo_notFound() {
        var updateInfo = new MovieInfo(null, "new movie", 2022, List.of("actor"), LocalDate.parse("2022-01-12"));
        client.put()
                .uri("/v1/movieinfos/{id}", "non-existing-id")
                .bodyValue(updateInfo)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getMovieInfoById_notFound() {
        client.get()
                .uri("/v1/movieinfos/{id}", "non-existing-id")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getMovieInfosByYear() {
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

    @Test
    void getMovieInfoStream() {
        var movie = new MovieInfo(null, "Movie Title", 2021, List.of("First Last"), LocalDate.parse("2021-01-11"));
        client.post()
                .uri("/v1/movieinfos")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saved = movieInfoEntityExchangeResult.getResponseBody();
                    assertThat(saved.getId(), is(not(nullValue())));
                });


        var moviesStreamFlux = client
                .get()
                .uri("/v1/movieinfos/stream")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MovieInfo.class)
                .getResponseBody();


        StepVerifier.create(moviesStreamFlux.log())
                .assertNext(movieInfo -> {
                    assertThat(movieInfo.getId(), is(not(nullValue())));
                })
                .thenCancel();
    }
}
