package shop.dodotalk.dorundorun.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dodotalk.dorundorun.metrics.HttpRequestCounter;

@RequiredArgsConstructor
@RestController
public class KwTestController {
    private final HttpRequestCounter httpRequestCounter;
    @GetMapping("/")
    public ResponseEntity success() {
        System.out.println("여기가 실행되는지 학인핧껀뗴노ㅒ노냬냐");
        httpRequestCounter.increment();
        return ResponseEntity.ok().build();
    }
}
