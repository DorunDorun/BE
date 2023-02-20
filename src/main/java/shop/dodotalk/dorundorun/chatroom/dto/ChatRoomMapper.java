package shop.dodotalk.dorundorun.chatroom.dto;


import org.mapstruct.Mapper;
import shop.dodotalk.dorundorun.chatroom.dto.response.ChatRoomResponseDto;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;

import java.util.List;

@Mapper(componentModel = "spring") //스프링에서 사용하기 위해 인터페이스 위에 설정
public interface ChatRoomMapper {

    List<ChatRoomResponseDto> roomsToRoomResponseDtos(List<ChatRoom> rooms); //메소드 이름은 자유롭게 변경 가능


}