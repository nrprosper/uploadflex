package org.devkiki.uploadflex.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("cloudinaryService")
public class CloudinaryService implements FileService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, String> upload(MultipartFile file, String bucketName) throws IOException {
        var params = ObjectUtils.asMap(
                "public_id", bucketName+"/"+file.getOriginalFilename(),
                "folder", bucketName,
                "use_uniquename", "true"
        );

        var cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), params);
        String url = cloudinaryResponse.get("secure_url").toString();
        String publicId = cloudinaryResponse.get("public_id").toString();
        return Map.of("url", url, "identifier", publicId);
    }

    @Override
    public void delete(String identifier, String bucketName) throws IOException {
        try {
            cloudinary.uploader().destroy(identifier, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new IOException("Failed to delete file from Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getAllFiles(String bucketName) throws IOException {
        try {
            List<Map<String, String>> files = new ArrayList<>();
            var response = cloudinary.api().resources(ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", bucketName + "/",
                    "max_results", 500
            ));
            List<Map> resources = (List<Map>) response.get("resources");
            for (Map resource : resources) {
                String url = resource.get("secure_url").toString();
                String publicId = resource.get("public_id").toString();
                files.add(Map.of("url", url, "identifier", publicId));
            }
            return files;
        } catch (Exception e) {
            throw new IOException("Failed to list files from Cloudinary: " + e.getMessage(), e);
        }
    }
}
