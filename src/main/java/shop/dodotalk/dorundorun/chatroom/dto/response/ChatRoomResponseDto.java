package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import shop.dodotalk.dorundorun.chatroom.entity.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ChatRoomResponseDto {


    private String sessionId;
    private String title;
    private String subtitle;
    private boolean status;
    private ButtonImageEnum buttonImage;
    private String password;
    private String category;
    private String master;
    private String saying;
    private List<RoomUsers> roomUsers;
    private Long cntUser;

    public void setCategory(Category category) {
        this.category = category.getCategory().getCategoryKr();
    }

    public ChatRoomResponseDto(Room room) {

        this.sessionId = room.getSessionId();
        this.title = room.getTitle();
        this.subtitle = room.getSubtitle();
        this.status = room.isStatus();
        this.buttonImage = room.getButtonImage();
        this.password = room.getPassword();
        this.category = room.getCategory().getCategory().getCategoryKr();
        this.master = room.getMaster();
        this.saying = room.getSaying();
        this.roomUsers = room.getRoomUsers();
        this.cntUser = room.getCntUser();
    }
}
