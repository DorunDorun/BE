package shop.dodotalk.dorundorun.chatroom.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomUserId;

    // 채팅방
    @Column
    private String sessionId;

    @Column
    private Long userId;

    @Column
    private String social;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String profileImage;


    @Column
    private String enterRoomToken;


    // 삭제 여부 -> 방에서 나간 여부
    @Column
    private boolean isDelete;


    // 방에 들어온 시간 기록.
    @Column
    private LocalDateTime roomEnterTime;

    // 방에서 나간 시간 기록
    @Column
    private LocalDateTime roomExitTime;


    /*방에서 총 머문 일자 (재 입장 다 합쳐서)*/
    @Column
    private Long roomStayDay;


    /*방에서 총 머문 시간 (재 입장 다 합쳐서)*/
    @Column
    private Time roomStayTime;

    @Column
    private Long mediaBackImage;



    /* 방에서 나가는 경우  */
    public void deleteRoomUsers (LocalDateTime roomExitTime, LocalTime roomStayTime, Long roomStayDay) {
        this.isDelete = true;
        this.roomExitTime = roomExitTime;
        this.roomStayTime = Time.valueOf(roomStayTime);
        this.roomStayDay = roomStayDay;
    }

    /* 방에 재입장 하는 경우 */
    public void reEnterRoomUsers(String enterRoomToken, String nickname, Long mediaBackImage) {
        this.isDelete = false;
        this.roomEnterTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();
        this.roomExitTime = null;
        this.enterRoomToken = enterRoomToken;
        this.nickname = nickname;
        this.mediaBackImage = mediaBackImage;
    }



}
