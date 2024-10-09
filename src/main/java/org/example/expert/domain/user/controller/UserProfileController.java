package org.example.expert.domain.user.controller;

import org.example.expert.domain.common.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/profile")
public class UserProfileController {

    private final S3Service s3Service;

    @Autowired
    public UserProfileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 로컬 임시 파일 생성
        File tempFile = convertMultiPartToFile(file);

        // S3 버킷에 파일 업로드
        String fileName = "profile-images/" + file.getOriginalFilename();
        s3Service.uploadFile(fileName, tempFile);

        // 임시 파일 삭제
        tempFile.delete();

        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<URL> downloadProfileImage(@PathVariable String filename) {
        String fileKey = "profile-images/" + filename;
        URL presignedUrl = s3Service.getPresignedUrl(fileKey);

        return ResponseEntity.ok(presignedUrl);
    }

    // MultipartFile을 File 객체로 변환하는 헬퍼 메소드
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
