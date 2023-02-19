package shop.dodotalk.dorundorun.message.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("/chat/room/msg")
    public ChatMsgDeleteResponseDto chatMsgDelete(@RequestBody ChatMsgDeleteRequestDto chatMsgDeleteRequestDto) {
                                                  //@Authenticated OAuth2UserInfoAuthentication authentication) {
        //User user = (User) authentication.getPrincipal();
        System.out.println("오고 있긴 한거지??");
        ChatMsgDeleteResponseDto chatMsgDeleteResponseDto = chatMessageService.ChatMessageDelete(chatMsgDeleteRequestDto); // user

        return chatMsgDeleteResponseDto;

    }

    @PostMapping("/chat/room/file")
    public ChatFileDeleteResponseDto chatFileDelete(@RequestBody ChatFileDeleteRequestDto chatFileDeleteRequestDto,
                                                    @Authenticated OAuth2UserInfoAuthentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatFileDeleteResponseDto chatFileDeleteResponseDto = chatMessageService.ChatFileDelete(chatFileDeleteRequestDto, user);

        return chatFileDeleteResponseDto;
    }
}
