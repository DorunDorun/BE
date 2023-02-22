package shop.dodotalk.dorundorun.error;

import io.openvidu.java.client.OpenViduHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // custom 예외처리
    @ExceptionHandler
    public ResponseEntity<?> handleException(CustomErrorException customErrorException){
        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(customErrorException.getErrormessage());
    }

//    // ExceptionHandler
//    @ExceptionHandler
//    protected ResponseEntity<?> handleException(Exception ex) {
//        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
//    }
//
//    // IllegalArgumentException 예외처리
//    @ExceptionHandler
//    public ResponseEntity<?> handleException(IllegalArgumentException ex){
//        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
//    }


    @ExceptionHandler
    public ResponseEntity<ExceptionResponseMessage> handleException(NullPointerException ex){

        ExceptionResponseMessage message =
                new ExceptionResponseMessage(BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.valueOf(message.getHttpStatus()));

    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponseListMessage> handleException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();

        List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());


        ExceptionResponseListMessage message = new ExceptionResponseListMessage(BAD_REQUEST.value(), errors);



        return new ResponseEntity<>(message, HttpStatus.valueOf(message.getHttpStatus()));

    }


//    @ExceptionHandler
//    public ResponseEntity<?> handleException(RuntimeException ex){
//        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<?> handleException2(HttpRequestMethodNotSupportedException ex){
//        return ResponseEntity.badRequest().header("Content-Type","application/json; charset=UTF-8").body(ex.getMessage());
//    }
}
