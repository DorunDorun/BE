package shop.dodotalk.dorundorun.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class ChatRoomEnterDataRequestDto {


    private String password;

    @NotNull(message = "nickname을 설정해 주세요.")
    private String nickname;


    @NotNull(message = "mediaBackImage를 선택해 주세요.")
    private Long mediaBackImage;


    @AssertTrue
    public boolean isMediaBackImage() {
        try{


            if (mediaBackImage >= 1 && mediaBackImage <= 8) {
                return true;
            }else{
                return false;
            }
        } catch (Exception exception){
            throw new IllegalArgumentException("mediaBackImage 값을 정확하게 입력해 주세요.");
        }

    }
}

