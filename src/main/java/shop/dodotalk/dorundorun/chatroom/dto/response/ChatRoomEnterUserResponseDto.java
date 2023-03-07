package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

import java.sql.Time;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEnterUserResponseDto {

    // 채팅방
    private String sessionId;

    // 소셜 로그인 방법
    private String social;

    // 멤버
    private Long userId;

    private String nickname;

    private String email;

    private String ProfileImage;

    /*해당 방에 머물은 시간(재 접속 할경우 +해서 계산됨)*/
    private Time stayTime;

    /*해당 방에 머물은 Days -> stayTime이 24시간이 넘을시 1씩 추가됨*/
    private Long stayDay;

    private Long mediaBackImage;

    // 방장인지 확인
    private boolean roomMaster;

    // 방 접속시 모든 사용자의 정보를 보내기 때문에 현재 접속한 유저 구분용도
    private boolean nowUser;

    private String enterRoomToken;

    public ChatRoomEnterUserResponseDto(ChatRoomUser entity, boolean roomMaster, boolean nowUser){
        this.sessionId = entity.getSessionId();
        this.social = entity.getSocial();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.ProfileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();
        this.stayTime = entity.getRoomStayTime();
        this.stayDay = entity.getRoomStayDay();
        this.mediaBackImage = entity.getMediaBackImage();
        this.roomMaster = roomMaster;
        this.nowUser = nowUser;
    }

}
