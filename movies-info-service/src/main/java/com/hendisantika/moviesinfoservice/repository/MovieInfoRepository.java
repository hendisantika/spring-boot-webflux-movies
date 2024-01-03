package com.hendisantika.moviesinfoservice.repository;

import com.hendisantika.moviesinfoservice.entity.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:01
 * To change this template use File | Settings | File Templates.
 */
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findByYear(Integer year);

    Mono<MovieInfo> findByName(String name);
}
