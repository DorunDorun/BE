package shop.dodotalk.dorundorun.sse.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.sse.dto.SseResposneDto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitters {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ChatRoomRepository chatRoomRepository;

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        emitter.onCompletion(() -> {
            log.info("SSE 만료됨");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            emitter.complete();
        });

//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            try {
//                log.info("SSE 하트비트 전송");
//                emitter.send("");
//            } catch (IOException e) {
//                // SSE 연결이 끊어진 경우
//                emitter.complete();
//                executor.shutdown();
//                this.emitters.remove(emitter);
//            }
//        }, 0, 5, TimeUnit.SECONDS);

        return emitter;
    }

    public void count() {

        log.info("emitter.size() = " + String.valueOf(emitters.size()));

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);

        SseResposneDto sseResposneDto = new SseResposneDto(Long.valueOf(chatRooms.size()));


        log.info("emitter.size() = " + String.valueOf(emitters.size()));

        if (emitters.size() > 0) {
            log.info("emitter.size() = " + String.valueOf(emitters.size()));
            emitters.forEach(emitter -> {
                try {
                    log.info("--------try 시작");
                    emitter.send(SseEmitter.event()
                            .name("count")
                            .data(sseResposneDto));
                    log.info("--------try 끝");

                } catch (IOException e) {
                    log.info("--------익셉션 발생");
                    throw new RuntimeException(e);
                }
            });
        }

    }
}
