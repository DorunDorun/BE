package shop.dodotalk.dorundorun.message.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.Room;
import shop.dodotalk.dorundorun.chatroom.entity.Timestamped;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RoomFileMessage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    @ManyToOne
    private User sender;
    private String imgUrl;
    private String sessionId; // 방 sessionId
    private boolean isDelete = false;

    public RoomFileMessage(ChatMessageResponseDto chatMessageResponseDto, User user, Room room) {
        this.sender = user;
        this.imgUrl = chatMessageResponseDto.getImgUrl();
        this.sessionId = room.getSessionId();
    }

    public void RoomFileDelete() {
        this.isDelete = true;
    }
}
