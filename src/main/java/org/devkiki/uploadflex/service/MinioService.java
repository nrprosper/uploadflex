package org.devkiki.uploadflex.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public String upload(MultipartFile file, String bucketName) {
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

            return String.format("%s/%s/%s", MINIO_ENDPOINT, bucketName, objectName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO" + e.getMessage());
        }
    }


}
