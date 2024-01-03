package com.hendisantika.moviesinfoservice.controller;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import com.hendisantika.moviesinfoservice.service.MoviesInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
