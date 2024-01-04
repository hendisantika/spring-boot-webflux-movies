package com.hendisantika.moviesservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:05
 * To change this template use File | Settings | File Templates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Review {

    private String reviewId;

    @NotNull(message = "review.movieInfoId must not be null")
    private Long movieInfoId;
    private String comment;

    @Min(value = 0L, message = "review.rating must be positive")
    private Double rating;
}
