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

        return emitter;
    }

    public void count() {

        if (emitters.size() == 0) {
            log.info("--------emitters.size = 0");
            return;
        }


        log.info("--------에미터 있음");

        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);

        SseResposneDto sseResposneDto = new SseResposneDto(Long.valueOf(chatRooms.size()));

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
