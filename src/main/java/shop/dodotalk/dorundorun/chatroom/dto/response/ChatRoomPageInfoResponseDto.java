package shop.dodotalk.dorundorun.chatroom.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomPageInfoResponseDto {

    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
}
