package shop.dodotalk.dorundorun.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

import java.util.Optional;

@Repository
public interface RoomFileMessageRepository extends JpaRepository<RoomFileMessage, Long> {
    Optional<RoomFileMessage> findBySessionIdAndFileIdAndSocialUid(String sessionId, Long fileId, String socialUid);
}
