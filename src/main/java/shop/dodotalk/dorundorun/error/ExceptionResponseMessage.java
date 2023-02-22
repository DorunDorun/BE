package shop.dodotalk.dorundorun.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;


@Getter
public class ExceptionResponseMessage {

    private Integer httpStatus;
    private String errormessage;

    public ExceptionResponseMessage(Integer httpStatus, String errormessage) {
        this.httpStatus = httpStatus;
        this.errormessage = errormessage;
    }
}
