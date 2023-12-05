package com.bidnamu.bidnamubackend.file.service;

import com.bidnamu.bidnamubackend.file.domain.FileInfo;
import java.util.Collection;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileInfo uploadFile(final MultipartFile multipartFile);

    List<FileInfo> uploadFiles(final Collection<MultipartFile> multipartFiles);

    void deleteFile(final String fileKey);
}