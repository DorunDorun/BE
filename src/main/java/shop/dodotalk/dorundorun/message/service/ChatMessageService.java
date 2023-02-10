package shop.dodotalk.dorundorun.message.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import shop.dodotalk.dorundorun.message.dto.ChatMessageRequestDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

//import com.project.trysketch.dto.request.GameFlowRequestDto;
//import com.project.trysketch.entity.*;
//import com.project.trysketch.dto.GamerEnum;
//import com.project.trysketch.global.utill.sse.SseEmitters;
//import com.project.trysketch.repository.*;
//import com.project.trysketch.global.exception.CustomException;
//import com.project.trysketch.global.exception.StatusMsgCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodotalk.dorundorun.message.dto.ChatMessageResponseDto;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {
    @Autowired // aws img test
    AmazonS3Client amazonS3Client;
    private String S3Bucket = "mysparta1"; // Bucket 이름 aws img test
    @Transactional
    public ChatMessageResponseDto BinaryImageChange(ChatMessageRequestDto chatMessageRequestDto) {
        try {
            String[] strings = chatMessageRequestDto.getImgCode().split(",");
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

            String originalName = UUID.randomUUID().toString();

            amazonS3Client.putObject(new PutObjectRequest(S3Bucket, originalName, tempFile).withCannedAcl(CannedAccessControlList.PublicRead));

            String awsS3ImageUrl = amazonS3Client.getUrl(S3Bucket, originalName).toString();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile); // 파일 삭제시 전부 아웃풋 닫아줘야함
                fileOutputStream.close();
                if (tempFile.delete()) {
                    log.info("File delete success");
                } else {
                    log.info("File delete fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessageRequestDto);
            chatMessageResponseDto.setImgCode(awsS3ImageUrl);
            return chatMessageResponseDto;
        } catch (IOException ex) {
            log.error("IOException Error Message : {}",ex.getMessage());
            ex.printStackTrace();
        }
    return new ChatMessageResponseDto(); // 이 부분은 꼭 수정해야할듯
    }
}
