package shop.dodotalk.dorundorun.chatroom.controller;


import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomCreateRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomEnterDataRequestDto;
import shop.dodotalk.dorundorun.chatroom.entity.CategoryEnum;
import shop.dodotalk.dorundorun.chatroom.error.PrivateResponseBody;
import shop.dodotalk.dorundorun.chatroom.service.ChatRoomService;
import shop.dodotalk.dorundorun.chatroom.util.ResponseUtil;
import shop.dodotalk.dorundorun.security.annotation.Authenticated;
import shop.dodotalk.dorundorun.security.jwt.OAuth2UserInfoAuthentication;
import shop.dodotalk.dorundorun.sse.entity.SseEmitters;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /*화상 채팅 방 API Controller*/

    /*
     * 방 생성 API : 방 만들기 API
     * 방 입장 API : 만들어진 방에 입장하는 API
     * 방 목록 API : page 별로 방 목록을 조회해온다. 1 page = 8 rooms
     * 방 나가기 API : 방을 나갈때 해당 하는 API이며, 해당 방의 모든 유저가 나갈 시 방이 삭제 된다.*/

    /*방 생성 API*/
    @PostMapping("/rooms")
    public ResponseEntity<PrivateResponseBody> makeRoom(
            @Validated @RequestBody ChatRoomCreateRequestDto chatRoomCreateRequestDto,
            @Authenticated OAuth2UserInfoAuthentication authentication)
            throws Exception {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forCreatedSuccess(chatRoomService
                .createChatRoom(chatRoomCreateRequestDto, user));
    }

    /*채팅방 입장 API*/
    @PostMapping("/rooms/{sessionid}")
    public ResponseEntity<PrivateResponseBody> enterRoom(@PathVariable(name = "sessionid") String sessionId,
                                                         HttpServletRequest request,
                                                         @Valid @RequestBody ChatRoomEnterDataRequestDto requestData,
                                                         @Authenticated OAuth2UserInfoAuthentication authentication)
            throws OpenViduJavaClientException, OpenViduHttpException {

        User user = (User) authentication.getPrincipal();
        return new ResponseUtil<>().forSuccess(chatRoomService.enterChatRoom(sessionId, request, requestData, user));
    }

    /*채팅방(속한 유저들) 정보 불러오기 API*/
    @GetMapping("/rooms/{sessionid}/users")
    public ResponseEntity<PrivateResponseBody> getAllRooms(@PathVariable(name = "sessionid") String sessionId,
                                                           @Authenticated OAuth2UserInfoAuthentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.getRoomUserData(sessionId, user));
    }


    /*방 목록 API*/
    @GetMapping("/rooms/{page}")
    public ResponseEntity<PrivateResponseBody> getAllRooms(@PathVariable int page) {
        return new ResponseUtil<>().forSuccess(chatRoomService.getAllChatRooms(page));
    }


    /*방 나가기 API*/
    @DeleteMapping("/rooms/{sessionid}")
    public ResponseEntity<PrivateResponseBody> outRoomUser(@PathVariable(name = "sessionid") String sessionId,
                                                           HttpServletRequest request,
                                                           @Authenticated OAuth2UserInfoAuthentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forDeletedSuccess(chatRoomService.outRoomUser(sessionId, request, user));
    }

    /*랜딩 페이지에 보여줄정보
     * 1.지금까지 생성된 채팅방 개수 합산.
     * 2.방이 생성되고 삭제되기 전까지의 시간 총 합(유저 머문 시간 x)*/
    @GetMapping("/rooms/info")
    public ResponseEntity<PrivateResponseBody> getRoomInfo() {

        return new ResponseUtil<>().forSuccess(chatRoomService.getRoomInfo());
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
    public ResponseEntity<PrivateResponseBody> searchRoom(@PathVariable int page,
                                                          @RequestParam String keyword) {

        return new ResponseUtil<>().forSuccess(chatRoomService.searchRoom(keyword, page));
    }

    /*카테고리 클릭 해당 카테고리의 채팅방 불러오기 API*/
    @GetMapping("/rooms/{page}/category")
    public ResponseEntity<PrivateResponseBody> searchCategory(@PathVariable int page,
                                                              @RequestParam String category) {

        CategoryEnum categoryEnum;
        try{
            categoryEnum = CategoryEnum.valueOf(category);
        }catch (Exception exception){
            throw new IllegalArgumentException("category 값을 정확하게 입력해 주세요.");
        }



        return new ResponseUtil<>().forSuccess(chatRoomService.searchCategory(categoryEnum, page));
    }

    /*자신이 참여 했던 방 리스트 보여주기. 참여 히스토리.*/
    @GetMapping("/rooms/{page}/history")
    public ResponseEntity<PrivateResponseBody> getAllHistoryRooms(@PathVariable int page,
                                                                  @Authenticated OAuth2UserInfoAuthentication authentication) {

        User user = (User) authentication.getPrincipal();

        return new ResponseUtil<>().forSuccess(chatRoomService.getAllHistoryChatRooms(page, user));
    }
}
