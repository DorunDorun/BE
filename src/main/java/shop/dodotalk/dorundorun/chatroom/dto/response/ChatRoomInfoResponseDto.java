package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Getter;

@Getter
public class ChatRoomInfoResponseDto {

    Long totalHour;
    Long totalRoom;

    public ChatRoomInfoResponseDto(Long totalHour, Long totalRoom) {
        this.totalHour = totalHour;
        this.totalRoom = totalRoom;

    }
}
