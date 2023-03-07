package shop.dodotalk.dorundorun.sse.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.sse.dto.SseResposneDto;
import shop.dodotalk.dorundorun.sse.entity.SseEmitters;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SseController {
    private final SseEmitters sseEmitters;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping(value = "/ssehtml")
    public String ssehtml() {
        return "ssechatroom";
    }

    @ResponseBody
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);

        SseResposneDto sseResposneDto = new SseResposneDto(Long.valueOf(chatRooms.size()));

        sseEmitters.add(emitter);

        try {
            log.info("SSE Connect");
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data(sseResposneDto));
        } catch (IOException e) {
            log.info("SSE 연결 익셉션");
            emitter.complete();
            sseEmitters.remove(emitter);
        }

        return ResponseEntity.ok(emitter);
    }
    @ResponseBody
    @PostMapping("/count")
    public void send() throws InterruptedException {
        log.info("----실시간 채팅방 카운트----");
        sseEmitters.count();
    }
}
