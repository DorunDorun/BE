package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;

@Getter
@NoArgsConstructor
public class ChatMsgDeleteResponseDto {
    private HttpStatus status;
    private String msg;
    private Long messageId;
    private boolean isDelete;

    public ChatMsgDeleteResponseDto(HttpStatus status, String msg, RoomMessage roomMessage) {
        this.status = status;
        this.msg = msg;
        this.messageId = roomMessage.getMessageId();
        this.isDelete = roomMessage.isDelete();
    }
}
