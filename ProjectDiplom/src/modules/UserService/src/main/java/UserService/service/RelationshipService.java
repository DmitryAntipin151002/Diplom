package UserService.service;

import UserService.dto.RelationshipDto;
import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface RelationshipService {
    @Transactional
    RelationshipDto createRelationship(UUID userId, UUID relatedUserId, String typeName);
@Transactional
    List<RelationshipDto> getUserRelationships(UUID userId, String typeName);

    RelationshipDto updateStatus(Long relationshipId, String statusName);

    void deleteRelationship(Long relationshipId);
}