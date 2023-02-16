package shop.dodotalk.dorundorun.chatroom.dto.response;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.Room;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CreateRoomResponseDto {

    // 실제 방 session id
    private String sessionId;
    private String title;
    private String subtitle;
    private boolean status;
    private String  category;
    private String password;
    private String master;
    private String saying;
    private String token;

    // 방 생성 시간
    private LocalDateTime createdAt;
    // 방 수정 시간
    private LocalDateTime modifiedAt;


}
