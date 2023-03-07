package shop.dodotalk.dorundorun.message.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.Timestamped;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;
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
    private String socialUid;
    private String name;
    private String nickname;
    private String profile;
    private String message;
    private String sessionId; // 방 sessionId
    private boolean isDelete = false;

    public RoomMessage(ChatMessageRequestDto chatMessageRequestDto) {
        this.socialUid = chatMessageRequestDto.getSocialUid();
        this.name = chatMessageRequestDto.getName();
        this.nickname = chatMessageRequestDto.getNickname();
        this.profile = chatMessageRequestDto.getProfile();
        this.message = chatMessageRequestDto.getMessage();
        this.sessionId = chatMessageRequestDto.getSessionId();
    }

    public void RoomMessageDelete() {
        this.isDelete = true;
    }
}
