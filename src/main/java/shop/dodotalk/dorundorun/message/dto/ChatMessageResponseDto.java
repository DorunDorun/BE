package shop.dodotalk.dorundorun.message.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long messageId;
    private Long fileId;
    private String sessionId;
    private String nickname;
    private String message;
    private String imgUrl;
    private Boolean isDelete;
    private String createdAt;
    private String modifiedAt;

    public ChatMessageResponseDto(RoomFileMessage roomFileMessage) {
        this.fileId = roomFileMessage.getFileId();
        this.sessionId = roomFileMessage.getSessionId();
        this.nickname = roomFileMessage.getNickname();
        this.imgUrl = roomFileMessage.getImgUrl();
        this.isDelete = roomFileMessage.isDelete();
        this.createdAt = roomFileMessage.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.modifiedAt = roomFileMessage.getModifiedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    public ChatMessageResponseDto(RoomMessage roomMessage) {
        this.messageId = roomMessage.getMessageId();
        this.sessionId = roomMessage.getSessionId();
        this.nickname = roomMessage.getNickname();
        this.message = roomMessage.getMessage();
        this.isDelete = roomMessage.isDelete();
        this.createdAt = roomMessage.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.modifiedAt = roomMessage.getModifiedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
