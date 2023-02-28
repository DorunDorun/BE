package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomEnterUsersResponseDto {

    private String sessionId;
    private String title;
    private String subtitle;
    private boolean status;
    private String password;
    private String category;
    private String master;
    private Long cntUser;

    List<ChatRoomEnterUserResponseDto> chatRoomUserList;
    List<ChatRoomSayingResponseDto> chatRoomSayingList;


    public ChatRoomEnterUsersResponseDto(ChatRoom room,
                                         List<ChatRoomEnterUserResponseDto> chatRoomUserResponseDtos,
                                         List<ChatRoomSayingResponseDto> chatRoomSayingResponseDtos) {

        this.sessionId = room.getSessionId();
        this.title = room.getTitle();
        this.subtitle = room.getSubtitle();
        this.status = room.isStatus();
        this.password = room.getPassword();
        this.category = room.getCategory().getCategory().getCategoryKr();
        this.master = room.getMaster();

        this.cntUser = room.getCntUser();
        this.chatRoomUserList = chatRoomUserResponseDtos;
        this.chatRoomSayingList = chatRoomSayingResponseDtos;
    }


}
