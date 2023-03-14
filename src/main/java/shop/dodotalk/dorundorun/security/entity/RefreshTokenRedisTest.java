//package shop.dodotalk.dorundorun.security.entity;
//
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//import org.springframework.data.redis.core.TimeToLive;
//import org.springframework.data.redis.core.index.Indexed;
//
//import java.io.Serializable;
//
//
//@Data
//@NoArgsConstructor
//@RedisHash(value = "TestRefreshTokenTest", timeToLive = 604800)// 1주일
//public class RefreshTokenRedisTest {
//
//    @Id
//    private String userId;
//
//    @Indexed
//    private String refreshToken;
//
//    @TimeToLive
//    private Long timeToLive;
//
//    /* Test Code용 생성자 */
//    public RefreshTokenRedisTest(final String userId, final String refreshToken, final Long timeToLive) {
//        this.userId = userId;
//        this.refreshToken = refreshToken;
//        this.timeToLive = timeToLive;
//    }
//
//
//    public static RefreshTokenRedisTest createToken(String userId, String refreshToken, Long timeToLive) {
//        return new RefreshTokenRedisTest(userId, refreshToken ,timeToLive);
//    }
//
//    public RefreshTokenRedisTest update(String userId, String refreshToken) {
//        this.userId = userId;
//        this.refreshToken = refreshToken;
//        return this;
//    }
//
//
//}
