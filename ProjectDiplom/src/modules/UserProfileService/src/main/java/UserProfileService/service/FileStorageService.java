package UserProfileService.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final StorageProperties properties;

    public String store(MultipartFile file, UUID userId) {
        if (properties.getType().equals("s3")) {
            // Реализация для S3
        } else {
            Path path = Paths.get(properties.getFilesystem().getPath(), userId.toString());
            // Сохранение файла
            return path.toString();
        }
    }
}