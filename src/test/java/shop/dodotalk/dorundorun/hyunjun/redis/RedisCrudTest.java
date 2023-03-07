//package shop.dodotalk.dorundorun.hyunjun.redis;
//
//
//
//
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import shop.dodotalk.dorundorun.security.entity.RefreshTokenRedis;
//import shop.dodotalk.dorundorun.security.repository.RefreshTokenRedisRepository;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@SpringBootTest
//public class RedisCrudTest extends AbstractContainerBaseTest {
//
//    @Autowired
//    private RefreshTokenRedisRepository refreshTokenRedisRepository;
//
//
//    private RefreshTokenRedis refreshTokenRedis;
//
//    @Autowired
//    @Qualifier("tokenRedisConnectionTemplate")
//    RedisTemplate<String, String> redisTemplate;
//
//    @BeforeEach
//    void setUp() {
//        System.out.println("@BeforeEach 실행 !");
//        refreshTokenRedis = new RefreshTokenRedis("1", "abcd1234");
//        System.out.println("@BeforeEach 끝 !");
//
//
//    }
//
//    @AfterEach
//    void teardown() {
//        System.out.println("@AfterEach 시작 !");
//        refreshTokenRedisRepository.deleteById(refreshTokenRedis.getUserId());
//        System.out.println("@AfterEach 끝 !");
//    }
//
//    @Test
//    @DisplayName("Redis 에 데이터를 저장하면 정상적으로 조회되어야 한다")
//    public void redis_save_test() {
//
////        redisTemplate.opsForValue().set("hello", "world");
//
//
//        System.out.println(refreshTokenRedisRepository);
//        System.out.println(refreshTokenRedis);
//
//        // given
//        refreshTokenRedisRepository.save(refreshTokenRedis);
//
//        // when
//        RefreshTokenRedis tokenRedis = refreshTokenRedisRepository.findById(refreshTokenRedis.getUserId())
//                .orElseThrow(RuntimeException::new);
//
//        // then
//        assertThat(tokenRedis.getUserId()).isEqualTo(refreshTokenRedis.getUserId());
//        assertThat(tokenRedis.getRefreshToken()).isEqualTo(refreshTokenRedis.getRefreshToken());
//    }
//
//    @Test
//    @DisplayName("Redis 에 데이터를 수정하면 정상적으로 수정되어야 한다")
//    public void redis_update_test() {
//        // given
//        refreshTokenRedisRepository.save(refreshTokenRedis);
//
//        RefreshTokenRedis tokenRedis = refreshTokenRedisRepository.findById(refreshTokenRedis.getUserId())
//                .orElseThrow(RuntimeException::new);
//
//        // when
//        tokenRedis.update("5", "12345678");
//        refreshTokenRedisRepository.save(tokenRedis);
//
//        // then
//        assertThat(tokenRedis.getUserId()).isEqualTo("5");
//        assertThat(tokenRedis.getRefreshToken()).isEqualTo("12345678");
//    }
//
//    @Test
//    @DisplayName("Redis 에 데이터를 삭제하면 정상적으로 삭제되어야 한다")
//    public void redis_delete_test() {
//        // given
//        refreshTokenRedisRepository.save(refreshTokenRedis);
//
//        // when
//        refreshTokenRedisRepository.delete(refreshTokenRedis);
//        Optional<RefreshTokenRedis> deletedToken = refreshTokenRedisRepository.findById(refreshTokenRedis.getUserId());
//
//        // then
//        assertTrue(deletedToken.isEmpty());
//    }
//}
