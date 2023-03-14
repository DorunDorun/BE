package shop.dodotalk.dorundorun.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dodotalk.dorundorun.security.entity.RefreshToken;
import shop.dodotalk.dorundorun.security.repository.RefreshTokenRedisTestRepository;
import shop.dodotalk.dorundorun.security.repository.RefreshTokenRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


/*Refresh Token 사용 시, EC2 Redis와 RDS의 MySql의 속도차이를 측정하기 위해 만든 컨트롤러입니다.*/
@RestController
@RequiredArgsConstructor
public class DatabaseTestController {

    /*Mysql*/
    private final RefreshTokenRepository repository;

    /*Redis*/
    @Autowired
    @Qualifier("stringObjectTemplate")
    RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/test/redis")
    public void redisTest() {


        /*Access Token이 만료됬다고 가정하고 Refresh토큰을 재발급 하는 과정*/
        String newRefreshToken = "dcba4321";
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("TestRedis", "userId", newRefreshToken);
        redisTemplate.expire("TestRedis", 10, TimeUnit.SECONDS);


    }

    @GetMapping("/test/mysql")
    public void mysqlTest() {

        /*Access Token이 만료됬다고 가정하고 Refresh토큰을 재발급 하는 과정*/
        String newRefreshToken = "dcba4321";
        RefreshToken newToken = RefreshToken.createToken("userId", newRefreshToken);
        repository.save(newToken);


    }
}
