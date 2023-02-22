package shop.dodotalk.dorundorun.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class CustomErrorException extends RuntimeException {
    private HttpStatus httpStatus;
    private Integer errorCode;
    private String errormessage;
    public CustomErrorException(HttpStatus httpStatus, Integer errorCode, String errormessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errormessage = errormessage;
    }
}
