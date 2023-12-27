package com.bidnamu.bidnamubackend.file.domain;

import com.bidnamu.bidnamubackend.global.util.FileNameUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class FileInfo {

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String extension;
    @Column(nullable = false)
    private Long fileSize;
    @Column(nullable = false)
    private String contentType;

    public static FileInfo of(final String originalFileName, final MultipartFile file) {
        return FileInfo.builder()
            .fileName(FileNameUtils.getFileName(originalFileName))
            .originalFileName(originalFileName)
            .extension(FileNameUtils.getExtension(originalFileName))
            .fileSize(file.getSize())
            .contentType(file.getContentType())
            .build();
    }
}