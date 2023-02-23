package shop.dodotalk.dorundorun.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserEmail(String userId);

    Optional<RefreshToken> findByToken(String refreshToken);
}
