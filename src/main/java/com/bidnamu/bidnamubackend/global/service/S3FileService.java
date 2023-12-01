package com.bidnamu.bidnamubackend.global.service;

import com.bidnamu.bidnamubackend.global.util.FileNameUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(final MultipartFile multipartFile) throws FileUploadException {
        if (multipartFile.isEmpty()) {
            throw new FileUploadException("업로드 파일이 비어있습니다.");
        }
        final String fileName = getFileName(multipartFile);
        try {
            final var putObjectRequest = getPutObjectRequest(multipartFile, fileName);
            final RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new FileUploadException("파일 업로드에 실패하였습니다: " + e.getMessage());
        }

        return fileName;
    }

    @Override
    public List<String> uploadFiles(final Collection<MultipartFile> multipartFiles)
        throws FileUploadException {
        final List<String> results = new ArrayList<>();
        for (final var multipartFile : multipartFiles) {
            results.add(uploadFile(multipartFile));
        }
        return results;
    }

    @Override
    public void deleteFile(final String fileKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(fileKey)
            .build());
    }

    private String getFileName(final MultipartFile multipartFile) {
        return FileNameUtils.buildFileName(
            Objects.requireNonNull(multipartFile.getOriginalFilename()));
    }

    private PutObjectRequest getPutObjectRequest(final MultipartFile multipartFile,
        final String fileName) {
        return PutObjectRequest.builder()
            .bucket(bucket)
            .contentType(multipartFile.getContentType())
            .contentLength(multipartFile.getSize())
            .key(fileName)
            .build();
    }
}
