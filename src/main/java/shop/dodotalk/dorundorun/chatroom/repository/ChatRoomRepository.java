package shop.dodotalk.dorundorun.chatroom.repository;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;


import javax.persistence.LockModeType;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    void delete(ChatRoom room);


    /*전체 채팅방 조회*/
    @Query(
            value = "SELECT distinct room FROM ChatRoom room " +
                    "left JOIN FETCH room.chatRoomUserList user " +
                    "WHERE room.isDelete = false " +
                    "AND user.isDelete = :isDelete " +
                    "AND room.cntUser > 0 " +
                    "or room.master = '두런두런' " +
                    "ORDER BY room.modifiedAt DESC",
            countQuery = "SELECT count(distinct room) FROM ChatRoom room " +
                    "left JOIN room.chatRoomUserList user " +
                    "WHERE room.isDelete = false " +
                    "AND user.isDelete = :isDelete " +
                    "AND room.cntUser > 0 " +
                    "or room.master = '두런두런'"
    )
    Page<ChatRoom> findByIsDelete(@Param("isDelete") boolean isDelete, Pageable pageable);


    /*히스토리 채팅방 전체 조회.*/
    @Query(
            value = "SELECT distinct room FROM ChatRoom room " +
                    "left JOIN room.chatRoomUserList user " +
                    "WHERE room.isDelete = false " +
                    "AND user.userId = :userId " +
                    "AND room.cntUser > 0 " +
                    "ORDER BY room.modifiedAt DESC"
    )
    Page<ChatRoom> findByUserId(@Param("userId") Long userId, Pageable pageable);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ChatRoom> findBySessionIdAndIsDelete(String chatRoomId, boolean isDelete);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ChatRoom> findBySessionId(String chatRoomId);

    Long countAllBy();


    @Query(
            value = "SELECT distinct room FROM ChatRoom room " +
                    "LEFT JOIN FETCH room.chatRoomUserList user " +
                    "WHERE ((room.title like %:title% " +
                    "or room.subtitle like %:subtitle%) " +
                    "And room.isDelete = false " +
                    "AND user.isDelete = false " +
                    "AND room.cntUser > 0) " +
                    "or ((room.title like %:title% " +
                    "or room.subtitle like %:subtitle%) " +
                    "and room.master = '두런두런') " +
                    "ORDER BY room.modifiedAt DESC",
            countQuery = "SELECT count(distinct room) FROM ChatRoom room " +
                    "left JOIN room.chatRoomUserList user " +
                    "WHERE ((room.title like %:title% " +
                    "or room.subtitle like %:subtitle%) " +
                    "and room.isDelete = false " +
                    "AND user.isDelete = false " +
                    "AND room.cntUser > 0) " +
                    "or ((room.title like %:title% " +
                    "or room.subtitle like %:subtitle%) " +
                    "and room.master = '두런두런') "
    )/*채팅방 검색하기.*/
    Page<ChatRoom> findByTitleContainingOrSubtitleContaining(@Param("title") String title,
                                                             @Param("subtitle") String subtitle,
                                                             Pageable pageable);


    /*히스토리 채팅방 검색*/
    @Query("select distinct room from ChatRoom room " +
            "left join room.chatRoomUserList user " +
            "where room.isDelete = false " +
            "and user.userId = :userId " +
            "and room.cntUser > 0 " +
            "and (room.title like %:title% " +
            "or room.subtitle like %:subtitle%) " +
            "order by room.modifiedAt DESC")
    Page<ChatRoom> findByUserId(@Param("title") String title, @Param("subtitle") String subtitle,
                                @Param("userId") Long userId,
                                Pageable pageable);

    @Query(
            value = "SELECT distinct room FROM ChatRoom room " +
                    "left JOIN FETCH room.chatRoomUserList user " +
                    "WHERE room.isDelete = false " +
                    "AND user.isDelete = false " +
                    "AND room.cntUser > 0 " +
                    "and room.category = :category " +
                    "or room.master = '두런두런' " +
                    "and room.category = :category " +
                    "ORDER BY room.modifiedAt DESC",
            countQuery = "SELECT count(distinct room) FROM ChatRoom room " +
                    "left JOIN room.chatRoomUserList user " +
                    "WHERE room.isDelete = false " +
                    "AND user.isDelete = false " +
                    "AND room.cntUser > 0 " +
                    "and room.category = :category " +
                    "or room.master = '두런두런'" +
                    "and room.category = :category "
    )/*해당 Category 전체 채팅방 조회하기.*/
    Page<ChatRoom> findByCategory(@Param("category") Category category, Pageable pageable);


    List<ChatRoom> findAllByIsDelete(boolean isDelete);

    /* 관우 삭제되지 않은 실시간 채팅방 개수 전부 나타내기 */
    List<ChatRoom> findAllByIsDeleteAndCntUserAfter(boolean isDelete, Long cntUser);


}
