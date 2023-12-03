package com.bidnamu.bidnamubackend.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseFileInfoEntity extends BaseTimeEntity {

    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private String extension;
    @Column(nullable = false)
    private Long fileSize;
    @Column(nullable = false)
    private String contentType;
}
