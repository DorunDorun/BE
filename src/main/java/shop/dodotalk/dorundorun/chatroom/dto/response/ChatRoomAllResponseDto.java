package shop.dodotalk.dorundorun.chatroom.dto.response;


import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomAllResponseDto {

    private List<ChatRoomResponseDto> chattingRoomList;
    private ChatRoomPageInfoResponseDto pageInfo;


    public ChatRoomAllResponseDto(List<ChatRoomResponseDto> chatRoomResponseDtos, ChatRoomPageInfoResponseDto chatRoomPageInfoResponseDto) {
        this.chattingRoomList = chatRoomResponseDtos;
        this.pageInfo = chatRoomPageInfoResponseDto;
    }

}
