package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    @Where(clause = "is_delete = true")
    Optional<ChatRoomUser> findBySessionIdAndUserId(String sessionId, Long userId);

    Optional<ChatRoomUser> findByUserIdAndSessionIdAndIsDelete(Long userId, String sessionId, boolean isDelete);

    List<ChatRoomUser> findAllBySessionIdAndIsDelete(String sessionId, boolean isDelete);

}
