package shop.dodotalk.dorundorun.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

import java.util.Optional;

@Repository
public interface RoomMessageRepository extends JpaRepository<RoomMessage, Long> {
    Optional<RoomMessage> findBySessionIdAndMessageIdAndSocialUid(String sessionId, Long messageId, String socialUid);
    Optional<RoomMessage> findBySessionId(String sessionId);
    Optional<RoomMessage> findByMessageId(Long messageId);
    Optional<RoomMessage> findBySocialUid(String socialUid);
}
