package shop.dodotalk.dorundorun.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CustomErrorException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorCode;
    private String errormessage;
    public CustomErrorException(HttpStatus httpStatus, String errorCode, String errormessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errormessage = errormessage;
    }
    @Override
    public String toString() {
        return "HttpStatus : " + this.httpStatus + "<br>"
                + "errorCode : " + this.errorCode + "<br>"
                + "errormessage : " + this.errormessage;
    }
}
