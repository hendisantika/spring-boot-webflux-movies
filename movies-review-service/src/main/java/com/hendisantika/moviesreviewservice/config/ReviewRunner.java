package com.hendisantika.moviesreviewservice.config;

import com.hendisantika.moviesreviewservice.domain.Review;
import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:49
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
class ReviewRunner implements CommandLineRunner {

    private final ReviewReactiveRepository reviewReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        reviewReactiveRepository.deleteAll()
                .block();

        var random = new Random();
        var reviews = new ArrayList<Review>();

        for (int i = 0; i < 100; i++) {
            var movieId = random.nextInt(3 - 1 + 1) + 1;
            var rating = Math.round(((10 - 1) * random.nextDouble() + 1) * 10) / 10.0;
            var review = new Review(null, (long) movieId, UUID.randomUUID().toString(), rating);
            reviews.add(review);
        }

        reviewReactiveRepository.saveAll(reviews)
                .blockLast();
    }
}
