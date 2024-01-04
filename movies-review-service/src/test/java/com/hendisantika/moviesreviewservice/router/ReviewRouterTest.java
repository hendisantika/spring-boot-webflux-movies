package com.hendisantika.moviesreviewservice.router;

import com.hendisantika.moviesreviewservice.exception.GlobalExceptionHandler;
import com.hendisantika.moviesreviewservice.handler.ReviewHandler;
import com.hendisantika.moviesreviewservice.repository.ReviewReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/4/24
 * Time: 07:50
 * To change this template use File | Settings | File Templates.
 */
@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalExceptionHandler.class})
class ReviewRouterTest {

    @MockBean
    private ReviewReactiveRepository repository;

    @Autowired
    private WebTestClient client;

}
