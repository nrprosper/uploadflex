package org.devkiki.uploadflex.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service("cloudinaryService")
public class CloudinaryService implements FileService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(MultipartFile file, String bucketName) throws IOException {
        var params = ObjectUtils.asMap(
                "public_id", bucketName+"/"+file.getOriginalFilename(),
                "folder", bucketName,
                "use_uniquename", "true"
        );

        var cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), params);
        return cloudinaryResponse.get("secure_url").toString();
    }
}
