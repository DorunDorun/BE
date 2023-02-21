package shop.dodotalk.dorundorun.sse.Entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitters {
    private static final AtomicLong counter = new AtomicLong();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final ChatRoomRepository chatRoomRepository;

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        System.out.println("emitter : " + emitter);
        System.out.println("emitter list size: " + emitters.size());
        emitter.onCompletion(() -> {
            System.out.println("만료됨");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            System.out.println("타임아웃");
            emitter.complete();
        });

        return emitter;
    }

    public void count() {
        long count = counter.incrementAndGet();
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("count")
                        .data(chatRooms.size()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
