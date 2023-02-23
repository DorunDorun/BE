package shop.dodotalk.dorundorun.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;


@Getter
public class ExceptionResponseMessage {

    private Integer statusCode;
    private String statusMsg;

    public ExceptionResponseMessage(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
