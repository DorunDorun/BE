package shop.dodotalk.dorundorun.chatroom.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrivateResponseBody {
    private String statusCode;
    private String statusMsg;
    private Object data;

    public PrivateResponseBody(SuccessCode successCode) {
        this.statusCode = successCode.getErrorCode();
        this.statusMsg = successCode.getErrormessage();

    }

    public PrivateResponseBody(SuccessCode successCode, Object data){
        this.statusCode = successCode.getErrorCode();
        this.statusMsg = successCode.getErrormessage();
        this.data = data;
    }


}