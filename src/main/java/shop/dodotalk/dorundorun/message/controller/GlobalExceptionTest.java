package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dodotalk.dorundorun.error.CustomErrorException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GlobalExceptionTest {
    @GetMapping("/nulltest")
    public String nulltest() {
        throw new IllegalArgumentException("abcdef...");
    }

    @GetMapping("/test")
    public String test() {
        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "200", "검색 결과가 없습니다.");
    }

    @GetMapping("/test2")
    public String test2() {
        throw new RuntimeException("런타임.");
    }

    @GetMapping("/test3")
    public String test3() throws Exception{
        throw new Exception("익셉션.");
    }
}
