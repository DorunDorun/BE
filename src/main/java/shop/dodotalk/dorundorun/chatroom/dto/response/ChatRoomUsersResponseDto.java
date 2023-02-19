package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.ButtonImageEnum;
import shop.dodotalk.dorundorun.chatroom.entity.Room;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomUsersResponseDto {

    private String sessionId;
    private String title;
    private String subtitle;
    private boolean status;
    private ButtonImageEnum buttonImage;
    private String password;
    private String category;
    private String master;
    private String saying;
    private Long cntUser;

    List<RoomUsersResponseDto> roomUsersResponseDtos;


    public ChatRoomUsersResponseDto(Room room, List<RoomUsersResponseDto> roomUsersResponseDtos) {

        this.sessionId = room.getSessionId();
        this.title = room.getTitle();
        this.subtitle = room.getSubtitle();
        this.status = room.isStatus();
        this.buttonImage = room.getButtonImage();
        this.password = room.getPassword();
        this.category = room.getCategory().getCategory().getCategoryKr();
        this.master = room.getMaster();
        this.saying = room.getSaying();
        this.cntUser = room.getCntUser();
        this.roomUsersResponseDtos = roomUsersResponseDtos;
    }
}
