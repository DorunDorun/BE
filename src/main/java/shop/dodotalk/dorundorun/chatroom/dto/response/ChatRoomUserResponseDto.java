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
public class ChatRoomUserResponseDto {

//    private Long roomUserId;

    // 채팅방
    private String sessionId;

    // 소셜 로그인 방법
    private String social;

    // 멤버
    private Long userId;

    private String nickname;

    private String email;

    private String profileImage;

    private String enterRoomToken;

    public ChatRoomUserResponseDto(ChatRoomUser entity){
//        this.roomUserId = entity.getRoomUserId();
        this.sessionId = entity.getSessionId();
        this.social = entity.getSocial();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.profileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();

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
