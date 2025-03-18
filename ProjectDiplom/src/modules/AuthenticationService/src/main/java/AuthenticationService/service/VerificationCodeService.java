package AuthenticationService.service;

import AuthenticationService.domain.dto.VerificationCodeDto;

import AuthenticationService.domain.mapper.UserMapper;
import AuthenticationService.domain.model.VerificationCode;
import AuthenticationService.repository.VerificationCodeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
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

        // Преобразуем userId в UUID (если это необходимо для БД)
        UUID userUuid = UUID.fromString(userId);

        // Удаляем старые коды для пользователя, чтобы не было конфликтов
        verificationCodeRepository.deleteByUserId(userUuid);

        // Создаем запись о коде верификации и сохраняем его в базе данных
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUserId(userUuid);
        verificationCode.setCode(code);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // код действует 5 минут
        verificationCodeRepository.save(verificationCode);

        // Возвращаем DTO с кодом
        return userMapper.codeToVerificationCodeDto(code);
    }

    @Transactional
    public boolean validateVerificationCode(String userId, String code) {
        UUID userUuid = UUID.fromString(userId);

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByUserId(userUuid);

        if (verificationCodeOptional.isEmpty()) {
            return false;
        }

        VerificationCode verificationCode = verificationCodeOptional.get();
        LocalDateTime currentTime = LocalDateTime.now();

        // Проверяем, совпадает ли код и не истек ли срок действия
        return verificationCode.getCode().equals(code) && currentTime.isBefore(verificationCode.getExpiresAt());
    }
}
