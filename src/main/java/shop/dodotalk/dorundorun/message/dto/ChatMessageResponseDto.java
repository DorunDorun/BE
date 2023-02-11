package shop.dodotalk.dorundorun.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {
    private Long messageId;
    private String email;
    private String message; // 메시지
    private String imgUrl;

    public ChatMessageResponseDto(ChatMessageRequestDto chatMessageRequestDto) {
        this.messageId = 1L;
        this.email = chatMessageRequestDto.getEmail();
        this.message = chatMessageRequestDto.getMessage();
    }

    public void ChatMessageImgUpdate(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
