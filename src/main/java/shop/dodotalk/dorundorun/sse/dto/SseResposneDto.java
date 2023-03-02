package shop.dodotalk.dorundorun.sse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SseResposneDto {
    private Long chatRoomCount;

    public SseResposneDto(Long chatRoomCount) {
        this.chatRoomCount = chatRoomCount;
    }
}
