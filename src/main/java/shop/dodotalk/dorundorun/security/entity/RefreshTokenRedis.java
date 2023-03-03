package shop.dodotalk.dorundorun.security.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;


@Data
@RedisHash(value = "refreshToken", timeToLive = 604800)// 1주일
public class RefreshTokenRedis {

    @Id
    private String userId;

    @Indexed
    private String refreshToken;


    public RefreshTokenRedis(final String userId, final String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }



    public static RefreshTokenRedis createToken(String userId, String refreshToken){
        return new RefreshTokenRedis(userId, refreshToken);
    }

    public RefreshTokenRedis update(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        return this;
    }


}
