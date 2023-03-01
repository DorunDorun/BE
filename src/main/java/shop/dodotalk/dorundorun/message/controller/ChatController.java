package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
import shop.dodotalk.dorundorun.message.service.ChatMessageService;



@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    @ResponseBody
    //@CrossOrigin(origins = "https://dorundorun.shop")
    @MessageMapping("/chat/room")
    public void message(ChatMessageRequestDto chatMessageRequestDto) {
        log.info("방 ID : " + chatMessageRequestDto.getSessionId());
        log.info("메세지 바이트 코드 : " + chatMessageRequestDto.getImgByteCode());
        log.info("방 메세지 : " + chatMessageRequestDto.getMessage());

        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.ChatMessageCreate(chatMessageRequestDto);

        ChannelTopic topic = new ChannelTopic(chatMessageResponseDto.getSessionId());
        redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);

        redisPublisher.publish(topic, chatMessage);

//        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageRequestDto.getSessionId(), chatMessageResponseDto);
    }
}
