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
    //private static final AtomicLong counter = new AtomicLong();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final ChatRoomRepository chatRoomRepository;

    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            emitter.complete();
        });

        return emitter;
    }

    public void count() {
        //long count = counter.incrementAndGet();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByIsDelete(false);
        System.out.println("------------------채팅방 생성 or 삭제----------------------");
        System.out.println("chatRooms.size() : " + chatRooms.size());
        System.out.println("------------------채팅방 생성 or 삭제----------------------");
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
