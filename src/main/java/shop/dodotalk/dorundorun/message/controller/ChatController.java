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
    @MessageMapping("/chats") // socket 통신은 request를 안주나??? // 혹시 @Transactional 줘야하나???
    public void message(ChatMessageRequestDto chatMessageRequestDto) {
        chatMessageService.BinaryImageChange(chatMessageRequestDto);
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessageRequestDto);
        messagingTemplate.convertAndSend("/sub/chat/room" + chatMessageRequestDto.getChatRoomId(), chatMessageResponseDto);
    }
}
