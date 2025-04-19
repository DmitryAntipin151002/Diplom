package UserService.service.impl;

import UserService.exception.FileStorageException;
import UserService.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation;

    public FileStorageServiceImpl(@Value("${app.file-storage.location:uploads}") String location) {
        this.rootLocation = Paths.get(location).toAbsolutePath().normalize();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file, UUID userId) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file");
            }

            // Создаем папку пользователя
            Path userDir = this.rootLocation.resolve(userId.toString());
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }

            // Генерируем уникальное имя файла
            String filename = UUID.randomUUID() +
                    getFileExtension(file.getOriginalFilename());

            // Сохраняем файл
            Path destination = userDir.resolve(filename);
            Files.copy(file.getInputStream(), destination,
                    StandardCopyOption.REPLACE_EXISTING);

            return userId + "/" + filename;

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    private String getFileExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.'))
                : "";
    }

    @Override
    public void delete(String filePath) {
        try {
            Path file = rootLocation.resolve(filePath).normalize();
            if (!Files.exists(file)) {
                throw new FileStorageException("File not found: " + filePath);
            }
            Files.deleteIfExists(file);

            // Удаляем папку пользователя, если она пуста
            Path userDir = file.getParent();
            if (Files.list(userDir).count() == 0) {
                Files.deleteIfExists(userDir);
            }
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file: " + filePath, e);
        }
    }

    @Override
    public Resource loadAsResource(String filePath) {
        try {
            Path file = rootLocation.resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileStorageException("Could not read file: " + filePath);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException("Could not read file: " + filePath, e);
        }
    }
}