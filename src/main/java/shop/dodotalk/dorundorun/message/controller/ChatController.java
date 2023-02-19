package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
import shop.dodotalk.dorundorun.message.service.ChatMessageService;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    @ResponseBody
    @MessageMapping("/chat/room")
    public void message(ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.ChatMessageCreate(chatMessageRequestDto);

        System.out.println("--------------------controller------------------------");
        System.out.println(chatMessageResponseDto.getMessageId());
        System.out.println(chatMessageResponseDto.getMessage());
        System.out.println("--------------------controller------------------------");

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageRequestDto.getSessionId(), chatMessageResponseDto);
    }
}
