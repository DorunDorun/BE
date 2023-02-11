package shop.dodotalk.dorundorun.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String iamAccessKey = "IAM 생성할 때 확인했던 AccessKey 입력"; // IAM Access Key
    @Value("${cloud.aws.credentials.secret-key}")
    private String iamSecretKey = "IAM 생성할 때 확인했던 SecretKey 입력"; // IAM Secret Key
    //@Value("${cloud.aws.region.static}")
    private String region = "ap-northeast-2"; // Bucket Region

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
