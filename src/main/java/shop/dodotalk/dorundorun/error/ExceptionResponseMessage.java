package shop.dodotalk.dorundorun.error;

import lombok.Getter;

@Getter
public class ExceptionResponseMessage {

    private Integer statusCode;
    private String statusMsg;

    public ExceptionResponseMessage(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
