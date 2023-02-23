package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodotalk.dorundorun.chatroom.entity.*;

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

    private List<ChatRoomUserResponseDto> chatRoomUserList;
    private Long cntUser;

    public void setCategory(Category category) {
        this.category = category.getCategory().getCategoryKr();
    }

}
