package shop.dodotalk.dorundorun.chatroom.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import shop.dodotalk.dorundorun.chatroom.error.SuccessCode;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;

@RequiredArgsConstructor
@Component
public class ResponseUtil<T> {


    public ResponseEntity<PrivateResponseBody> forSuccess(T data){
        return new ResponseEntity<>(
                new PrivateResponseBody(
                        new SuccessCode(HttpStatus.OK, "200", "정상") , data
                ) ,
                HttpStatus.OK);
    }


    public ResponseEntity<PrivateResponseBody> forCreatedSuccess(T data){
        return new ResponseEntity<>(
                new PrivateResponseBody(
                        new SuccessCode(HttpStatus.OK, "201", "방 생성 완료.") , data
                ) ,
                HttpStatus.OK);
    }

    public ResponseEntity<PrivateResponseBody> forDeletedSuccess(T data){
        return new ResponseEntity<>(
                new PrivateResponseBody(
                        new SuccessCode(HttpStatus.OK, "204", "방 삭제 완료.") , data
                ) ,
                HttpStatus.OK);
    }

}
