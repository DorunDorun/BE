package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Getter;
import shop.dodotalk.dorundorun.chatroom.entity.Saying;

@Getter
public class ChatRoomSayingResponseDto {

    private String saying;
    private String font;

    public ChatRoomSayingResponseDto(Saying saying) {
        this.saying = saying.getSaying();
        this.font = saying.getFont();
    }
}
