package com.hendisantika.moviesinfoservice.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:09
 * To change this template use File | Settings | File Templates.
 */
@DataMongoTest
@ExtendWith(SpringExtension.class)
class MovieInfoRepositoryTests {

    @Autowired
    MovieInfoRepository movieInfoRepository;

}
