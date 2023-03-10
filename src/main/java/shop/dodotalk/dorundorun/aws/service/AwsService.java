package shop.dodotalk.dorundorun.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsService {
    @Autowired
    AmazonS3Client amazonS3Client;
    private String S3Bucket = "dodo-buket"; // S3 Bucket 이름

    @Transactional
    public String S3FileImageUpload(File tempFile) {
        String originalName = UUID.randomUUID().toString();

        amazonS3Client.putObject(new PutObjectRequest(S3Bucket, originalName, tempFile).withCannedAcl(CannedAccessControlList.PublicRead));

        String awsS3ImageUrl = amazonS3Client.getUrl(S3Bucket, originalName).toString();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile); // 파일 삭제시 전부 아웃풋 닫아줘야함
            fileOutputStream.close();
            if (tempFile.delete()) {
                log.info("파일 성공적으로 삭제됨");
            } else {
                log.info("파일 성공적으로 삭제안됨");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return awsS3ImageUrl;
    }
}
