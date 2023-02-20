package shop.dodotalk.dorundorun.message.service;

import org.springframework.http.HttpStatus;
import shop.dodotalk.dorundorun.aws.service.AwsService;
import shop.dodotalk.dorundorun.chatroom.entity.BenUser;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoom;
import shop.dodotalk.dorundorun.chatroom.entity.ChatRoomUser;
import shop.dodotalk.dorundorun.chatroom.repository.BenUserRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomRepository;
import shop.dodotalk.dorundorun.chatroom.repository.ChatRoomUserRepository;
import shop.dodotalk.dorundorun.error.CustomErrorException;
import shop.dodotalk.dorundorun.message.dto.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.message.entity.RoomFileMessage;
import shop.dodotalk.dorundorun.message.entity.RoomMessage;
import shop.dodotalk.dorundorun.message.repository.RoomFileMessageRepository;
import shop.dodotalk.dorundorun.message.repository.RoomMessageRepository;
import shop.dodotalk.dorundorun.users.entity.User;
import shop.dodotalk.dorundorun.users.repository.UserRepository;


@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final AwsService awsService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final BenUserRepository benUserRepository;
    private final RoomMessageRepository roomMessageRepository;
    private final RoomFileMessageRepository roomFileMessageRepository;

    @Transactional
    public ChatMessageResponseDto ChatMessageCreate(ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageResponseDto chatMessageResponseDto;

        if (chatMessageRequestDto.getImgByteCode() != null) {
            RoomFileMessage roomFileMessage = BinaryImageChange(chatMessageRequestDto);
            chatMessageResponseDto = new ChatMessageResponseDto(roomFileMessage);
            roomFileMessageRepository.save(roomFileMessage);
        } else {
            RoomMessage roomMessage = new RoomMessage(chatMessageRequestDto);
            chatMessageResponseDto = new ChatMessageResponseDto(roomMessage);
            roomMessageRepository.save(roomMessage);
        }

        return chatMessageResponseDto;
    }

    @Transactional
    public ChatMsgDeleteResponseDto ChatMessageDelete(ChatMsgDeleteRequestDto chatMsgDeleteRequestDto,
                                                    User user) {
        ChatRoom room = chatRoomRepository.findById(chatMsgDeleteRequestDto.getSessionId()).orElseThrow(
                () -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다."));

        BenUser benUser = benUserRepository.findByUserIdAndRoomId(user.getId(), chatMsgDeleteRequestDto.getSessionId());
        if (benUser != null) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "강퇴당한 방입니다.");
        }

        ChatRoomUser alreadyRoomUser = chatRoomUserRepository.findBySessionIdAndUserId(chatMsgDeleteRequestDto.getSessionId(), user.getId())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방에 유저가 없습니다."));

        RoomMessage roomMessage = roomMessageRepository.findBySessionIdAndMessageIdAndSocialUid(
                chatMsgDeleteRequestDto.getSessionId(), chatMsgDeleteRequestDto.getMessageId(), chatMsgDeleteRequestDto.getSocialUid())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방에 메세지가 없습니다."));

        roomMessage.RoomMessageDelete();

        ChatMsgDeleteResponseDto chatMsgDeleteResponseDto = new ChatMsgDeleteResponseDto(HttpStatus.OK, "성공적으로 메세지가 삭제되었습니다.");

        return chatMsgDeleteResponseDto;
    }

    @Transactional
    public ChatFileDeleteResponseDto ChatFileDelete(ChatFileDeleteRequestDto chatFileDeleteRequestDto,
                                                       User user) {
        ChatRoom room = chatRoomRepository.findById(chatFileDeleteRequestDto.getSessionId()).orElseThrow(
                () -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다."));

        BenUser benUser = benUserRepository.findByUserIdAndRoomId(user.getId(), chatFileDeleteRequestDto.getSessionId());
        if (benUser != null) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "강퇴당한 방입니다.");
        }


        ChatRoomUser alreadyRoomUser = chatRoomUserRepository.findBySessionIdAndUserId(chatFileDeleteRequestDto.getSessionId(), user.getId())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방에 유저가 없습니다."));

        RoomFileMessage roomFile = roomFileMessageRepository.findBySessionIdAndFileIdAndSocialUid(
                chatFileDeleteRequestDto.getSessionId(), chatFileDeleteRequestDto.getFileId(), chatFileDeleteRequestDto.getSocialUid())
                .orElseThrow(() -> new CustomErrorException(HttpStatus.BAD_REQUEST, "400", "해당 방에 파일메세지가 없습니다."));

        roomFile.RoomFileDelete();

        ChatFileDeleteResponseDto chatFileDeleteResponseDto = new ChatFileDeleteResponseDto(HttpStatus.OK, "성공적으로 파일이 삭제되었습니다.");

        return chatFileDeleteResponseDto;
    }

    @Transactional
    public RoomFileMessage BinaryImageChange(ChatMessageRequestDto chatMessageRequestDto) {
        RoomFileMessage roomFileMessage;
        try {
            String[] strings = chatMessageRequestDto.getImgByteCode().split(",");
            String base64Image = strings[1];

            String extension = strings[0].split(";")[0].split("/")[1];

            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            File tempFile = File.createTempFile("image", "." + extension);
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(imageBytes);
            }

            String awsS3ImageUrl = awsService.S3FileImageUpload(tempFile);

            roomFileMessage = new RoomFileMessage(chatMessageRequestDto, awsS3ImageUrl);
            return roomFileMessage;
        } catch (IOException ex) {
            log.error("IOException Error Message : {}",ex.getMessage());
            ex.printStackTrace();
        }
        throw new CustomErrorException(HttpStatus.OK, "200", "이미지 파일이 생성되지 않았습니다");
    }
}
