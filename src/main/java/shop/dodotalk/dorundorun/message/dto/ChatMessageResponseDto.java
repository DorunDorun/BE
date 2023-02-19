package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long messageId;
    private Long fileId;
    private String nickname;
    private String message; // 메시지
    private String imgUrl;
    private Boolean isDelete = false;

    public ChatMessageResponseDto(RoomFileMessage roomFileMessage) {
        this.fileId = roomFileMessage.getFileId();
        this.nickname = roomFileMessage.getNickname();
        this.imgUrl = roomFileMessage.getImgUrl();
    }
    public ChatMessageResponseDto(RoomMessage roomMessage) {
        this.messageId = roomMessage.getMessageId();
        this.nickname = roomMessage.getNickname();
        this.message = roomMessage.getMessage();
    }

}
