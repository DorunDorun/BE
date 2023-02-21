package shop.dodotalk.dorundorun.chatroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    void delete(ChatRoom room);


    Page<ChatRoom> findByIsDeleteOrderByModifiedAtDesc(Boolean isDelete, Pageable pageable);
//    Page<ChatRoom> findByIsDeleteAndChatRoomUserList_IsDeleteOrderByModifiedAtDesc(Boolean isDelete, Pageable pageable);





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
