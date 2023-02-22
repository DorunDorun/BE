package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    Long countAllBySessionId(String sessionId);

    Optional<ChatRoomUser> findBySessionIdAndNickname(String sessionId, String Nickname);


    @Where(clause = "is_delete = true")
    Optional<ChatRoomUser> findBySessionIdAndUserId(String sessionId, Long userId);



//    is_delete = false 동작 안함.
//    @Where(clause = "is_delete = false")
//    Optional<ChatRoomUser> findByUserIdAndSessionId(Long userId, String sessionId);

    Optional<ChatRoomUser> findByUserIdAndSessionIdAndIsDelete(Long userId, String sessionId, boolean isDelete);



    List<ChatRoomUser> findAllBySessionIdAndIsDelete(String sessionId, boolean isDelete);

    List<ChatRoomUser> findAllBySessionId(String sessionId);



}
