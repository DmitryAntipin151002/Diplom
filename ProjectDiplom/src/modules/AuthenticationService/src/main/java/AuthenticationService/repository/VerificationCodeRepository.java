package AuthenticationService.repository;


import AuthenticationService.domain.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findByUserId(UUID userId);  // Поиск кода по userId
    void deleteByUserId(UUID userId);  // Удаление всех кодов для пользователя
}
