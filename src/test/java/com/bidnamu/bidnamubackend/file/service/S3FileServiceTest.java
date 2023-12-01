package com.bidnamu.bidnamubackend.file.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
class S3FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    @DisplayName("MultipartFile 한 개 업로드 시 예외가 발생하지 않는다")
    void testUploadFile() {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());

        // Then
        assertDoesNotThrow(() -> fileService.uploadFile(mockFile));
    }

    @Test
    @DisplayName("MultipartFile 여러개 업로드 시 예외가 발생하지 않는다")
    void testUploadFiles() {
        // Given
        final List<MultipartFile> files = IntStream.range(0, 3).mapToObj(
            i -> new MockMultipartFile("file", "filename%d.txt".formatted(i), "text/plain",
                "test content".getBytes())).collect(Collectors.toList());

        // Then
        assertDoesNotThrow(() -> fileService.uploadFiles(files));
    }

    @Test
    @DisplayName("MultipartFile 삭제 요청시 예외가 발생하지 않는다")
    void testDeleteFile() throws Exception {
        // Given
        final MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain",
            "test content".getBytes());
        final String fileKey = fileService.uploadFile(mockFile);

        // Then
        assertDoesNotThrow(() -> fileService.deleteFile(fileKey));
    }
}
