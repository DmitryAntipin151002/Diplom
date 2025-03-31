package AuthenticationService.service;

import AuthenticationService.domain.dto.VerificationCodeDto;
import AuthenticationService.domain.mapper.UserMapper;
import AuthenticationService.domain.model.VerificationCode;
import AuthenticationService.repository.VerificationCodeRepository;

import lombok.extern.slf4j.Slf4j; // Импортируем Lombok логгер

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j // Аннотация для автоматической генерации логгера
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserMapper userMapper;
    private final Random random = new Random();

    public VerificationCodeService(VerificationCodeRepository verificationCodeRepository, UserMapper userMapper) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public VerificationCodeDto generateVerificationCode(String userId) {
        // Генерация 6-значного кода
        String code = String.valueOf(random.nextInt(900000) + 100000);
        log.info("Generated verification code: {}", code);

        // Преобразуем userId в UUID (если это необходимо для БД)
        UUID userUuid = UUID.fromString(userId);
        log.info("User ID (UUID): {}", userUuid);

        // Удаляем старые коды для пользователя, чтобы не было конфликтов
        log.info("Deleting old verification codes for userId: {}", userId);
        verificationCodeRepository.deleteByUserId(userUuid); // Это нужно, если хотите только один активный код

        // Создаем запись о коде верификации и сохраняем его в базе данных
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUserId(userUuid);
        verificationCode.setCode(code);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // код действует 5 минут

        log.info("Saving verification code to the database for userId: {}", userId);
        verificationCodeRepository.save(verificationCode);

        // Возвращаем DTO с кодом
        log.info("Returning DTO for generated verification code.");
        return userMapper.codeToVerificationCodeDto(code);
    }

    @Transactional
    public boolean validateVerificationCode(String userId, String code) {
        log.info("Validating verification code for userId: {}", userId);

        UUID userUuid = UUID.fromString(userId);
        log.info("User ID (UUID) for validation: {}", userUuid);

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByUserId(userUuid);
        if (verificationCodeOptional.isEmpty()) {
            log.warn("No verification code found for userId: {}", userId); // Логирование предупреждения
            return false; // Код не найден
        }

        VerificationCode verificationCode = verificationCodeOptional.get();
        log.info("Found verification code for userId: {}: {}", userId, verificationCode.getCode());

        LocalDateTime currentTime = LocalDateTime.now();
        log.info("Current time: {}", currentTime);

        // Проверяем, совпадает ли код и не истек ли срок действия
        if (!verificationCode.getCode().equals(code)) {
            log.warn("Invalid code for userId: {}. Expected: {}, Provided: {}", userId, verificationCode.getCode(), code); // Логирование предупреждения
            return false; // Неверный код
        }

        if (currentTime.isAfter(verificationCode.getExpiresAt())) {
            log.warn("Verification code expired for userId: {}. Expiry time: {}", userId, verificationCode.getExpiresAt()); // Логирование предупреждения
            return false; // Код истек
        }

        log.info("Verification code is valid for userId: {}", userId);
        // Если код валиден, возвращаем true
        return true;
    }
}
