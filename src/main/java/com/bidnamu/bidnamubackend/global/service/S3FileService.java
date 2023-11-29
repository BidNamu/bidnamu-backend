package com.bidnamu.bidnamubackend.global.service;

import com.bidnamu.bidnamubackend.global.util.FileNameUtils;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService implements FileService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(final MultipartFile multipartFile) throws FileUploadException {

        if (multipartFile.isEmpty()) {
            log.error("MultipartFile is empty.");
            throw new FileUploadException("업로드 파일이 비어있습니다.");
        }

        final String fileName = getFileName(multipartFile);

        try {
            final var putObjectRequest = getPutObjectRequest(multipartFile, fileName);
            final RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new FileUploadException("파일 업로드에 실패하였습니다: " + e.getMessage());
        }
        final var getUrlRequest = GetUrlRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    private String getFileName(final MultipartFile multipartFile) {
        return FileNameUtils.buildFileName(
            Objects.requireNonNull(multipartFile.getOriginalFilename()));
    }

    private PutObjectRequest getPutObjectRequest(MultipartFile multipartFile, String fileName) {
        return PutObjectRequest.builder()
            .bucket(bucket)
            .contentType(multipartFile.getContentType())
            .contentLength(multipartFile.getSize())
            .key(fileName)
            .build();
    }
}
