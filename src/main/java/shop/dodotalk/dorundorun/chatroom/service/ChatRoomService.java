package shop.dodotalk.dorundorun.chatroom.service;


import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.chatroom.dto.ChatRoomMapper;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomCreateRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.ChatRoomEnterDataRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.response.*;
import shop.dodotalk.dorundorun.chatroom.entity.*;
import shop.dodotalk.dorundorun.chatroom.repository.CategoryRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomUserRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.chatroom.util.CreateSaying;
import shop.dodotalk.dorundorun.sse.entity.SseEmitters;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.sql.Timestamp;
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
    //    private final BenUserRepository benUserRepository;
    private final CreateSaying createSaying;
    private final CategoryRepository categoryRepository;
    private final ChatRoomMapper chatRoomMapper;

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    private final SseEmitters sseEmitters; // 관우 실시간 방 개수 나타내기

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }


    /* 방 생성 */
    @Transactional
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomCreateRequestDto chatRoomCreateRequestDto, User user)
            throws Exception {

        /* Session Id, Token 셋팅 */
        ChatRoomCreateResponseDto newToken = createNewToken(user);

        /* 카테고리 별 랜덤 명언. */
        String saying = createSaying.createSaying(CategoryEnum.valueOf(chatRoomCreateRequestDto.getCategory()));

        Category category = categoryRepository.findByCategory(CategoryEnum.valueOf(chatRoomCreateRequestDto.getCategory()))
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
                );


        /*채팅방 빌드*/
        ChatRoom chatRoom = ChatRoom.builder()
                .sessionId(newToken.getSessionId())
                .title(chatRoomCreateRequestDto.getTitle())
                .subtitle(chatRoomCreateRequestDto.getSubtitle())
                .master(user.getName())
                .masterUserId(user.getId())
                .buttonImage(ButtonImageEnum.valueOf(chatRoomCreateRequestDto.getButtonImage()))
                .status(chatRoomCreateRequestDto.getStatus())
                .category(category)
                .password(chatRoomCreateRequestDto.getPassword())
                .saying(saying)
                .cntUser(0L)
                .build();


        /*빌드된 채팅방 저장(생성)*/
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);


        /*채팅방에 보여질 정보들을 리턴*/
        return ChatRoomCreateResponseDto.builder()
                .sessionId(savedRoom.getSessionId())
                .title(savedRoom.getTitle())
                .subtitle(savedRoom.getSubtitle())
                .masterName(savedRoom.getMaster())
                .isRoomMaster(true)
                .status(savedRoom.isStatus())
                .buttonImage(savedRoom.getButtonImage().name())
                .saying(saying)
                .category(savedRoom.getCategory().getCategory().getCategoryKr())
                .password(savedRoom.getPassword())
                .createdAt(savedRoom.getCreatedAt())
                .modifiedAt(savedRoom.getModifiedAt())
                .build();
    }


    /*전체 방 조회하기*/
    @Transactional
    public ChatRoomGetAllResponseDto getAllChatRooms(int page) {


        /*페이지네이션 설정 --> 무한 스크롤 예정*/
        PageRequest pageable = PageRequest.of(page - 1, 12);
        Page<ChatRoom> chatRoomList = chatRoomRepository.findByIsDeleteOrderByModifiedAtDesc(false, pageable);

        /*채팅방이 존재하지 않을 경우*/
        if (chatRoomList.isEmpty()) {
            throw new EntityNotFoundException("채팅방이 존재하지 않습니다.");
        }

        /*pagination을 위한 정보를 담은 Dto 생성*/
        ChatRoomPageInfoResponseDto chatRoomPageInfoResponseDto
                = new ChatRoomPageInfoResponseDto(page, 12,
                (int) chatRoomList.getTotalElements(), chatRoomList.getTotalPages());


        /*chatRoomList에서 Page 정보를 제외 ChatRoom만 꺼내온다.*/
        List<ChatRoom> chatRooms = chatRoomList.getContent();

        /*mapper를 활용하여 chatRoom Entity를 Dto로 변환.*/
        List<ChatRoomResponseDto> chatRoomResponseDtoList = chatRoomMapper.roomsToRoomResponseDtos(chatRooms);

        return new ChatRoomGetAllResponseDto(chatRoomResponseDtoList, chatRoomPageInfoResponseDto);
    }


    /*채팅방 입장*/
    @Transactional
    public String enterChatRoom(String SessionId, HttpServletRequest request, ChatRoomEnterDataRequestDto
            requestData, User user) throws OpenViduJavaClientException, OpenViduHttpException {


        /*방이 있는 지 확인*/
        ChatRoom chatRoom = chatRoomRepository.findBySessionId(SessionId).orElseThrow(
                () -> new EntityNotFoundException("해당 방이 없습니다."));


        /*방에서 강퇴당한 멤버인지 확인 --> 2차 scope*/
        /*BenUser benUser = benUserRepository.findByUserIdAndRoomId(user.getId(), SessionId);
        if (benUser != null) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "강퇴당한 방입니다."));
        }*/


        /*방 인원 초과 시 -> 최대 유저 6명*/
        if (chatRoom.getCntUser() >= 7) {
            throw new IllegalArgumentException("방이 가득찼습니다.");
        }

        /*비공개 방일시 비밀번호 체크*/
        if (!chatRoom.isStatus()) {
            if (null == requestData || null == requestData.getPassword()) {    // 패스워드를 입력 안했을 때 에러 발생
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
            }
            if (!chatRoom.getPassword().equals(requestData.getPassword())) {  // 비밀번호가 틀리면 에러 발생
                throw new IllegalArgumentException("비밀번호가 틀립니다.");
            }
        }


        /*실제로 해당 방에 접속이 되있으면서 다른 경로로 다시 접속하는 경우
         * -> 다른 브라우저로 똑같은 방에 접속한다던지...*/
        Optional<ChatRoomUser> alreadyEnterChatRoomUser
                = chatRoomUserRepository.findByUserIdAndSessionIdAndIsDelete(user.getId(), SessionId, false);

        if (alreadyEnterChatRoomUser.isPresent()) throw new IllegalArgumentException("이미 입장한 멤버입니다.");


        /*해당 방에서 나간 기록이 있고, 다시 '재접속' 하는 유저*/
        Optional<ChatRoomUser> reEnterChatRoomUser =
                chatRoomUserRepository.findBySessionIdAndUserId(SessionId, user.getId());



        /*방 입장 토큰*/
        String enterRoomToken = enterRoomCreateSession(user, chatRoom.getSessionId());

        /*재 입장 유저의 경우*/
        if (reEnterChatRoomUser.isPresent()) {

            ChatRoomUser chatRoomUser = reEnterChatRoomUser.get();

            /*채팅방 전용 닉네임 재설정*/
            String nickname = requestData.getNickname();

            chatRoomUser.reEnterRoomUsers(enterRoomToken, nickname);
            chatRoom.setDelete(false);

        } else {/*처음 입장하는 유저*/

            /*채팅 방 유저 빌드*/
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .sessionId(chatRoom.getSessionId())
                    .userId(user.getId())
                    .social(user.getProvider())
                    .nickname(requestData.getNickname())
                    .email(user.getEmail())
                    .profileImage(user.getProfile())
                    .enterRoomToken(enterRoomToken)
                    .roomEnterTime(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime())
                    .roomStayTime(Time.valueOf("00:00:00"))
                    .build();

            /*현재 방에 접속한 사용자 저장*/
            chatRoomUserRepository.save(chatRoomUser);
        }


        Long currentUser = chatRoomUserRepository.countAllBySessionId(chatRoom.getSessionId());

        chatRoom.updateCntUser(currentUser);

        chatRoomRepository.save(chatRoom);


        return "Success";
    }


    /*채팅방 + 채팅방에 속한 유저 정보 불러오기*/
    @Transactional
    public ChatRoomEnterUsersResponseDto getRoomUserData(String SessionId, User user) {


        /*방이 있는 지 확인*/
        ChatRoom chatRoom = chatRoomRepository.findBySessionIdAndIsDelete(SessionId, false).orElseThrow(
                () -> new EntityNotFoundException("해당 방이 없습니다."));


        /*해당 방에 해당 유저가 접속해 있는 상태여아
         * 방유저 정보 불러오기 API를 사용할 수 있다.*/
        chatRoomUserRepository.findByUserIdAndSessionIdAndIsDelete(user.getId(), SessionId, false)
                .orElseThrow(
                        () -> new IllegalArgumentException("방에 유저가 존재하지 않습니다.")
                );


        boolean chatRoomMaster;
        boolean chatRoomNowUser;

        /*채팅방 유저들 Entity*/
        List<ChatRoomUser> chatRoomUserList =
                chatRoomUserRepository.findAllBySessionIdAndIsDelete(chatRoom.getSessionId(), false);

        /*채팅방 유저들 Dto*/
        List<ChatRoomEnterUserResponseDto> chatRoomUserListResponseDto = new ArrayList<>();


        /*
        채팅방 유저들 Entity -> DTO
        방장 정보 및 현재 접속한 유저 설정*/
        for (ChatRoomUser chatRoomUser : chatRoomUserList) {

            /*방장일 시*/
            if (user != null && chatRoom.getMasterUserId().equals(chatRoomUser.getUserId())) {
                chatRoomMaster = true;
            } else {
                chatRoomMaster = false;
            }


            /*API 요청자,
             * 현재 방에 접속한 자기 자신 표시*/
            if (user != null && chatRoomUser.getUserId().equals((user.getId()))) {
                chatRoomNowUser = true;
            } else {
                chatRoomNowUser = false;
            }


            chatRoomUserListResponseDto.add(new ChatRoomEnterUserResponseDto(chatRoomUser, chatRoomMaster, chatRoomNowUser));

        }

        ChatRoomEnterUsersResponseDto chatRoomResponseDto
                = new ChatRoomEnterUsersResponseDto(chatRoom, chatRoomUserListResponseDto);

        return chatRoomResponseDto;
    }


    /*방 나가기*/
    @Transactional
    public String outRoomUser(String sessionId, HttpServletRequest request, User user) {

        /*방이 있는 지 확인*/
        ChatRoom chatRoom = chatRoomRepository.findBySessionIdAndIsDelete(sessionId, false).orElseThrow(
                () -> new EntityNotFoundException("방이 존재하지않습니다.")
        );

        /*방에 멤버가 존재하는지 확인.*/
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByUserIdAndSessionIdAndIsDelete(user.getId(), sessionId, false).orElseThrow(
                () -> new EntityNotFoundException("방에 있는 멤버가 아닙니다.")
        );

        /*이미 해당 방에서 나간 유저 표시.*/
        if (chatRoomUser.isDelete()) {
            throw new IllegalArgumentException("이미 방에서 나간 유저 입니다.");
        }


        /*해당 채팅방에서 얼마나 있었는지 시간 표시 기능 구현
         * 1. 사용자 입장 -> 프론트 엔드 시계 돌아감 -> 퇴장 시 사용자가 방에 있었던 시간 저장
         * 2. 다음 입장 시 해당 시간부터 시계 보여줌.*/


        /*방에서 나간 시간 저장.*/
        LocalDateTime chatRoomExitTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();

        LocalTime start = chatRoomUser.getRoomEnterTime().toLocalTime();
        LocalTime end = chatRoomExitTime.toLocalTime();

        /*1.기존에 현재방에서 있었던 시간을 가지고 온다, 처음 입장한 유저 = 00:00:00 */
        LocalTime beforeChatRoomStayTime = chatRoomUser.getRoomStayTime().toLocalTime();

        /*2.현재방에 들어왔던 시간 - 나가기 버튼 누른 시간 = 머문 시간*/
        long afterSeconds = ChronoUnit.SECONDS.between(start, end);

        /*3. 1번의 기존 머문 시간에 + 다시 들어왔을때의 머문시간을 더한다.
        * 처음 들어온 유저의 경우 ex) 00:00:00 + 00:05:20 */
        LocalTime chatRoomStayTime = beforeChatRoomStayTime.plusSeconds(afterSeconds);

        /*4. 채팅방 유저 논리 삭제, 방에서 나간 시간 저장, 방에 머문 시간 교체*/
        chatRoomUser.deleteRoomUsers(chatRoomExitTime, chatRoomStayTime);

        /* 채팅방 유저 수 확인
         * 채팅방 유저가 0명이라면 방 논리삭제. */
        Long cntUsers = chatRoom.getCntUser();

        if ((cntUsers - 1) <= 0) {
            /*방 논리 삭제 + 방 삭제된 시간 기록*/
            LocalDateTime roomDeleteTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();
            chatRoom.deleteRoom(roomDeleteTime);

            /*방인원 0명으로.*/
            chatRoom.updateCntUser(chatRoom.getCntUser() - 1);

            sseEmitters.count(); // 관우 실시간 방 개수 나타내기

            return "Success";
        }

        /*
        채팅방의 유저 수가 1명 이상 있다면,
        룸의 유저 수 변경
        */
        chatRoom.updateCntUser(chatRoom.getCntUser() - 1);

        return "Success";
    }


    /*채팅방 생성 시 세션 발급*/
    private ChatRoomCreateResponseDto createNewToken(User user) throws OpenViduJavaClientException, OpenViduHttpException {


        /*사용자 연결 시 닉네임 전달*/
        String serverData = user.getName();


        /*serverData을 사용하여 connectionProperties 객체를 빌드*/
        ConnectionProperties connectionProperties =
                new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).build();


        /*새로운 OpenVidu 세션(채팅방) 생성*/
        Session session = openvidu.createSession();


        /*  채팅방 생성 시 방을 만들며, 방장이 들어가지게 구현하려면 아래의 코드로 토큰 바로 발급
            방 생성, 방 입장(방장 입장) 로직이 나누어져 있다면 토큰 발급 필요 없음.
        String token = session.createConnection(connectionProperties).getToken();
        */

        return ChatRoomCreateResponseDto.builder()
                .sessionId(session.getSessionId()) //리턴해주는 해당 세션아이디로 다른 유저 채팅방 입장시 요청해주시면 됩니다.
                .build();
    }


    /*채팅방 입장 시 토큰 발급*/
    private String enterRoomCreateSession(User user, String sessionId) throws
            OpenViduJavaClientException, OpenViduHttpException {

        String serverData = user.getName();

        /*serverData을 사용하여 connectionProperties 객체를 빌드*/
        ConnectionProperties connectionProperties
                = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).build();

        openvidu.fetch();

        /*오픈비두에 활성화된 세션을 모두 가져와 리스트에 담음*/
        List<Session> activeSessionList = openvidu.getActiveSessions();

        /*1. Request : 다른 유저가 타겟 채팅방에 입장하기 위한 타겟 채팅방의 세션 정보 , 입장 요청하는 유저 정보*/

        Session session = null;

        /*활성화된 session의 sessionId들을 registerReqChatRoom에서 리턴한 sessionId(입장할 채팅방의 sessionId)와 비교
        같을 경우 해당 session으로 새로운 토큰을 생성*/
        for (Session getSession : activeSessionList) {
            if (getSession.getSessionId().equals(sessionId)) {
                session = getSession;
                break;
            }
        }

        if (session == null) {
            throw new EntityNotFoundException("방이 존재하지않습니다.");
        }

        /*2. Openvidu에 유저 토큰 발급 요청 : 오픈비두 서버에 요청 유저가 타겟 채팅방에 입장할 수 있는 토큰을 발급 요청
        토큰을 가져옴*/
        return session.createConnection(connectionProperties).getToken();
    }


    /*
     * 2차 Scope 채팅방 검색
     * */
    @Transactional
    public ChatRoomGetAllResponseDto searchRoom(String keyword, int page) {

        if (keyword.length() < 1 || keyword.length() > 20) {
            throw new IllegalArgumentException("검색 양식에 맞지 않습니다.");
        }

        PageRequest pageable = PageRequest.of(page - 1, 12);

        Page<ChatRoom> searchRoom =
                chatRoomRepository.findByTitleContainingOrSubtitleContainingOrderByModifiedAtDesc(keyword
                        , keyword
                        , pageable);

        /*검색 결과가 없다면*/
        if (searchRoom.isEmpty()) {
            throw new EntityNotFoundException("검색 결과가 없습니다.");
        }

        /*pagination을 위한 정보를 담은 Dto 생성*/
        ChatRoomPageInfoResponseDto chatRoomPageInfoResponseDto
                = new ChatRoomPageInfoResponseDto(page, 12,
                (int) searchRoom.getTotalElements(), searchRoom.getTotalPages());


        /*chatRoomList에서 Page 정보를 제외 ChatRoom만 꺼내온다.*/
        List<ChatRoom> chatRooms = searchRoom.getContent();

        /*mapper를 활용하여 chatRoom Entity를 Dto로 변환.*/
        List<ChatRoomResponseDto> chatRoomResponseDtoList = chatRoomMapper.roomsToRoomResponseDtos(chatRooms);

        return new ChatRoomGetAllResponseDto(chatRoomResponseDtoList, chatRoomPageInfoResponseDto);

    }

}










