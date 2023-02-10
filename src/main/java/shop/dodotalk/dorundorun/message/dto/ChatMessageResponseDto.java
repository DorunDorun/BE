package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
