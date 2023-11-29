package com.bidnamu.bidnamubackend.global.util;

public class FileNameUtils {

    private FileNameUtils() {
    }

    public static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String getFileName(final String originalFileName) {
        final int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return originalFileName.substring(0, fileExtensionIndex);
    }

    public static String buildFileName(final String originalFileName) {
        final int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        final String fileExtension = originalFileName.substring(fileExtensionIndex);
        final String fileName = originalFileName.substring(0, fileExtensionIndex);
        final String now = String.valueOf(System.currentTimeMillis()); // 파일 업로드 시간
        return fileName + "_" + now + fileExtension;
    }
}
