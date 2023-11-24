package com.lucy.arti.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Manager {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile,String dir) throws IOException {
        String S3_PATH_PREFIX = dir+"/";
        String s3FileName = S3_PATH_PREFIX + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(multipartFile.getSize());

            amazonS3.putObject(bucket, s3FileName, inputStream, objMeta);
        } catch (AmazonS3Exception e) {
            // S3 업로드 중 오류가 발생한 경우 예외 처리
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.", e);
        }
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

}
