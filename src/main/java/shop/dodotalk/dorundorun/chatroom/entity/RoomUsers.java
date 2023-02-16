package shop.dodotalk.dorundorun.chatroom.entity;

import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomUserId;

    // 채팅방
    @Column
    private String sessionId;

    @Column
    private Long user;

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



    public void deleteRoomUsers (LocalDateTime roomExitTime) {
        this.isDelete = true;
        this.roomExitTime = roomExitTime;

    }


}
