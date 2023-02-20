package shop.dodotalk.dorundorun.message.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;
import shop.dodotalk.dorundorun.chatroom.util.ResponseUtil;
import shop.dodotalk.dorundorun.error.CustomErrorException;

@Controller
@RequestMapping("/api")
public class ChatTestController {
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
}
