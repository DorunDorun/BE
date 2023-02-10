package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Setter 이거 없애니까 message, userJWT roomUUID가 회색빛으로 바뀜
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long chatRoomId;
    private String userEmail;
    private String message; // 메시지
    private String imgCode;

    public ChatMessageResponseDto(ChatMessageRequestDto chatMessageRequestDto) {
        this.chatRoomId = chatMessageRequestDto.getChatRoomId();
        this.userEmail = chatMessageRequestDto.getUserEmail();
        this.message = chatMessageRequestDto.getMessage();
        this.imgCode = chatMessageRequestDto.getImgCode();
    }
}
