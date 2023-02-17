package shop.dodotalk.dorundorun.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;

@Repository
public interface RoomFileMessageRepository extends JpaRepository<RoomFileMessage, Long> {
}
