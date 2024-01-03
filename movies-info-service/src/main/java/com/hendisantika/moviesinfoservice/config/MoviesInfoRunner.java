package com.hendisantika.moviesinfoservice.config;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:05
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
class MoviesInfoRunner implements CommandLineRunner {

    private final MovieInfoRepository movieInfoRepository;

    @Override
    public void run(String... args) throws Exception {
        movieInfoRepository.deleteAll()
                .block();

        var movieInfos = List.of(
                new MovieInfo("1", "Batman Begins", 2005, List.of("Christian Bale"), LocalDate.parse("2005-06-15")),
                new MovieInfo("2", "The Dark Knight", 2008, List.of("Christian Bale"), LocalDate.parse("2008-07-18")),
                new MovieInfo("3", "Dark Knight Rises", 2012, List.of("Christian Bale"), LocalDate.parse("2012-07-20"))
        );

        movieInfoRepository.saveAll(movieInfos)
                .blockLast();
    }
}
