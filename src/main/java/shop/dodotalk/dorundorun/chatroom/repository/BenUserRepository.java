package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.chatroom.entity.BenUser;

public interface BenUserRepository extends JpaRepository<BenUser, Long> {
    BenUser findByUserIdAndRoomId(Long userId, String sessionId);
}
