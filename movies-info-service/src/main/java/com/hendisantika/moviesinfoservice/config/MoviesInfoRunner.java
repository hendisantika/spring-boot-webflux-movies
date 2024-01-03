package com.hendisantika.moviesinfoservice.config;

import com.hendisantika.moviesinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

}
