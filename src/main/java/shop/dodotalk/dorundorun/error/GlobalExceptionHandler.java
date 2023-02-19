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
    @ExceptionHandler
    public String handleException(CustomErrorException customErrorException){
        return customErrorException.toString();
    }
    // ExceptionHandler
    @ExceptionHandler
    protected ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    // IllegalArgumentException 예외처리
    @ExceptionHandler
    public ResponseEntity<?> handleException(IllegalArgumentException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<?> handleException(NullPointerException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException2(HttpRequestMethodNotSupportedException ex){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
    }
}
