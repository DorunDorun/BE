package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Long countAllBySessionId(String sessionId);

    Optional<ChatRoomUser> findBySessionIdAndNickname(String sessionId, String Nickname);

    Optional<ChatRoomUser> findBySessionIdAndUserId(String sessionId, Long userId);

    List<ChatRoomUser> findAllBySessionId(String sessionId);



}
