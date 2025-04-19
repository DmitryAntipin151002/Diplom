package UserService.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {
    void init();
    String store(MultipartFile file, UUID userId); // Добавлен userId
    void delete(String filePath);
    Resource loadAsResource(String filePath);
}