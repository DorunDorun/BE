package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long messageId;
    private Long fileId;
    private String sessionId;
    private String nickname;
    private String message; // 메시지
    private String imgUrl;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ChatMessageResponseDto(RoomFileMessage roomFileMessage) {
        this.fileId = roomFileMessage.getFileId();
        this.sessionId = roomFileMessage.getSessionId();
        this.nickname = roomFileMessage.getNickname();
        this.imgUrl = roomFileMessage.getImgUrl();
        this.isDelete = roomFileMessage.isDelete();
        this.createdAt = roomFileMessage.getCreatedAt();
        this.modifiedAt = roomFileMessage.getModifiedAt();
    }
    public ChatMessageResponseDto(RoomMessage roomMessage) {
        this.messageId = roomMessage.getMessageId();
        this.sessionId = roomMessage.getSessionId();
        this.nickname = roomMessage.getNickname();
        this.message = roomMessage.getMessage();
        this.isDelete = roomMessage.isDelete();
        this.createdAt = roomMessage.getCreatedAt();
        this.modifiedAt = roomMessage.getModifiedAt();
    }

}
