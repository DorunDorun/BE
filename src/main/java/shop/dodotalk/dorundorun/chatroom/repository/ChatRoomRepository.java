package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;


import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    void delete(ChatRoom room);

    @Where(clause = "is_delete = false")
    Page<ChatRoom> findByIsDeleteOrderByModifiedAtDesc(Boolean isDelete, Pageable pageable);




    Optional<ChatRoom> findBySessionIdAndIsDelete(String chatRoomId, boolean isDelete);

    Optional<ChatRoom> findBySessionId(String chatRoomId);

    // Containing을 붙여주면 Like 검색이 가능 --> %{keyword}%x

    /*방 제목, 내용, 카테고리 검색*/
/*    Page<Room> findByTitleContainingOrSubtitleContainingOrCategoryContainingOrderByModifiedAtDesc(String title,
                                                                                                  String subtitle,
                                                                                                  Category category,
                                                                                                 Pageable pageable); */

    /*방 제목, 내용 검색*/
    Page<ChatRoom> findByTitleContainingOrSubtitleContainingOrderByModifiedAtDesc(String title,
                                                                                                  String subtitle,
                                                                                                  Pageable pageable);


    /* 관우 삭제되지 않은 실시간 채팅방 개수 전부 나타내기 */
    List<ChatRoom> findAllByIsDelete(boolean isDelete);


}
