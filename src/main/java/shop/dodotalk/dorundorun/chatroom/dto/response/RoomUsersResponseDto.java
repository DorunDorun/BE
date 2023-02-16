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

    // 멤버
    private Long User;

    private String nickname;

    private String email;

    private String ProfileImage;

    // 방장인지 확인
    private boolean roomMaster;

    private String enterRoomToken;

    public RoomUsersResponseDto(RoomUsers entity, boolean a){
        this.roomUserId = entity.getRoomUserId();
        this.sessionId = entity.getSessionId();
        this.User = entity.getUser();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.ProfileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();

        // todo ???이거모지 이거 때매 일반유저도 룸마스터 true라고 나오나 ?
        this.roomMaster = a;
    }

    public RoomUsersResponseDto(RoomUsers entity) {
        this.roomUserId = entity.getRoomUserId();
        this.sessionId = entity.getSessionId();
        this.User = entity.getUser();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.ProfileImage = entity.getProfileImage();
        this.enterRoomToken = entity.getEnterRoomToken();
        this.roomMaster = false;
    }
}
