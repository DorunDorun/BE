package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMsgDeleteRequestDto {
    private String sessionId;      // 방 Id
    private Long messageId;        // 방 메세지 Id
}
