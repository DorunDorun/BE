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

        log.info("emmiters2 사이즈 : " + emitters.size());

        emitter.onCompletion(() -> {
            log.info("SSE onCompletion2");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });

        emitter.onTimeout(() -> {
            log.info("SSE onTimeout2");
            emitter.complete();
        });

        emitter.onError(throwable -> emitter.complete());

        return emitter;
    }

    public void remove(SseEmitter emitter) {
        this.emitters.remove(emitter);
    }

    public void count() throws InterruptedException {
        Thread.sleep(1000L);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDeleteAndCntUserAfter(false, 0L);

        SseResposneDto sseResposneDto = new SseResposneDto(Long.valueOf(chatRooms.size()));

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(sseResposneDto));
                //emitter.complete();
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
