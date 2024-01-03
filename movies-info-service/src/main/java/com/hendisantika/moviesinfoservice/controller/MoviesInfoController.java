package com.hendisantika.moviesinfoservice.controller;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.service.MoviesInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:06
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/v1/movieinfos")
@RequiredArgsConstructor
public class MoviesInfoController {

    private final MoviesInfoService moviesInfoService;
    private final Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many().replay().latest();

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return moviesInfoService.addMovieInfo(movieInfo)
                .doOnNext(savedInfo -> moviesInfoSink.tryEmitNext(savedInfo));
    }

    @GetMapping("")
    public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value = "year", required = false) Integer year) {
        if (year != null) {
            return moviesInfoService.getMovieInfosByYear(year);
        }
        return moviesInfoService.getAllMovieInfos();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
        return moviesInfoService.getMovieInfoById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id) {
        return moviesInfoService.updateMovieInfo(updatedMovieInfo, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id) {
        return moviesInfoService.deleteMovieInfo(id);
    }
}
