package shop.dodotalk.dorundorun.chatroom.controller;


import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomCreateRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomPasswordRequestDto;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;
import shop.dodotalk.dorundorun.chatroom.service.ChatRoomService;
import shop.dodotalk.dorundorun.chatroom.util.ResponseUtil;
import shop.dodotalk.dorundorun.security.annotation.Authenticated;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;
import shop.dodotalk.dorundorun.sse.Entity.SseEmitters;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final SseEmitters sseEmitters; // 관우 실시간 방 개수 나타내기

    /*화상 채팅 방 API Controller*/

    /*
    * 방 생성 API : 방이 만들어지며, 방을 만든 사람은 즉시 접속 된다.
    * 방 입장 API : 만들어진 방에 입장하는 API --> 방장 X
    * 방 목록 API : page 별로 방 목록을 조회해온다. 1 page = 8 rooms
    * 방 나가기 API : 방을 나갈때 해당 하는 API이며, 해당 방의 모든 유저가 나갈 시 방이 삭제 된다.*/

    /*방 생성 API*/
    @PostMapping("/rooms")
    public ResponseEntity<PrivateResponseBody> makeRoom(@RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto,
                                                        HttpServletRequest request,
                                                        @Authenticated OAuth2UserInfoAuthentication authentication)
            throws OpenViduJavaClientException, OpenViduHttpException {


        User user = (User) authentication.getPrincipal();

        sseEmitters.count(); // 관우 실시간 방 개수 나타내기

        return new ResponseUtil<>().forSuccess(chatRoomService
                .createRoom(chatRoomCreateRequestDto, request, user));

    }

    /*방 입장 API
    * 새로고침 시 reload=true,
    * 일반 입장 시 reload=false*/
    @PostMapping("/rooms/{sessionid}")
    public ResponseEntity<PrivateResponseBody> enterRoom(@PathVariable(name = "sessionid") String sessionId,
                                                         HttpServletRequest request,
                                                         @RequestBody(required = false) ChatRoomPasswordRequestDto password,
                                                         @RequestParam Boolean reload,
                                                         @Authenticated OAuth2UserInfoAuthentication authentication)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.getRoomData(sessionId, request, password, reload, user));
    }

    /*방 목록 API*/
    @GetMapping("/rooms/{page}")
    public ResponseEntity<PrivateResponseBody> getAllRooms(@PathVariable int page) {
        return new ResponseUtil<>().forSuccess(chatRoomService.getAllRooms(page));
    }



    /*방 나가기 API*/
    @DeleteMapping("/rooms/{sessionid}")
    public ResponseEntity<PrivateResponseBody> outRoomUser(@PathVariable(name = "sessionid") String sessionId,
                                                           HttpServletRequest request,
                                                           @Authenticated OAuth2UserInfoAuthentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.outRoomUser(sessionId, request, user));
    }


    /*
    * 2차 Scope
    * 방 검색 API
    * - SearchTypeEnum
    *   - 카테고리 검색 --> 아직 미구현 Repository에서 카테고리 타입을 넘겨야함.
    *   - 글 제목 검색
    *   - 글 내용 검색
    */

    /*방 검색 API(키워드)*/
    @GetMapping("/rooms/{page}/search")
    public ResponseEntity<?> searchRoom(@PathVariable int page,
                                        @RequestParam String keyword) {

        return new ResponseUtil<>().forSuccess(chatRoomService.searchRoom(keyword, page));
    }

}
