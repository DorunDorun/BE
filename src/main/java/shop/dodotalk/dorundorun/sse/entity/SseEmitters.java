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
import java.util.concurrent.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitters {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    //private final ConcurrentLinkedQueue<SseEmitter> emitters = new ConcurrentLinkedQueue<>();

    //private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final ChatRoomRepository chatRoomRepository;

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);

        log.info("emmiters 사이즈 : " + emitters.size());

        emitter.onCompletion(() -> {
            log.info("SSE onCompletion2");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });

        emitter.onTimeout(() -> {
            log.info("SSE onTimeout2");
            emitter.complete();
        });

        emitter.onError(throwable -> emitter.complete()); // 트라이 캐치 코치

//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(() -> {
//            try {
//                log.info("SSE 하트비트 전송");
//                log.info("SSE 리스트 크기 : " + emitters.size());
//                emitter.send("");
//            } catch (IOException e) {
//                // SSE 연결이 끊어진 경우
//                emitter.complete();
//                executor.shutdown();
//                this.emitters.remove(emitter);
//            }
//        }, 0, 30, TimeUnit.SECONDS);

        return emitter;
    }

    public void remove(SseEmitter emitter) {
        this.emitters.remove(emitter);
    }

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
                //emitter.complete();
            } catch (IOException e) {
                log.info("SSE 익셉션 발생");
                emitter.complete();
                this.emitters.remove(emitter);
            }
        });
    }
}
