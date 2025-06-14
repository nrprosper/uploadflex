package org.devkiki.uploadflex.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {
    Map<String, String> upload(MultipartFile file, String bucketName) throws IOException;
    void delete(String identifier, String bucketName) throws IOException;
    List<Map<String, String>> getAllFiles(String bucketName) throws IOException;
}
