package shop.dodotalk.dorundorun.chatroom.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.chatroom.error.ErrorCode;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;

@RequiredArgsConstructor
@Component
public class ResponseUtil<T> {


    public ResponseEntity<PrivateResponseBody> forSuccess(T data){
        return new ResponseEntity<>(
                new PrivateResponseBody(
                        new ErrorCode(HttpStatus.OK, "200", "정상") , data
                ) ,
                HttpStatus.OK);
    }

}
