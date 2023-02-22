package shop.dodotalk.dorundorun.chatroom.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomCreateResponseDto {

    // 실제 방 session id
    private String sessionId;
    private String title;
    private String subtitle;
    private String buttonImage;
    private boolean status;
    private String  category;
    private String password;
    private String masterName;
    private boolean isRoomMaster;
    private String saying;


    // 방 생성 시간
    private LocalDateTime createdAt;
    // 방 수정 시간
    private LocalDateTime modifiedAt;


    public boolean getIsRoomMaster() {
        return isRoomMaster;
    }
}
