package shop.dodotalk.dorundorun.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionResponseListMessage {
    private Integer httpStatus;
    private List<String> messages;

    public ExceptionResponseListMessage(Integer httpStatus, List<String> messages) {
        this.httpStatus = httpStatus;
        this.messages = messages;
    }
}
