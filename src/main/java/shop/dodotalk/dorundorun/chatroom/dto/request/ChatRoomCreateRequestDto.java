package shop.dodotalk.dorundorun.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import shop.dodotalk.dorundorun.chatroom.entity.ButtonImageEnum;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class ChatRoomCreateRequestDto {

    @NotBlank(message = "제목을 입력해 주세요!")
    private String title;                   // 방 제목

    @NotBlank(message = "부제목을 입력해 주세요!")
    private String subtitle;                // 방 부제목 (내용)


    @NotBlank(message = "카테고리를 선택해 주세요!")
    private String category;          // 카테고리



    @NotBlank(message = "버튼 이미지를 선택해 주세요!")
    private String buttonImage;    // 버튼 이미지(캐릭터) 색깔


    @NotNull(message = "방의 상태를 설정해 주세요!")
    private Boolean status;                 // 방 상태 (public / private)


    private String password;                // 방이 private으로 설정될 시 패스워드 입력


    @AssertTrue
    public boolean isButtonImage() {

        try{
            ButtonImageEnum buttonImageEnum = ButtonImageEnum.valueOf(buttonImage);
        } catch (Exception exception){
            throw new IllegalArgumentException("button Image 값을 정확하게 입력해 주세요.");
        }
        return true;
    }


    @AssertTrue
    public boolean isCategory() {
        try{
            CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
        } catch (Exception exception){
            throw new IllegalArgumentException("category 값을 정확하게 입력해 주세요.");
        }
        return true;

    }



}
