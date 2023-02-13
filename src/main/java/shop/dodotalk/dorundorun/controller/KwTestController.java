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
        return ResponseEntity.ok().build();
    }
}