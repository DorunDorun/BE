package shop.dodotalk.dorundorun.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.ButtonImageEnum;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class ChatRoomCreateRequestDto {

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;                   // 방 제목

    @NotBlank(message = "부제목은 공백일 수 없습니다.")
    private String subtitle;                // 방 부제목 (내용)


    private CategoryEnum category;          // 카테고리


    private ButtonImageEnum buttonImage;    // 버튼 이미지(캐릭터) 색깔


    private boolean status;                 // 방 상태 (public / private)


    private String password;                // 방이 private으로 설정될 시 패스워드 입력
}
