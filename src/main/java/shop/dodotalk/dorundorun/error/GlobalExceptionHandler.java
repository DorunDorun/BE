package shop.dodotalk.dorundorun.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // custom 예외처리
    @ExceptionHandler({CustomErrorException.class})
    public String handleException(CustomErrorException customErrorException){
        return customErrorException.toString();
    }
    // ExceptionHandler
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception ex) {
        System.out.println("익셉션오류????");
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    // IllegalArgumentException 예외처리
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> handleException(IllegalArgumentException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> handleException(NullPointerException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleException2(HttpRequestMethodNotSupportedException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }
}
