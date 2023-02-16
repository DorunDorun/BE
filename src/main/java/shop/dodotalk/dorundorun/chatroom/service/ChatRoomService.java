package shop.dodotalk.dorundorun.chatroom.service;


import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.chatroom.dto.request.CreateRoomRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.request.RoomPasswordRequestDto;
import shop.dodotalk.dorundorun.chatroom.dto.response.CreateRoomResponseDto;
import shop.dodotalk.dorundorun.chatroom.dto.response.RoomUsersResponseDto;
import shop.dodotalk.dorundorun.chatroom.entity.BenUser;
import shop.dodotalk.dorundorun.chatroom.entity.Category;
import shop.dodotalk.dorundorun.chatroom.entity.Room;
import shop.dodotalk.dorundorun.chatroom.entity.RoomUsers;
import shop.dodotalk.dorundorun.chatroom.error.ErrorCode;
import shop.dodotalk.dorundorun.chatroom.error.PrivateException;
import shop.dodotalk.dorundorun.chatroom.repository.BenUserRepository;
import shop.dodotalk.dorundorun.chatroom.repository.CategoryRepository;
import shop.dodotalk.dorundorun.chatroom.repository.RoomUsersRepository;
import shop.dodotalk.dorundorun.chatroom.repository.RoomRepository;
import shop.dodotalk.dorundorun.chatroom.util.CreateSaying;
import shop.dodotalk.dorundorun.security.jwt.JwtUtil;
import shop.dodotalk.dorundorun.users.entity.User;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final RoomRepository roomRepository;
    private final RoomUsersRepository roomUsersRepository;
    private final BenUserRepository benUserRepository;
    private final CreateSaying createSaying;
    private final CategoryRepository categoryRepository;

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

    public CreateRoomResponseDto createRoom(CreateRoomRequestDto createRoomRequestDto,
                                            HttpServletRequest request, User user)
            throws OpenViduJavaClientException, OpenViduHttpException {


        CreateRoomResponseDto newToken = createNewToken(user);
        log.info("생성된 토큰은 : " + newToken + " 입니다.");


        /* 카테고리 확인 후 해당 카테고리에 맞는 랜덤 명언 제조 */
        String saying = createSaying.createSaying(createRoomRequestDto.getCategory());

        Optional<Category> optionalCategory = categoryRepository.findByCategory(createRoomRequestDto.getCategory());
        if (optionalCategory.isEmpty()) {
            throw new IllegalAccessError("해당 카테고리가 존재하지 않습니다.");
        }

        Category category = optionalCategory.get();


        // 채팅방 빌드
        Room room = Room.builder()
                .sessionId(newToken.getSessionId())
                .title(createRoomRequestDto.getTitle())
                .subtitle(createRoomRequestDto.getSubtitle())
                .master(user.getName())
                .buttonImage(createRoomRequestDto.getButtonImage())
                .status(createRoomRequestDto.isStatus())
                .category(category)
                .password(createRoomRequestDto.getPassword())
                .saying(saying)
                .build();


        // 채팅방 저장
        Room savedRoom = roomRepository.save(room);

        // 아래 코드 계속 진행시 createdAt이 null로 바뀌어서.
        // 이부분에서 미리 저장해놓음 .
        LocalDateTime createdAt = savedRoom.getCreatedAt();





        // 방생성 할때 첫유저 (방장)
        RoomUsers roomUsers = RoomUsers.builder()
                .sessionId(savedRoom.getSessionId())
                .user(user.getId())
                .nickname(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfile())
                .enterRoomToken(savedRoom.getSessionId())
                .roomEnterTime(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime())
                .enterRoomToken(newToken.getToken())
                .build();

        // 채팅방 인원 저장하기
        roomUsersRepository.save(roomUsers);


        boolean roomMaster;
        List<RoomUsers> roomUsersList = roomUsersRepository.findAllBySessionId(savedRoom.getSessionId());

        List<RoomUsersResponseDto> roomUsersResponseDtoList = new ArrayList<>();
        // 채팅방 인원 추가
        for (RoomUsers roomUsersEle : roomUsersList) {
            // 방장일 시
            if (user != null) {
                roomMaster = Objects.equals(roomUsersEle.getNickname(), user.getName());
            }
            // 방장이 아닐 시
            else {
                roomMaster = false;
            }
            roomUsersResponseDtoList.add(new RoomUsersResponseDto(roomUsersEle, roomMaster));

        }


        Long currentUser = roomUsersRepository.countAllBySessionId(savedRoom.getSessionId());

        room.updateCntUser(currentUser);
        roomRepository.save(room);


        // 저장된 채팅방의 roomId는 OpenVidu 채팅방의 세션 아이디로써 생성 후 바로 해당 채팅방의 세션 아이디와
        // 오픈 비두 서버에서 미디어 데이터를 받아올 떄 사용할 토큰을 리턴.
        // 채팅방 생성 후 최초 채팅방 생성자는 채팅방에 즉시 입장할 것으로 예상 -> 채팅방이 보여지기 위한 정보들을 리턴
        return CreateRoomResponseDto.builder()
                .sessionId(savedRoom.getSessionId())
                .title(savedRoom.getTitle())
                .subtitle(savedRoom.getSubtitle())
                .master(savedRoom.getMaster())
                .status(savedRoom.isStatus())
                .token(newToken.getToken())
                .buttonImage(savedRoom.getButtonImage().name())
                .saying(saying)
                .category(savedRoom.getCategory().getCategory().name())
                .password(savedRoom.getPassword())
                .createdAt(createdAt)
                .modifiedAt(savedRoom.getModifiedAt())
                .build();




    }


    // 전체 방 조회하기
    public Page<Room> getAllRooms(int page) {
        PageRequest pageable = PageRequest.of(page - 1, 8);
        Page<Room> roomlist = roomRepository.findByIsDeleteOrderByModifiedAtDesc(false, pageable);
        if (roomlist.isEmpty()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "200", "검색 결과가 없습니다."));
        }
        return roomlist;
    }

//    // 키워드로 채팅방 검색하기
//    public Page<Room> searchRoom(String keyword, int page) {
//        PageRequest pageable = PageRequest.of(page - 1, 8);
//
//        Page<Room> searchRoom = roomRepository.findByTitleContainingOrderByModifiedAtDesc(keyword, pageable);
//        if (keyword.length() < 2 || keyword.length() > 14) {
//            throw new PrivateException(new ErrorCode(HttpStatus.OK, "200", "검색 양식에 맞지 않습니다."));
//        }
//
//        // 검색 결과가 없다면
//        if (searchRoom.isEmpty()) {
//            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "200", "검색 결과가 없습니다."));
//        }
//
//        return searchRoom;
//
//    }


    // 방 접속
    public RoomUsersResponseDto getRoomData(String SessionId, HttpServletRequest request, RoomPasswordRequestDto
            password, User user) throws OpenViduJavaClientException, OpenViduHttpException {


        // 방이 있는 지 확인
        Room room = roomRepository.findById(SessionId).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다.")));


        // 방에서 강퇴당한 멤버인지 확인
        BenUser benUser = benUserRepository.findByUserIdAndRoomId(user.getId(), SessionId);
        if (benUser != null) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "강퇴당한 방입니다."));
        }
        // 방 인원 초과 시
        if (room.getCntUser() >= 7) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방이 가득찼습니다."));
        }

        // 룸 멤버 있는 지 확인
        Optional<RoomUsers> alreadyRoomUser = roomUsersRepository.findBySessionIdAndNickname(SessionId, user.getName());
        if (alreadyRoomUser.isPresent()) {
            throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "이미 입장한 멤버입니다."));
        }

        // 비공개 방일시 비밀번호 체크 등등
        if (!room.isStatus()) {
            if (null == password || null == password.getPassword())  {    // 패스워드를 입력 안했을 때 에러 발생
                throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "비밀번호를 입력해주세요."));
            }
            if (!room.getPassword().equals(password.getPassword())) {  // 비밀번호가 틀리면 에러 발생
                throw new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "비밀번호가 틀립니다."));
            }
        }
        //채팅방 입장 시 토큰 발급
        String enterRoomToken = enterRoomCreateSession(user, room.getSessionId());

        // 채팅방 인원
        RoomUsers roomUsers = RoomUsers.builder()
                .sessionId(room.getSessionId())
                .user(user.getId())
                .nickname(user.getName())
                .email(user.getEmail())
                .profileImage(user.getProfile())
                .enterRoomToken(enterRoomToken)
                .roomEnterTime(Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime())
                .build();

        // 채팅방 인원 저장하기
        roomUsersRepository.save(roomUsers);

        boolean roomMaster = false;
        List<RoomUsers> roomUsersList = roomUsersRepository.findAllBySessionId(room.getSessionId());

        List<RoomUsersResponseDto> roomUsersResponseDtoList = new ArrayList<>();

        // 채팅방 인원 추가
        for (RoomUsers addRoomUsers : roomUsersList) {
            // 방장일 시
            if (user != null) {
                roomMaster = Objects.equals(addRoomUsers.getNickname(), user.getName());
            }
            // 방장이 아닐 시
            else {
                roomMaster = false;
            }
            roomUsersResponseDtoList.add(new RoomUsersResponseDto(addRoomUsers, roomMaster));

        }

        Long currentUser = roomUsersRepository.countAllBySessionId(room.getSessionId());

        room.updateCntUser(currentUser);

        roomRepository.save(room);

        return RoomUsersResponseDto.builder()
                .roomUserId(roomUsers.getRoomUserId())
                .sessionId(roomUsers.getSessionId())
                .User(roomUsers.getUser())
                .nickname(roomUsers.getNickname())
                .email(roomUsers.getEmail())
                .ProfileImage(roomUsers.getProfileImage())
                .enterRoomToken(roomUsers.getEnterRoomToken())
                .roomMaster(roomMaster)
                .build();
    }




    // 방 나가기
    @Transactional
    public String outRoomUser(String sessionId, HttpServletRequest request, User user) {


        // 방이 있는 지 확인
        Room room = roomRepository.findById(sessionId).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방이 존재하지않습니다."))
        );

        // 룸 멤버 찾기
        RoomUsers roomUsers = roomUsersRepository.findBySessionIdAndNickname(sessionId, user.getName()).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "방에 있는 멤버가 아닙니다."))
        );



        // 룸 멤버 논리 삭제. soft delete
        // 퇴장(삭제)한 시간 기록
        LocalDateTime roomExitTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();
        roomUsers.deleteRoomUsers(roomExitTime);



        /* 룸 유저 수 확인
        *  자신이 나갈 시 0명이된다면 (현재1명 이라면)
        * 나감과 동시에 방 논리 삭제. */
        Long cntUsers = room.getCntUser();

        if ((cntUsers - 1) == 0){


            // 방 논리 삭제 + 방 삭제된 시간 기록
            LocalDateTime roomDeleteTime = Timestamp.valueOf(LocalDateTime.now()).toLocalDateTime();
            room.deleteRoom(roomDeleteTime);

            // 방인원 0명으로.
            room.updateCntUser(room.getCntUser() - 1);

            // 방이 삭제된 시간 기록.


            return "Success";
        }

        // 아니라면 멤버수만 변경.
        // 룸 멤버 수 변경
        room.updateCntUser(room.getCntUser() - 1);


        // 룸 변경사항 저장
        roomRepository.save(room);

        return "Success";
    }


    // 채팅방 생성 시 토큰 발급
    private CreateRoomResponseDto createNewToken(User user) throws OpenViduJavaClientException, OpenViduHttpException {




        // 사용자 연결 시 닉네임 전달
        /*todo serverData에 카카오정보를 넣어서 운용이 가능할까? */
        String serverData = user.getName();




        // serverData을 사용하여 connectionProperties 객체를 빌드
        ConnectionProperties connectionProperties =
                new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).data(serverData).build();



        // 새로운 OpenVidu 세션(채팅방) 생성
        Session session = openvidu.createSession();



        String token = session.createConnection(connectionProperties).getToken();



        return CreateRoomResponseDto.builder()
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


}










