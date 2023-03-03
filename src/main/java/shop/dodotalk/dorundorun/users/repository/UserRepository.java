package shop.dodotalk.dorundorun.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, String provider);

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialUid(String socialUid);
}
