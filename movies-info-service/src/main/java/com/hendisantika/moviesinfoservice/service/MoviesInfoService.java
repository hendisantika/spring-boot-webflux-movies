package com.hendisantika.moviesinfoservice.service;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:02
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class MoviesInfoService {

    private final MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return repository.save(movieInfo).log();
    }

    public Flux<MovieInfo> getAllMovieInfos() {
        return repository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return repository.findById(id);
    }

}
