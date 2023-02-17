package shop.dodotalk.dorundorun.message.service;

import org.springframework.http.HttpStatus;
import shop.dodotalk.dorundorun.aws.service.AwsService;
import shop.dodotalk.dorundorun.chatroom.entity.Room;
import shop.dodotalk.dorundorun.chatroom.error.ErrorCode;
import shop.dodotalk.dorundorun.chatroom.error.PrivateException;
import shop.dodotalk.dorundorun.chatroom.repository.RoomRepository;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;
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
    private final RoomRepository roomRepository;
    private final RoomMessageRepository roomMessageRepository;
    private final RoomFileMessageRepository roomFileMessageRepository;

    @Transactional
    public ChatMessageResponseDto ChatMessageCreate(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findByEmail(chatMessageRequestDto.getEmail()).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "해당 유저가 없습니다.")));

        Room room = roomRepository.findById(chatMessageRequestDto.getRoomId()).orElseThrow(
                () -> new PrivateException(new ErrorCode(HttpStatus.BAD_REQUEST, "400", "해당 방이 없습니다.")));

        ChatMessageResponseDto chatMessageResponseDto;

        if (chatMessageRequestDto.getImgByteCode() != null) {
            chatMessageResponseDto = BinaryImageChange(chatMessageRequestDto);
            RoomFileMessage roomFileMessage = new RoomFileMessage(chatMessageResponseDto, user, room);
            roomFileMessageRepository.save(roomFileMessage);

        } else {
            chatMessageResponseDto = new ChatMessageResponseDto(chatMessageRequestDto);
            RoomMessage roomMessage = new RoomMessage(chatMessageResponseDto, user, room);
            roomMessageRepository.save(roomMessage);
        }

        return chatMessageResponseDto;
    }


    @Transactional
    public ChatMessageResponseDto BinaryImageChange(ChatMessageRequestDto chatMessageRequestDto) {

        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessageRequestDto);

        try {
            String[] strings = chatMessageRequestDto.getImgByteCode().split(",");
            String base64Image = strings[1];
            String extension = "";
            if (strings[0].equals("data:image/jpeg;base64")) {
                extension = "jpeg";
            } else if (strings[0].equals("data:image/png;base64")){
                extension = "png";
            } else {
                extension = "jpg";
            }

            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

            File tempFile = File.createTempFile("image", "." + extension);
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(imageBytes);
            }

            String awsS3ImageUrl = awsService.S3FileImageUpload(tempFile);

            chatMessageResponseDto.ChatMessageImgUpdate(awsS3ImageUrl);
        } catch (IOException ex) {
            log.error("IOException Error Message : {}",ex.getMessage());
            ex.printStackTrace();
        }

        return chatMessageResponseDto;

    }
}
