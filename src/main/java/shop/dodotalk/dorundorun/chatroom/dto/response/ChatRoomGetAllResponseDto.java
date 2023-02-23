package shop.dodotalk.dorundorun.chatroom.dto.response;


import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomGetAllResponseDto {

    private List<ChatRoomResponseDto> chattingRoomList;
    private ChatRoomPageInfoResponseDto pageInfo;


    public ChatRoomGetAllResponseDto(List<ChatRoomResponseDto> chatRoomResponseDtos, ChatRoomPageInfoResponseDto chatRoomPageInfoResponseDto) {
        this.chattingRoomList = chatRoomResponseDtos;
        this.pageInfo = chatRoomPageInfoResponseDto;
    }

}
