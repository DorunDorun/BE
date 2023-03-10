package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;


import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    void delete(ChatRoom room);


    /*전체 방 조회*/
    Page<ChatRoom> findByIsDeleteAndCntUserAfterOrderByModifiedAtDesc(boolean isDelete, Long cntUser, Pageable pageable);


    /*전체 방 조회*/
    @Query("select distinct room from ChatRoom room " +
            "join room.chatRoomUserList user " +
            "where room.isDelete = :isDelete " +
            "and user.isDelete = :isDelete " +
            "or user = null " +
            "order by room.modifiedAt DESC")
    Page<ChatRoom> findByIsDelete(@Param("isDelete") boolean isDelete, Pageable pageable);


    /*사용자가 입장했던 전체 방 조회
     * user가 null이거나 user가 자기 자신 */
//    @Query("select c from ChatRoom c left join c.chatRoomUserList item where c.masterUserId = :masterId or item.isDelete = :isDelete or item.userId = :userId order by c.modifiedAt DESC")
    @Query("select distinct room from ChatRoom room " +
            "left join room.chatRoomUserList user " +
            "where (room.isDelete = :isDelete2 " +
            "and room.masterUserId = :userId " +
            "or room.isDelete = :isDelete2 " +
            "and user.userId = :userId " +
            "and user.isDelete = :isDelete) " +
            "and room.cntUser > :cntUser " +
            "order by room.modifiedAt DESC")
    Page<ChatRoom> findByUserIdAndIsDelete(
            @Param("userId") Long userId,
            @Param("isDelete") boolean isDelete,
            @Param("isDelete2") boolean isDelete2,
            @Param("cntUser") Long cntUser,
            Pageable pageable);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ChatRoom> findBySessionIdAndIsDelete(String chatRoomId, boolean isDelete);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ChatRoom> findBySessionId(String chatRoomId);

    Long countAllBy();

    /* 채팅방 검색 */
    @Query("select chatRoom from ChatRoom chatRoom WHERE " +
            "(chatRoom.title like %:title% " +
            "or chatRoom.subtitle like %:subtitle%) " +
            "and chatRoom.cntUser > :cntUser ")
    Page<ChatRoom> findByCntUserAfterAndTitleContainingOrSubtitleContainingOrderByModifiedAtDesc(
            @Param("cntUser") Long cntUser,
            @Param("title") String title,
            @Param("subtitle") String subtitle,
            Pageable pageable);

    //    /*히스토리 채팅방 검색*/
    @Query("select distinct room from ChatRoom room " +
            "left join room.chatRoomUserList user " +
            "where room.isDelete = :isDelete2 " +
            "and room.masterUserId = :userId " +
            "or room.isDelete = :isDelete2 " +
            "and user.userId = :userId " +
            "and user.isDelete = :isDelete " +
            "group by room.title, room.subtitle " +
            "HAVING (room.title like %:title% " +
            "or room.subtitle like %:subtitle%) " +
            "and room.cntUser > :cntUser " +
            "order by room.modifiedAt DESC")
    Page<ChatRoom> findByUserIdAndIsDelete(@Param("title") String title, @Param("subtitle") String subtitle,
                                           @Param("userId") Long userId,
                                           @Param("isDelete") boolean isDelete,
                                           @Param("isDelete2") boolean isDelete2,
                                           @Param("cntUser") Long cntUser,
                                           Pageable pageable);


    /*카테고리 클릭 시 해당 카테고리 글 반환*/
    Page<ChatRoom> findByIsDeleteAndCategoryAndCntUserAfterOrderByModifiedAtDesc(boolean isDelete, Category category,
                                                                                 Long cntUser, Pageable pageable);


    List<ChatRoom> findAllByIsDelete(boolean isDelete);

    /* 관우 삭제되지 않은 실시간 채팅방 개수 전부 나타내기 */
    List<ChatRoom> findAllByIsDeleteAndCntUserAfter(boolean isDelete, Long cntUser);


}
