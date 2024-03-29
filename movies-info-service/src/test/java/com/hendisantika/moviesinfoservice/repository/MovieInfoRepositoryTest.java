package com.hendisantika.moviesinfoservice.repository;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
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
 * Time: 08:09
 * To change this template use File | Settings | File Templates.
 */
@DataMongoTest
@Testcontainers
@ExtendWith(SpringExtension.class)
class MovieInfoRepositoryTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.4");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


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
    void findAll() {
        var flux = movieInfoRepository.findAll().log();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var mono = movieInfoRepository.findById("specific-id").log();
        StepVerifier.create(mono)
                .assertNext(movieInfo -> {
                    assertThat("specific-id", equalTo(movieInfo.getId()));
                    assertThat("Dark Knight Rises", equalTo(movieInfo.getName()));
                })
                .verifyComplete();
    }

    @Test
    void insert() {
        var movie = new MovieInfo(null, "Movie Title", 2021, List.of("First Last"), LocalDate.parse("2021-01-11"));
        var mono = movieInfoRepository.save(movie).log();
        StepVerifier.create(mono)
                .assertNext(movieInfo -> {
                    assertThat("Movie Title", equalTo(movieInfo.getName()));
                    assertThat(movieInfo.getId(), is(not(nullValue())));
                })
                .verifyComplete();
    }

    @Test
    void update() {
        var movie = movieInfoRepository.findById("specific-id").block();
        movie.setYear(2021);
        var mono = movieInfoRepository.save(movie).log();
        StepVerifier.create(mono)
                .assertNext(movieInfo -> {
                    assertThat(2021, equalTo(movieInfo.getYear()));
                });
    }

    @Test
    void delete() {
        movieInfoRepository.deleteById("specific-id").block();
        var flux = movieInfoRepository.findAll().log();
        StepVerifier.create(flux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findByYear() {
        var flux = movieInfoRepository.findByYear(2005);
        StepVerifier.create(flux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByName() {
        var mono = movieInfoRepository.findByName("Batman Begins");
        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
