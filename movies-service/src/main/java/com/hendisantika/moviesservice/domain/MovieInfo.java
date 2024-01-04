package com.hendisantika.moviesservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

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
public class MovieInfo {
    private String movieInfoId;

    @NotBlank(message = "movieInfo.name must be present")
    private String name;

    @NotNull
    @Positive(message = "movieInfo.year must be a positive")
    private Integer year;

    private List<@NotBlank(message = "movieInfo.cast must be present") String> cast;

    private LocalDate release_date;
}
