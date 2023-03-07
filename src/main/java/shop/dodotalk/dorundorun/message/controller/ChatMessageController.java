package shop.dodotalk.dorundorun.message.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.dodotalk.dorundorun.message.dto.ChatFileDeleteRequestDto;
import shop.dodotalk.dorundorun.message.dto.ChatFileDeleteResponseDto;
import shop.dodotalk.dorundorun.message.dto.ChatMsgDeleteRequestDto;
import shop.dodotalk.dorundorun.message.dto.ChatMsgDeleteResponseDto;
import shop.dodotalk.dorundorun.message.service.ChatMessageService;
import shop.dodotalk.dorundorun.security.annotation.Authenticated;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;
import shop.dodotalk.dorundorun.users.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @DeleteMapping("/chat/room/msg")
    public ChatMsgDeleteResponseDto chatMsgDelete(@RequestBody ChatMsgDeleteRequestDto chatMsgDeleteRequestDto,
                                                  @Authenticated OAuth2UserInfoAuthentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatMsgDeleteResponseDto chatMsgDeleteResponseDto = chatMessageService.ChatMessageDelete(chatMsgDeleteRequestDto, user);

        return chatMsgDeleteResponseDto;

    }

    @DeleteMapping("/chat/room/file")
    public ChatFileDeleteResponseDto chatFileDelete(@RequestBody ChatFileDeleteRequestDto chatFileDeleteRequestDto,
                                                    @Authenticated OAuth2UserInfoAuthentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatFileDeleteResponseDto chatFileDeleteResponseDto = chatMessageService.ChatFileDelete(chatFileDeleteRequestDto, user);

        return chatFileDeleteResponseDto;
    }
}
