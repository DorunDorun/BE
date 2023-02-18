package shop.dodotalk.dorundorun.message.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;
import shop.dodotalk.dorundorun.chatroom.util.ResponseUtil;

@Controller
public class ChatTestController {
    @GetMapping("/chatrooms")
    public String geadfadlRooms() {
        return "chatrooms";
    }

    @GetMapping("/chat/room/2")
    public String getAlsadasdlRooms() {
        return "chat";
    }
}
