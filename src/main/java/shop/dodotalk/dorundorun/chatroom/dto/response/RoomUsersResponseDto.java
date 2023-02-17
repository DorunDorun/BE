package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomUsersResponseDto {

    private Long roomUserId;

    // 채팅방
    private String sessionId;

    // 소셜 로그인 방법
    private String social;

    // 멤버
    private Long userId;

    private String nickname;

    private String email;

    private String ProfileImage;

    // 방장인지 확인
    private boolean roomMaster;

    private String enterRoomToken;

    public RoomUsersResponseDto(RoomUsers entity, boolean roomMaster){
        this.roomUserId = entity.getRoomUserId();
        this.sessionId = entity.getSessionId();
        this.social = entity.getSocial();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.ProfileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();
        this.roomMaster = roomMaster;
    }

//    public RoomUsersResponseDto(RoomUsers entity) {
//        this.roomUserId = entity.getRoomUserId();
//        this.sessionId = entity.getSessionId();
//        this.userId = entity.getUserId();
//        this.nickname = entity.getNickname();
//        this.email = entity.getEmail();
//        this.ProfileImage = entity.getProfileImage();
//        this.enterRoomToken = entity.getEnterRoomToken();
//        this.roomMaster = false;
//    }
}
