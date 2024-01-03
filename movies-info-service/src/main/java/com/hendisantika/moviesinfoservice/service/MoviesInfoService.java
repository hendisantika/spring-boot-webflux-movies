package com.hendisantika.moviesinfoservice.service;

import com.hendisantika.moviesinfoservice.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-webflux-movies
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 1/3/24
 * Time: 08:02
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class MoviesInfoService {

    private final MovieInfoRepository movieInfoRepository;
}
