package shop.dodotalk.dorundorun.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionResponseListMessage {
    private Integer statusCode;
    private List<String> statusMsg;

    public ExceptionResponseListMessage(Integer statusCode, List<String> statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }
}
