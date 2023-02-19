package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ChatMsgDeleteResponseDto {
    private HttpStatus status;
    private String msg;

    public ChatMsgDeleteResponseDto(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
