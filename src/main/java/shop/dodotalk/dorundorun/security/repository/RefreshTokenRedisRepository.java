package shop.dodotalk.dorundorun.security.repository;

import org.springframework.data.repository.CrudRepository;
import shop.dodotalk.dorundorun.security.entity.RefreshTokenRedis;

import java.util.Optional;


public interface RefreshTokenRedisRepository extends CrudRepository<RefreshTokenRedis, String> {

    Optional<RefreshTokenRedis> findByRefreshToken(String refreshToken);
}
