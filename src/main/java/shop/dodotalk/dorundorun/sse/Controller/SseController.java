package shop.dodotalk.dorundorun.sse.Controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import shop.dodotalk.dorundorun.sse.Entity.SseEmitters;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class SseController {
    private final SseEmitters sseEmitters;
    @GetMapping("/ssehtml")
    public String ssehtml() {
        return "ssechatroom";
    }

    @ResponseBody
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
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
