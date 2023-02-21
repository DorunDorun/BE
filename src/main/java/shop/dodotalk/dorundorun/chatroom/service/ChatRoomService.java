package shop.dodotalk.dorundorun.chatroom.service;


import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.chatroom.dto.ChatRoomMapper;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomCreateRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomEnterDataRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.response.*;
import shop.dodotalk.dorundorun.chatroom.entity.BenUser;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;
import shop.dodotalk.dorundorun.chatroom.error.ErrorCode;
import shop.dodotalk.dorundorun.chatroom.error.PrivateException;
import shop.dodotalk.dorundorun.chatroom.repository.BenUserRepository;
import shop.dodotalk.dorundorun.chatroom.repository.CategoryRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomUserRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.chatroom.util.CreateSaying;
import shop.dodotalk.dorundorun.security.jwt.JwtUtil;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final BenUserRepository benUserRepository;
    private final CreateSaying createSaying;
    private final CategoryRepository categoryRepository;
    private final ChatRoomMapper chatRoomMapper;

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }


    /* 방 생성 */
    @Transactional
    public ChatRoomCreateResponseDto createRoom(ChatRoomCreateRequestDto chatRoomCreateRequestDto,
                                                HttpServletRequest request, User user)
            throws OpenViduJavaClientException, OpenViduHttpException {

        /* Session Id, Token 셋팅 */
        ChatRoomCreateResponseDto newToken = createNewToken(user);


        /* 카테고리 확인 후 해당 카테고리에 맞는 랜덤 명언 제조 */
        String saying = createSaying.createSaying(chatRoomCreateRequestDto.getCategory());

        Optional<Category> optionalCategory = categoryRepository.findByCategory(chatRoomCreateRequestDto.getCategory());

        if (optionalCategory.isEmpty()) {
            throw new IllegalAccessError("해당 카테고리가 존재하지 않습니다.");
        }

        Category category = optionalCategory.get();


        // 채팅방 빌드
        ChatRoom chatRoom = ChatRoom.builder()
                .sessionId(newToken.getSessionId())
                .title(chatRoomCreateRequestDto.getTitle())
                .subtitle(chatRoomCreateRequestDto.getSubtitle())
                .master(user.getName())
                .masterUserId(user.getId())
                .buttonImage(chatRoomCreateRequestDto.getButtonImage())
                .status(chatRoomCreateRequestDto.isStatus())
                .category(category)
                .password(chatRoomCreateRequestDto.getPassword())
                .saying(saying)
                .build();


        /*빌드된 채팅방 저장(생성)*/
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);

        LocalDateTime createdAt = savedRoom.getCreatedAt();


        /*해당 방에 접속한 유저 정보 생성하기.*/
        // 방생성 할때 첫유저 (방장)
//        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
//                .sessionId(savedRoom.getSessionId())
//                .userId(user.getId())
//                .social(user.getProvider())
//                .nickname(user.getName())
//                .email(user.getEmail())
//                .profileImage(user.getProfile())
//                .enterRoomToken(savedRoom.getSessionId())
//                .roomEnterTime(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime())
//                .roomStayTime(Time.valueOf("00:00:00"))
//                .enterRoomToken(newToken.getToken())
//                .build();

        // 채팅방 인원 저장하기
//        chatRoomUserRepository.save(chatRoomUser);


//        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllBySessionId(savedRoom.getSessionId());

//        List<ChatRoomEnterUserResponseDto> chatRoomEnterUserResponseDtoList = new ArrayList<>();


//        boolean roomMaster;
//        boolean nowUser;

        // 채팅방 인원 추가
//        for (ChatRoomUser chatRoomUsersEle : chatRoomUserList) {
//
//            // 방장일 시
//            // 현재 접속한 유저일 시 본인이 누군지 알기 위해.
//            if (user != null && chatRoomUsersEle.getUserId().equals(user.getId())) {
//
//                roomMaster = true;
//                nowUser = true;
//            }
//            // 방장이 아닐 시
//            else {
//                roomMaster = false;
//                nowUser = false;
//            }
//
//
//            chatRoomEnterUserResponseDtoList.add(new ChatRoomEnterUserResponseDto(chatRoomUsersEle, roomMaster, nowUser));
//
//        }


//        Long currentUser = chatRoomUserRepository.countAllBySessionId(savedRoom.getSessionId());

        chatRoom.updateCntUser(0L);

        chatRoomRepository.save(chatRoom);


        /*

        채팅방에 보여질 정보들을 리턴*/
        return ChatRoomCreateResponseDto.builder()
                .sessionId(savedRoom.getSessionId())
                .title(savedRoom.getTitle())
                .subtitle(savedRoom.getSubtitle())
                .masterName(savedRoom.getMaster())
                .isRoomMaster(true)
                .status(savedRoom.isStatus())
                .token(newToken.getToken())
                .buttonImage(savedRoom.getButtonImage().name())
                .saying(saying)
                .category(savedRoom.getCategory().getCategory().getCategoryKr())
                .password(savedRoom.getPassword())
                .createdAt(createdAt)
                .modifiedAt(savedRoom.getModifiedAt())
                .build();


    }


    /*전체 방 조회하기*/
    @Transactional
    public ChatRoomAllResponseDto getAllRooms(int page) {

        /*페이지네이션 설정*/
        PageRequest pageable = PageRequest.of(page - 1, 12);
        Page<ChatRoom> roomList = chatRoomRepository.findByIsDeleteOrderByModifiedAtDesc(false, pageable);


        /*채팅방이 존재하지 않을 경우*/
        if (roomList.isEmpty()) {
            throw new PrivateException
                    (new ErrorCode(HttpStatus.BAD_REQUEST, "200", "채팅방이 존재하지 않습니다."));
        }


        /*pagination을 위한 정보를 담은 Dto 생성*/
        ChatRoomPageInfoResponseDto chatRoomPageInfoResponseDto
                = new ChatRoomPageInfoResponseDto(page, 12, (int) roomList.getTotalElements(), roomList.getTotalPages());

        /*room List 정보 Dto로 변환*/
        List<ChatRoom> rooms = roomList.getContent();



        List<ChatRoomResponseDto> chatRoomResponseDtoList = chatRoomMapper.roomsToRoomResponseDtos(rooms);



        return new ChatRoomAllResponseDto(chatRoomResponseDtoList, chatRoomPageInfoResponseDto);
    }


    /*채팅방 입장*/
    @Transactional
    public String enterChatRoom(String SessionId, HttpServletRequest request, ChatRoomEnterDataRequestDto
            requestData, User user) throws OpenViduJavaClientException, OpenViduHttpException {


        /*방이 있는 지 확인*/
        ChatRoom room = chatRoomRepository.findBySessionIdAndIsDelete(SessionId, false).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다.")));

        System.out.println("1111");

        /*방에서 강퇴당한 멤버인지 확인*/
//        BenUser benUser = benUserRepository.findByUserIdAndRoomId(user.getId(), SessionId);
//        if (benUser != null) {
//            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "강퇴당한 방입니다."));
//        }

        /*방 인원 초과 시*/
        if (room.getCntUser() >= 7) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방이 가득찼습니다."));
        }

        /*비공개 방일시 비밀번호 체크*/
        if (!room.isStatus()) {
            if (null == requestData || null == requestData.getPassword()) {    // 패스워드를 입력 안했을 때 에러 발생
                throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "비밀번호를 입력해주세요."));
            }
            if (!room.getPassword().equals(requestData.getPassword())) {  // 비밀번호가 틀리면 에러 발생
                throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "비밀번호가 틀립니다."));
            }
        }

        System.out.println("2222");

        /*룸 멤버 있는 지 확인*/
        Optional<ChatRoomUser> alreadyExitRoomUser
                = chatRoomUserRepository.findBySessionIdAndUserId(SessionId, user.getId());


        Optional<ChatRoomUser> alreadyRoomUser
                = chatRoomUserRepository.findByUserIdAndSessionId(user.getId(), SessionId);

        System.out.println("3333");
        boolean isReEnterUser = false;
        String enterRoomToken = null;

        if (alreadyRoomUser.isPresent()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "이미 입장한 멤버입니다."));

        }

        if (alreadyExitRoomUser.isPresent()) {
            
            ChatRoomUser roomUser = alreadyRoomUser.get();

            /* 방에서 나갔다가 재입장 하는 경우!*/
            isReEnterUser = true;
            enterRoomToken = enterRoomCreateSession(user, room.getSessionId());
            roomUser.reEnterRoomUsers(enterRoomToken);

        }



        if (isReEnterUser == false) {
            System.out.println("처음 입장!");
            // 채팅방 처음 입장 시 토큰 발급
            enterRoomToken = enterRoomCreateSession(user, room.getSessionId());



            // 채팅방 유저 만들기 (현재접속한 사용자) (처음 접속한 경우만)
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .sessionId(room.getSessionId())
                    .userId(user.getId())
                    .social(user.getProvider())
                    .nickname(requestData.getNickname())
                    .email(user.getEmail())
                    .profileImage(user.getProfile())
                    .enterRoomToken(enterRoomToken)
                    .roomEnterTime(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime())
                    .roomStayTime(Time.valueOf("00:00:00"))
                    .build();

            // 현재 방에 접속한 사용자 저장
            chatRoomUserRepository.save(chatRoomUser);
        }


        Long currentUser = chatRoomUserRepository.countAllBySessionId(room.getSessionId());

        room.updateCntUser(currentUser);

        chatRoomRepository.save(room);


        return "Success";
    }


    /*채팅방 + 채팅방에 속한 유저 정보 불러오기*/
    @Transactional
    public ChatRoomEnterUsersResponseDto getRoomUserData(String SessionId,
                                                         HttpServletRequest request,
                                                         User user)
            throws OpenViduJavaClientException, OpenViduHttpException {


        /*방이 있는 지 확인*/
        ChatRoom chatRoom = chatRoomRepository.findBySessionIdAndIsDelete(SessionId, false).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다.")));


        /*해당 방에 해당 유저가 접속해 있는 상태여아
        * 방유저 정보 불러오기 API를 사용할 수 있다.*/
        Optional<ChatRoomUser> alreadyRoomUser = chatRoomUserRepository.findByUserIdAndSessionId(user.getId(), SessionId);

        if (alreadyRoomUser.isEmpty()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방에 유저가 존재하지 않습니다."));
        }


        boolean roomMaster;
        boolean nowUser;

        List<ChatRoomUser> chatRoomUsersList = chatRoomUserRepository.findAllBySessionIdAndIsDelete(chatRoom.getSessionId(), false);

        List<ChatRoomEnterUserResponseDto> chatRoomEnterUserResponseDtoList = new ArrayList<>();

        // 채팅방 인원 추가
        // 방장 및 현재 접속한 유저 표시.
        for (ChatRoomUser addChatRoomUsers : chatRoomUsersList) {

            /*방장일 시*/
            if (user != null && chatRoom.getMasterUserId().equals(addChatRoomUsers.getUserId())) {
                roomMaster = true;
            } else {
                roomMaster = false;
            }


            // 현재 접속한 유저일 시 본인이 누군지 알기 위해.
            if (user != null && addChatRoomUsers.getUserId().equals((user.getId()))) {
                nowUser = true;
            } else {
                nowUser = false;
            }


            chatRoomEnterUserResponseDtoList.add(new ChatRoomEnterUserResponseDto(addChatRoomUsers, roomMaster, nowUser));

        }


        ChatRoomEnterUsersResponseDto chatRoomResponseDto
                = new ChatRoomEnterUsersResponseDto(chatRoom, chatRoomEnterUserResponseDtoList);


        return chatRoomResponseDto;


    }


    // 방 나가기
    @Transactional
    public String outRoomUser(String sessionId, HttpServletRequest request, User user) {
        /*todo 이미 나간 유저일 경우 나간 유저입니다 넣기*/

        // 방이 있는 지 확인
        ChatRoom room = chatRoomRepository.findBySessionIdAndIsDelete(sessionId, false).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방이 존재하지않습니다."))
        );

        // 룸 멤버 찾기
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByUserIdAndSessionId(user.getId(), sessionId).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방에 있는 멤버가 아닙니다."))
        );

        /*softDelete이기 때문에 이미 나간 상태면 튕구기*/
        if (chatRoomUser.isDelete()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "이미 방에서 나간 유저 입니다."));
        }


        // 룸 멤버 논리 삭제. soft delete
        // 퇴장(삭제)한 시간 기록
        LocalDateTime roomExitTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();

        /* 재입장의 경우 방에 머물었던 시간 + (방 삭제 시간 + 입장 시간 = 그 방에 총 머문시간) -> DB저장
         * 다음 입장시 이방에 몇분있었나? 보여주게 ----> 따로 유틸로 빼기!!*/
        LocalTime start = chatRoomUser.getRoomEnterTime().toLocalTime();
        LocalTime end = roomExitTime.toLocalTime();

        /*재입장의 경우 기존 머물었던 시간을 합쳐야 함.*/
        if (chatRoomUser.getRoomStayTime() != null) {
            LocalTime beforeRoomStayTime = chatRoomUser.getRoomStayTime().toLocalTime();

            long beforeHours = beforeRoomStayTime.getHour();
            long beforeMinutes = beforeRoomStayTime.getMinute();
            long beforeSeconds = beforeRoomStayTime.getSecond();

            long afterSeconds = ChronoUnit.SECONDS.between(start, end);

            LocalTime before = LocalTime.of((int) beforeHours, (int) beforeMinutes, (int) beforeSeconds);

            LocalTime roomStayTime = before.plusSeconds(afterSeconds);

            chatRoomUser.deleteRoomUsers(roomExitTime, roomStayTime);

        } else {/*처음 나간 경우*/
            Duration duration = Duration.between(start, end);

            long hours = ChronoUnit.HOURS.between(start, end);
            long minutes = ChronoUnit.MINUTES.between(start, end);
            long seconds = (ChronoUnit.SECONDS.between(start, end)) - (60 * minutes);

            LocalTime roomStayTime = LocalTime.of((int) hours, (int) minutes, (int) seconds);

            chatRoomUser.deleteRoomUsers(roomExitTime, roomStayTime);

        }









        /* 룸 유저 수 확인
         *  자신이 나갈 시 0명이된다면 (현재1명 이라면)
         * 나감과 동시에 방 논리 삭제. */
        Long cntUsers = room.getCntUser();

        if ((cntUsers - 1) == 0) {


            /*방 논리 삭제 + 방 삭제된 시간 기록*/
            LocalDateTime roomDeleteTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();
            room.deleteRoom(roomDeleteTime);

            // 방인원 0명으로.
            room.updateCntUser(room.getCntUser() - 1);


            return "Success";
        }

        // 아니라면 멤버수만 변경.
        // 룸 멤버 수 변경
        room.updateCntUser(room.getCntUser() - 1);


        // 룸 변경사항 저장
        chatRoomRepository.save(room);

        return "Success";
    }


    // 채팅방 생성 시 토큰 발급
    private ChatRoomCreateResponseDto createNewToken(User user) throws OpenViduJavaClientException, OpenViduHttpException {


        // 사용자 연결 시 닉네임 전달
        /*todo serverData에 카카오정보를 넣어서 운용이 가능할까? */
        String serverData = user.getName();


        // serverData을 사용하여 connectionProperties 객체를 빌드
        ConnectionProperties connectionProperties =
                new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).build();


        // 새로운 OpenVidu 세션(채팅방) 생성
        Session session = openvidu.createSession();


        String token = session.createConnection(connectionProperties).getToken();


        return ChatRoomCreateResponseDto.builder()
                .sessionId(session.getSessionId()) //리턴해주는 해당 세션아이디로 다른 유저 채팅방 입장시 요청해주시면 됩니다.
                .token(token) //이 토큰으로 오픈비두에 해당 유저의 화상 미디어 정보를 받아주세요
                .build();
    }


    //채팅방 입장 시 토큰 발급
    private String enterRoomCreateSession(User user, String sessionId) throws
            OpenViduJavaClientException, OpenViduHttpException {
        String serverData = user.getName();

        //serverData을 사용하여 connectionProperties 객체를 빌드
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).build();

        openvidu.fetch();

        //오픈비두에 활성화된 세션을 모두 가져와 리스트에 담음
        List<Session> activeSessionList = openvidu.getActiveSessions();

        // 1. Request : 다른 유저가 타겟 채팅방에 입장하기 위한 타겟 채팅방의 세션 정보 , 입장 요청하는 유저 정보

        Session session = null;

        //활성화된 session의 sessionId들을 registerReqChatRoom에서 리턴한 sessionId(입장할 채팅방의 sessionId)와 비교
        //같을 경우 해당 session으로 새로운 토큰을 생성
        for (Session getSession : activeSessionList) {
            if (getSession.getSessionId().equals(sessionId)) {
                session = getSession;
                break;
            }
        }
        if (session == null) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방이 존재하지않습니다."));
        }

        // 2. Openvidu에 유저 토큰 발급 요청 : 오픈비두 서버에 요청 유저가 타겟 채팅방에 입장할 수 있는 토큰을 발급 요청
        //토큰을 가져옴
        return session.createConnection(connectionProperties).getToken();
    }


    /*
     * 2차 Scope 채팅방 검색
     * */
    public Page<ChatRoom> searchRoom(String keyword, int page) {

        PageRequest pageable = PageRequest.of(page - 1, 8);

        Page<ChatRoom> searchRoom =
                chatRoomRepository.findByTitleContainingOrSubtitleContainingOrderByModifiedAtDesc(keyword
                        , keyword
                        , pageable);

        if (keyword.length() < 2 || keyword.length() > 14) {
            throw new PrivateException(new ErrorCode(HttpStatus.OK, "200", "검색 양식에 맞지 않습니다."));
        }

        // 검색 결과가 없다면
        if (searchRoom.isEmpty()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "200", "검색 결과가 없습니다."));
        }
        return searchRoom;
    }

}










