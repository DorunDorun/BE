package shop.dodotalk.dorundorun.chatroom.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;

@Getter
@RequiredArgsConstructor
public class CreateRoomRequestDto {

    private String title;   // 방 제목
    private String subtitle; // 방 부제목 (내용)
    private CategoryEnum category;  // 카테고리
    private boolean status;      // 방 상태 (public / private)
    private String password;    // 방이 private으로 설정될 시 패스워드 입력
}
