package com.bidnamu.bidnamubackend.global.service;

import java.util.Collection;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(final MultipartFile multipartFile) throws FileUploadException;

    List<String> uploadFiles(final Collection<MultipartFile> multipartFiles)
        throws FileUploadException;

    void deleteFile(final String fileKey);
}
