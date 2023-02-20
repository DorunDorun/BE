package shop.dodotalk.dorundorun.chatroom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEnterUserResponseDto {

//    private Long roomUserId;

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

    // 방 접속시 모든 사용자의 정보를 보내기 때문에 현재 접속한 유저 구분용도
    private boolean nowUser;

    private String enterRoomToken;

    public ChatRoomEnterUserResponseDto(ChatRoomUser entity, boolean roomMaster, boolean nowUser){
//        this.roomUserId = entity.getRoomUserId();
        this.sessionId = entity.getSessionId();
        this.social = entity.getSocial();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.ProfileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();
        this.roomMaster = roomMaster;
        this.nowUser = nowUser;
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
