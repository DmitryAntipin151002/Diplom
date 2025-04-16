package UserService.service.impl;

import UserService.dto.RelationshipDto;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.UserRelationshipRepository;
import UserService.repository.UserRepository;
import UserService.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {
    @Qualifier("userRelationshipRepository")
    private final UserRelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RelationshipDto> getUserRelationships(UUID userId, RelationshipType type) {
        return relationshipRepository.findByUserIdAndType(userId, type)
                .stream()
                .map(rel -> {
                    RelationshipDto dto = modelMapper.map(rel, RelationshipDto.class);
                    dto.setUserId(rel.getUser().getId());
                    dto.setRelatedUserId(rel.getRelatedUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RelationshipDto createRelationship(UUID userId, UUID relatedUserId, RelationshipType type) {
        if (userId.equals(relatedUserId)) {
            throw new SelfRelationshipException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User relatedUser = userRepository.findById(relatedUserId)
                .orElseThrow(() -> new UserNotFoundException(relatedUserId));

        Optional<UserRelationship> existing = relationshipRepository
                .findByUserIdAndRelatedUserIdAndType(userId, relatedUserId, type);

        if (existing.isPresent()) {
            throw new RelationshipExistsException();
        }

        UserRelationship relationship = UserRelationship.builder()
                .user(user)
                .relatedUser(relatedUser)
                .type(type)
                .status(type == RelationshipType.FRIEND ?
                        RelationshipStatus.PENDING : RelationshipStatus.ACCEPTED)
                .build();

        UserRelationship saved = relationshipRepository.save(relationship);
        return toDto(saved);
    }

    @Override
    @Transactional
    public RelationshipDto updateRelationshipStatus(Long relationshipId, RelationshipStatus status) {
        UserRelationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException(relationshipId));

        if (relationship.getType() != RelationshipType.FRIEND) {
            throw new IllegalArgumentException("Only friend relationships can be updated");
        }

        relationship.setStatus(status);
        relationship.setUpdatedAt(LocalDateTime.now());

        UserRelationship updated = relationshipRepository.save(relationship);
        return toDto(updated);
    }

    private RelationshipDto toDto(UserRelationship relationship) {
        RelationshipDto dto = modelMapper.map(relationship, RelationshipDto.class);
        dto.setUserId(relationship.getUser().getId());
        dto.setRelatedUserId(relationship.getRelatedUser().getId());
        // status уже правильно маппится через modelMapper
        return dto;
    }

    // В реализацию RelationshipServiceImpl
    @Override
    @Transactional
    public void deleteRelationship(Long relationshipId) {
        if (!relationshipRepository.existsById(relationshipId)) {
            throw new RelationshipNotFoundException(relationshipId);
        }
        relationshipRepository.deleteById(relationshipId);
    }
}