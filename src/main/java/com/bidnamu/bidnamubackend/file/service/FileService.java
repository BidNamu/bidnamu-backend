package com.bidnamu.bidnamubackend.file.service;

import java.util.Collection;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(final MultipartFile multipartFile);

    List<String> uploadFiles(final Collection<MultipartFile> multipartFiles);

    void deleteFile(final String fileKey);
}
