package org.devkiki.uploadflex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.devkiki.uploadflex.resolver.FileServiceResolver;
import org.devkiki.uploadflex.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "File Controller", description = "File management endpoints")
@RequestMapping("/api/files")
@RestController
public class FileController {
    private final FileServiceResolver fileServiceResolver;

    public FileController(FileServiceResolver fileServiceResolver) {
        this.fileServiceResolver = fileServiceResolver;
    }

    @PostMapping(path = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload File",
            description = "Endpoint for Uploading file"
    )
    public ResponseEntity<Map<String, String>> upload(
            @Parameter(
                    description = "File to upload",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "provider", required = false)
            @Parameter(description = "Storage provider key: minioService, cloudinaryService")
            String provider
    ) throws IOException {
        FileService service = fileServiceResolver.resolve(provider);
        var uploadResponse = service.upload(file, "uploadflex");
        return ResponseEntity.ok().body(uploadResponse);
    }

    @DeleteMapping("/{bucketName}/{identifier}")
    @Operation(
            summary = "Delete File",
            description = "Endpoint for deleting a file from the specified bucket using its identifier"
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Bucket name")
            @PathVariable String bucketName,
            @Parameter(description = "File identifier")
            @PathVariable String identifier,
            @RequestParam(value = "provider", required = false)
            @Parameter(description = "Storage provider key: minioService, cloudinaryService")
            String provider
    ) throws IOException {
        FileService service = fileServiceResolver.resolve(provider);
        service.delete(identifier, bucketName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bucketName}")
    @Operation(
            summary = "Get All Files",
            description = "Endpoint for retrieving all files in the specified bucket"
    )
    public ResponseEntity<List<Map<String, String>>> getAllFiles(
            @Parameter(description = "Bucket name")
            @PathVariable String bucketName,
            @RequestParam(value = "provider", required = false)
            @Parameter(description = "Storage provider key: minioService, cloudinaryService")
            String provider
    ) throws IOException {
        FileService service = fileServiceResolver.resolve(provider);
        List<Map<String, String>> files = service.getAllFiles(bucketName);
        return ResponseEntity.ok().body(files);
    }

}
