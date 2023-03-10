package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
import shop.dodotalk.dorundorun.message.service.ChatMessageService;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    @ResponseBody
    //@CrossOrigin(origins = "https://dorundorun.shop")
    @MessageMapping("/chat/room")
    public void message(ChatMessageRequestDto chatMessageRequestDto) {

        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.ChatMessageCreate(chatMessageRequestDto);

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageResponseDto);
    }
}
