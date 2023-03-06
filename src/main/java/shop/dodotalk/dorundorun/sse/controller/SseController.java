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
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

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

        log.info("emitters1 : " + emitters.size());

        try {
            log.info("SSE Connect");
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data(sseResposneDto));
            emitter.complete();
        } catch (IOException e) {
            log.info("SSE 연결 익셉션");
            emitter.complete();
            sseEmitters.remove(emitter);
        }

        return ResponseEntity.ok(emitter);
    }

    @GetMapping("/sse/count")
    public void count() {

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);

        SseResposneDto sseResposneDto = new SseResposneDto(Long.valueOf(chatRooms.size()));

        emitters.forEach(emitter -> {
            log.info("------emitter 리스트 시작------ ");
            log.info("emitter size : " + emitters.size());
            try {
                log.info("------------- try 시작 ----------------");
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(sseResposneDto));
                log.info("------------- try 끝 ----------------");
                emitter.complete();
            } catch (IOException e) {
                log.info("SSE 아이오 익셉션 발생");
                emitter.complete();
                this.emitters.remove(emitter);
            } catch (IllegalStateException e) {
                log.info("SSE 일리걸 익셉션 발생");
                //emitter.complete();
                this.emitters.remove(emitter);
            }
        });
    }
}
