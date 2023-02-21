package shop.dodotalk.dorundorun.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@RequiredArgsConstructor
@Getter
public class ChatRoomEnterDataRequestDto {

    @Null
    private String password;

    @NotNull(message = "nickname을 설정해 주세요.")
    private String nickname;



}
