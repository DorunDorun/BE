package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String test;
    private String roomId;
    private String email;
    private String message; // 메시지
    private String imgByteCode; // 이미지 바이트 코드
}
