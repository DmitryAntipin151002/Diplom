package UserService.repository;

import UserService.model.ChatTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatTypeRepository extends JpaRepository<ChatTypeEntity, Long> {
    Optional<ChatTypeEntity> findByName(String name);

    Optional<ChatTypeEntity> findByCode(String code);

    boolean existsByCode(String code);
}