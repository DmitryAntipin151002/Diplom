package UserService.repository;

import UserService.model.RelationshipStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipStatusRepository extends JpaRepository<RelationshipStatusEntity, Integer> {
    Optional<RelationshipStatusEntity> findByName(String name);
}