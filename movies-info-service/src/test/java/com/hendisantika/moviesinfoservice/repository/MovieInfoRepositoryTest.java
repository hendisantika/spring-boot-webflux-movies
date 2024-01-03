package com.hendisantika.moviesinfoservice.repository;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

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
@ExtendWith(SpringExtension.class)
class MovieInfoRepositoryTests {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

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
}
