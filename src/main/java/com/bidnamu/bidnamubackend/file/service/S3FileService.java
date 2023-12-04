package com.bidnamu.bidnamubackend.file.service;

import com.bidnamu.bidnamubackend.file.domain.FileInfo;
import com.bidnamu.bidnamubackend.file.exception.FileUploadException;
import com.bidnamu.bidnamubackend.global.util.FileNameUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
    public FileInfo uploadFile(final MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }
        final String fileName = getFileName(multipartFile);
        try {
            final var putObjectRequest = getPutObjectRequest(multipartFile, fileName);
            final RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new FileUploadException("파일 업로드에 실패하였습니다: " + e.getMessage());
        }

        return FileInfo.from(fileName);
    }

    @Override
    public List<FileInfo> uploadFiles(final Collection<MultipartFile> multipartFiles)
        throws FileUploadException {
        final List<FileInfo> results = new ArrayList<>();
        for (final var multipartFile : multipartFiles) {
            try {
                results.add(uploadFile(multipartFile));
            } catch (FileUploadException e) {
                results.forEach(fileInfo -> deleteFile(fileInfo.getOriginalFileName()));
            }
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