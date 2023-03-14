//package shop.dodotalk.dorundorun.hyunjun.redis;
//
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mapstruct.Context;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisKeyValueAdapter;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import shop.dodotalk.dorundorun.security.entity.RefreshTokenRedisTest;
//import shop.dodotalk.dorundorun.security.repository.RefreshTokenRedisTestRepository;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;
//
//
//@SpringBootTest
//public class RedisCrudTest extends AbstractContainerBaseTest {
//
//    @Autowired
//    private RefreshTokenRedisTestRepository refreshTokenRedisTestRepository;
//
//
//    private RefreshTokenRedisTest refreshTokenRedisTest;
//
//    @Autowired
//    @Qualifier("tokenRedisConnectionTemplate")
//    RedisTemplate<String, String> redisTemplate;
//
//    @BeforeEach
//    void setUp() {
////        System.out.println("Test 객체 생성");
//
//        /*given*/
////        refreshTokenRedisTest = new RefreshTokenRedisTest("99999999", "abcd1234", 2L);
//
//
//    }
//
//    @AfterEach
//    void teardown() {
////        System.out.println("Test 객체 삭제");
////        refreshTokenRedisTestRepository.deleteById(refreshTokenRedisTest.getUserId());
//    }
//
//    @Test
//    @DisplayName("Redis TTL 만료 후 Set 제거 확인 테스트")
//    public void redis_ttl_save_test() throws InterruptedException {
//
//        /*given*/
//        refreshTokenRedisTest = new RefreshTokenRedisTest("12345", "abcdefg", 1L);
//
//        /*when*/
//        refreshTokenRedisTestRepository.save(refreshTokenRedisTest);
//
//        /*저장 후 카운트 -> TTL이 아직 안지났으므로 값 '1' 예상*/
//        System.out.println(refreshTokenRedisTestRepository.count());
//
//        /*TTL 설정 시간 이상으로 쓰레드 슬립*/
//        Thread.sleep(3000L);
//
//
//        /*then
//         *TTL이 지났으므로 데이터는 삭제되어 '0' 예상*/
//        assertEquals(0, refreshTokenRedisTestRepository.count());
//    }
//
//    @Test
//    @DisplayName("Redis 데이터 save")
//    public void redis_save_test() throws InterruptedException {
//
//        /*given*/
//        refreshTokenRedisTest = new RefreshTokenRedisTest("12345", "abcdefg", 1L);
//
//
//        /*when*/
//        /*기존 레디스 사용 방식 처럼 2가지의 방식으로 테스트 가능하다.*/
//        //1.
//        refreshTokenRedisTestRepository.save(refreshTokenRedisTest);
//
//        RefreshTokenRedisTest tokenRedis = refreshTokenRedisTestRepository
//                .findById(refreshTokenRedisTest.getUserId())
//                .orElseThrow(RuntimeException::new);
//        //2.
//        redisTemplate.opsForValue().set("hello", "world");
//        ValueOperations<String, String> vop = redisTemplate.opsForValue();
//        String value = vop.get("hello");
//
//
//        /*then*/
//        assertThat(tokenRedis.getUserId()).isEqualTo(refreshTokenRedisTest.getUserId());
//        assertThat(tokenRedis.getRefreshToken()).isEqualTo(refreshTokenRedisTest.getRefreshToken());
//        assertThat(tokenRedis.getTimeToLive()).isEqualTo(refreshTokenRedisTest.getTimeToLive());
//        assertThat(value).isEqualTo("world");
//    }
//
//
//
//    @Test
//    @DisplayName("Redis 데이터 update")
//    public void redis_update_test() {
//        // given
//        refreshTokenRedisTestRepository.save(refreshTokenRedisTest);
//
//        RefreshTokenRedisTest tokenRedis = refreshTokenRedisTestRepository
//                .findById(refreshTokenRedisTest.getUserId())
//                .orElseThrow(RuntimeException::new);
//
//        // when
//        tokenRedis.update("99119911", "efgh5678");
//        refreshTokenRedisTestRepository.save(tokenRedis);
//
//
//        // then
//        assertThat(tokenRedis.getUserId()).isEqualTo("99119911");
//        assertThat(tokenRedis.getRefreshToken()).isEqualTo("efgh5678");
//
//
//        RefreshTokenRedisTest tokenRedis2 = refreshTokenRedisTestRepository
//                .findById(refreshTokenRedisTest.getUserId())
//                .orElseThrow(RuntimeException::new);
//
//        System.out.println(tokenRedis2);
//
//    }
//
//    @Test
//    @DisplayName("Redis 에 데이터를 삭제하면 정상적으로 삭제되어야 한다")
//    public void redis_delete_test() {
//        // given
//        refreshTokenRedisTestRepository.save(refreshTokenRedisTest);
//
//        // when
//        refreshTokenRedisTestRepository.delete(refreshTokenRedisTest);
//        Optional<RefreshTokenRedisTest> deletedToken = refreshTokenRedisTestRepository.findById(refreshTokenRedisTest.getUserId());
//
//        // then
//        assertTrue(deletedToken.isEmpty());
//    }
//}
