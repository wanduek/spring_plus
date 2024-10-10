package org.example.expert.domain.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 이미지 업로드 하기
     */
    public String uploadImage(MultipartFile image) throws IOException, java.io.IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename(); // 고유한 파일 이름 생성

        // 메타데이터 설정 (ContentType 추가)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        // 이미지 파일의 InputStream 가져오기
        InputStream inputStream = image.getInputStream();

        // S3에 파일 업로드 (metadata 없이 간단하게 업로드)
        amazonS3.putObject(bucket, fileName, inputStream, null);

        return getPublicUrl(fileName);
    }



    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
    }
}