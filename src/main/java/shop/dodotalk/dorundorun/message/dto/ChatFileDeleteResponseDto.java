package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;

@Getter
@NoArgsConstructor
public class ChatFileDeleteResponseDto {
    private HttpStatus status;
    private String msg;
    private Long fileId;
    private boolean isDelete;

    public ChatFileDeleteResponseDto(HttpStatus status, String msg, RoomFileMessage roomFileMessage) {
        this.status = status;
        this.msg = msg;
        this.fileId = roomFileMessage.getFileId();
        this.isDelete = roomFileMessage.isDelete();
    }
}
