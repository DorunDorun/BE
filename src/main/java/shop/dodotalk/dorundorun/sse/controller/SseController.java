package shop.dodotalk.dorundorun.sse.controller;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.sse.entity.SseEmitters;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SseController {
    private final SseEmitters sseEmitters;
    private final ChatRoomRepository chatRoomRepository;
    @GetMapping("/ssehtml")
    public String ssehtml() {
        return "ssechatroom";
    }

    @ResponseBody
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(30 * 1000L);
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);
        List<ChatRoom> chatRooms2 = chatRoomRepository.findAll();
        sseEmitters.add(emitter);
        try {
            System.out.println("연결됨");
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data(chatRooms.size()));
            emitter.send(SseEmitter.event()
                    .name("connect2")
                    .data(chatRooms2.size()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @ResponseBody
    @GetMapping("/count")
    public ResponseEntity<Void> count() {
        sseEmitters.count();
        return ResponseEntity.ok().build();
    }
}
