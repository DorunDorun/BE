package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.Room;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    void delete(Room room);


    Page<Room> findByIsDeleteOrderByModifiedAtDesc(Boolean isDelete, Pageable pageable);




    // Containing을 붙여주면 Like 검색이 가능
    // %{keyword}%가 가능
    Page<Room> findByTitleContainingOrderByModifiedAtDesc(String keyword, Pageable pageable);
}
