package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;

import java.util.List;
import java.util.Optional;

public interface RoomUsersRepository extends JpaRepository<RoomUsers, Long> {

    Long countAllBySessionId(String sessionId);

    Optional<RoomUsers> findBySessionIdAndNickname(String sessionId, String Nickname);

    Optional<RoomUsers> findBySessionIdAndUserId(String sessionId, Long userId);

    List<RoomUsers> findAllBySessionId(String sessionId);



}
