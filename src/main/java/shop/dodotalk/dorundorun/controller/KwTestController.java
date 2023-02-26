package shop.dodotalk.dorundorun.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KwTestController {
    @GetMapping("/")
    public ResponseEntity success() {
        System.out.println("여기가 실행되는지 학인핧껀뗴노ㅒ노냬냐");
        return ResponseEntity.ok().build();
    }
}
