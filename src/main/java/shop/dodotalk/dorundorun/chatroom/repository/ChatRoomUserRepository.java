package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Long countAllBySessionId(String sessionId);

    @Where(clause = "is_delete = true")
    Optional<ChatRoomUser> findBySessionIdAndUserId(String sessionId, Long userId);

    Optional<ChatRoomUser> findByUserIdAndSessionIdAndIsDelete(Long userId, String sessionId, boolean isDelete);

    List<ChatRoomUser> findAllBySessionIdAndIsDelete(String sessionId, boolean isDelete);




}
