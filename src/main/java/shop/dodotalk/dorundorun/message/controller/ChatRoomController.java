package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller // @ResponseBody 필요할때 쓰기!
@RequiredArgsConstructor
public class ChatRoomController {
    @RequestMapping("/chat/room/{roomId}")
    public ModelAndView chat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat");
        return mv;
    }

    @RequestMapping("/chat/rooms")
    public ModelAndView chatRooms() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chatrooms");
        return mv;
    }
}
