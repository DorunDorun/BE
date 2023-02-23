package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String sessionId; // 방 세션 id
    private String socialUid; // 유저 소셜 id
    private String nickname; // 유저 닉네임
    private String message; // 메시지
    private String imgByteCode; // 이미지 바이트 코드
}
