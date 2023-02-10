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
    public void BinaryImageChange(ChatMessageRequestDto chatMessageRequestDto) {
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
//            long multipartFileSize = multipartFile.getSize();

//            ObjectMetadata objectMetaData = new ObjectMetadata();
//            objectMetaData.setContentType(multipartFile.getContentType());
//            objectMetaData.setContentLength(multipartFileSize);

            amazonS3Client.putObject(new PutObjectRequest(S3Bucket, originalName, tempFile).withCannedAcl(CannedAccessControlList.PublicRead));
//            return s3Service.upload(tempFile, dirName, nickname);
        } catch (IOException ex) {
            log.error("IOException Error Message : {}",ex.getMessage());
            ex.printStackTrace();
        }
    }
}
