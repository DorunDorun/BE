package shop.dodotalk.dorundorun.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KwTestController {
    @GetMapping("/")
    public String success() {
        return "https 도메인 테스트";
    }
}
