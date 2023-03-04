package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;


import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    void delete(ChatRoom room);

//    /*전체 방 조회*/
//    Page<ChatRoom> findByIsDeleteOrderByModifiedAtDesc(@Param("delete") Boolean isDelete, Pageable pageable);

    /*전체 방 조회*/
    @Query("select c from ChatRoom c left join c.chatRoomUserList item where c.isDelete = false and item.isDelete = :isDelete or item = null order by c.modifiedAt DESC")
    Page<ChatRoom> findByIsDelete(@Param("isDelete") boolean isDelete, Pageable pageable);







    /*사용자가 입장했던 전체 방 조회*/
    @Query("select c from ChatRoom c left join c.chatRoomUserList item where c.masterUserId = :masterId or item.isDelete = :isDelete or item.userId = :userId order by c.modifiedAt DESC")
    Page<ChatRoom> findByUserIdAndIsDelete(@Param("masterId") Long masterId, @Param("userId") Long userId, @Param("isDelete") boolean isDelete, Pageable pageable);



    Optional<ChatRoom> findBySessionIdAndIsDelete(String chatRoomId, boolean isDelete);

    Optional<ChatRoom> findBySessionId(String chatRoomId);

    Long countAllBy();



    /*방 제목, 내용, 카테고리 검색*/
/*    Page<Room> findByTitleContainingOrSubtitleContainingOrCategoryContainingOrderByModifiedAtDesc(String title,
                                                                                                  String subtitle,
                                                                                                  Category category,
                                                                                                 Pageable pageable); */

    /*방 제목, 내용 검색*/
    Page<ChatRoom> findByTitleContainingOrSubtitleContainingOrderByModifiedAtDesc(String title,
                                                                                  String subtitle,
                                                                                  Pageable pageable);

    /*카테고리 클릭 시 해당 카테고리 글 반환*/
    Page<ChatRoom> findByCategoryOrderByModifiedAtDesc(Category category, Pageable pageable);

    /* 관우 삭제되지 않은 실시간 채팅방 개수 전부 나타내기 */
    List<ChatRoom> findAllByIsDelete(boolean isDelete);


}
