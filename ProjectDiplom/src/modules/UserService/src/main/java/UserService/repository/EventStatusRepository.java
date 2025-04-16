package UserService.repository;

import UserService.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventStatusRepository extends JpaRepository<EventStatus, Integer> {
    Optional<EventStatus> findByCode(String code);

    boolean existsByCode(String code);
}