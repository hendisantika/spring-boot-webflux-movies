package com.hendisantika.moviesreviewservice.repository;

import com.hendisantika.moviesreviewservice.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:46
 * To change this template use File | Settings | File Templates.
 */
public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
    Flux<Review> findByMovieInfoId(Long movieInfoId);
}
