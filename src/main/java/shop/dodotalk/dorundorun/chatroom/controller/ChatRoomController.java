package shop.dodotalk.dorundorun.chatroom.controller;


import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodotalk.dorundorun.chatroom.dto.request.CreateRoomRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.RoomPasswordRequestDto;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;
import shop.dodotalk.dorundorun.chatroom.service.ChatRoomService;
import shop.dodotalk.dorundorun.chatroom.util.ResponseUtil;
import shop.dodotalk.dorundorun.security.annotation.Authenticated;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 방 생성
    @PostMapping("/create/room")
    public ResponseEntity<PrivateResponseBody> makeRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto,
                                                        HttpServletRequest request,
                                                        @Authenticated OAuth2UserInfoAuthentication authentication)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService
                .createRoom(createRoomRequestDto, request, user));

    }

    //전체 방 조회 페이지처리
    @GetMapping("/rooms/{page}")
    public ResponseEntity<PrivateResponseBody> getAllRooms(@PathVariable int page) {
        return new ResponseUtil<>().forSuccess(chatRoomService.getAllRooms(page));
    }


    // 방 접속
    @PostMapping("/rooms/{sessionid}")
    public ResponseEntity<PrivateResponseBody> enterRoom(@PathVariable(name = "sessionid") String sessionId,
                                                         HttpServletRequest request,
                                                         @RequestBody(required = false) RoomPasswordRequestDto password,
                                                         @Authenticated OAuth2UserInfoAuthentication authentication)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.getRoomData(sessionId, request, password, user));
    }


    // 방 나가기 -> 모든인원 나가면 방삭제
    @PostMapping("/rooms/{sessionid}/users")
    public ResponseEntity<PrivateResponseBody> outRoomUser(@PathVariable(name = "sessionid") String sessionId, HttpServletRequest request,
                                                           @Authenticated OAuth2UserInfoAuthentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.outRoomUser(sessionId, request, user));
    }


//    // 키워드로 방 검색
//    @GetMapping("/rooms/search/{page}")
//    public ResponseEntity<?> searchRoom(@PathVariable int page, @RequestParam String keyword) {
//        return new ResponseUtil<>().forSuccess(chatRoomService.searchRoom(keyword, page));
//    }

}
