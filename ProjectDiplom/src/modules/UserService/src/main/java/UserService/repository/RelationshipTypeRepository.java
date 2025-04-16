package UserService.repository;

import UserService.model.RelationshipTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipTypeRepository extends JpaRepository<RelationshipTypeEntity, Integer> {
    Optional<RelationshipTypeEntity> findByName(String name);
}
