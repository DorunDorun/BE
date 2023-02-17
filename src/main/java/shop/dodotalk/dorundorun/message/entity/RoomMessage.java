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
public class RoomMessage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @ManyToOne
    private User sender;
    private String message;
    private String sessionId; // 방 sessionId
    private boolean isDelete = false;

    public RoomMessage(ChatMessageResponseDto chatMessageResponseDto, User user, Room room) {
        this.sender = user;
        this.message = chatMessageResponseDto.getMessage();
        this.sessionId = room.getSessionId();
    }

    public void RoomMessageDelete() {
        this.isDelete = true;
    }
}
