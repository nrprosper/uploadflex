package org.devkiki.uploadflex.service;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("minioService")
public class MinioService implements FileService {
    @Value("${minio.url}")
    private String MINIO_ENDPOINT;
    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }


    @Override
    public Map<String, String> upload(MultipartFile file, String bucketName) {
        try{

            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!isExist) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
            }

            String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String url = String.format("%s/%s/%s", MINIO_ENDPOINT, bucketName, objectName);
            return Map.of("url", url, "identifier", objectName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO" + e.getMessage());
        }
    }

    @Override
    public void delete(String identifier, String bucketName) throws IOException {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(identifier)
                            .build()
            );
        } catch (Exception e) {
            throw new IOException("Failed to delete file from MinIO: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getAllFiles(String bucketName) throws IOException {
        try {
            List<Map<String, String>> files = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();
                String url = String.format("%s/%s/%s", MINIO_ENDPOINT, bucketName, objectName);
                files.add(Map.of("url", url, "identifier", objectName));
            }
            return files;
        } catch (Exception e) {
            throw new IOException("Failed to list files from MinIO: " + e.getMessage(), e);
        }
    }


}
