package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatFileDeleteRequestDto {
    private String sessionId;      // 방 Id
    private Long fileId;           // 방 메세지 파일 Id
}
