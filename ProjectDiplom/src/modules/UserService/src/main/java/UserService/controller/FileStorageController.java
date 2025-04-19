package UserService.controller;


import UserService.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/files") // Явно указываем полный путь
@RequiredArgsConstructor
public class FileStorageController {
    private final FileStorageService fileStorageService;
    private final Path rootLocation = Paths.get("uploads");
    @PostMapping(
            value = "/{userId}/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadFile(
            @PathVariable UUID userId,
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String filePath = fileStorageService.store(file, userId);
        return ResponseEntity.ok(filePath);

    }

    @GetMapping("/{userId}/{filename:.+}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String userId,
            @PathVariable String filename) {

        String filePath = userId + "/" + filename;
        Resource file = fileStorageService.loadAsResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(file);
    }


    @DeleteMapping("/{userId}/{filename}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable UUID userId,
            @PathVariable String filename) {

        String filePath = userId + "/" + filename;
        fileStorageService.delete(filePath);
        return ResponseEntity.noContent().build();

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleExceptions(RuntimeException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file))
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}