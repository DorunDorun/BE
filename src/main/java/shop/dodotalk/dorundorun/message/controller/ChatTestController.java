package shop.dodotalk.dorundorun.message.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.dodotalk.dorundorun.error.CustomErrorException;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;
import shop.dodotalk.dorundorun.message.repository.RoomMessageRepository;

import java.util.Optional;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@ApiOperation(value="백엔드 메세지 테스트 코드")
public class ChatTestController {
    private final RoomMessageRepository roomMessageRepository;
    @GetMapping("/chatrooms")
    public String geadfadlRooms() {
        return "chatrooms";
    }

    @GetMapping("/chat/room/ses_Q6CoXlyYfA")
    public String getAlsadasdlRooms() {
        return "chat";
    }

    @GetMapping("/global/test")
    public void globaltest1() {
        throw new CustomErrorException(HttpStatus.OK, "200", "마지막 테스트");
    }

    @GetMapping("/global/test2")
    public void globaltest2() {
        throw new IllegalArgumentException();
    }

    @ResponseBody
    @GetMapping("/swaggertest/messageinfo")
    public ChatMessageResponseDto swaggerRoomMessage() {
        Optional<RoomMessage> roomMessage = roomMessageRepository.findById(1L);
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(roomMessage.get());
        return chatMessageResponseDto;
    }

    @ResponseBody
    @GetMapping("/swaggertest/messageinfo2")
    public RoomMessage swaggerRoomMessage2() {
        Optional<RoomMessage> roomMessage = roomMessageRepository.findById(1L);
        return roomMessage.get();
    }
}
