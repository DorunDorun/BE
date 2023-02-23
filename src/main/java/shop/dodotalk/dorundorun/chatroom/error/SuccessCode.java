package shop.dodotalk.dorundorun.chatroom.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessCode {

    private HttpStatus httpStatus;
    private String errorCode;
    private String errormessage;

    public SuccessCode(HttpStatus httpStatus, String errorCode, String errormessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errormessage = errormessage;
    }
}
