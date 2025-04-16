package UserService.service;

import UserService.dto.RelationshipDto;
import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface RelationshipService {
    List<RelationshipDto> getUserRelationships(UUID userId, RelationshipType type);

    @Transactional
    RelationshipDto createRelationship(UUID userId, UUID relatedUserId, RelationshipType type);

    @Transactional
    RelationshipDto updateRelationshipStatus(Long relationshipId, RelationshipStatus status);

    @Transactional
    void deleteRelationship(Long relationshipId);
}