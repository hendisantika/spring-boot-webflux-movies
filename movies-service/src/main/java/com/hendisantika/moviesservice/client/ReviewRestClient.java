package com.hendisantika.moviesservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 08:10
 * To change this template use File | Settings | File Templates.
 */
@Component
@RequiredArgsConstructor
public class ReviewRestClient {

    private final WebClient client;

    @Value("${restClient.reviewsUrl}")
    private String reviewUrl;
}
