package UserService.service.impl;

import UserService.dto.ProfileDto;
import UserService.dto.RelationshipDto;
import UserService.dto.UserDto;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.ProfileService;
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
    private final ProfileService profileService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RelationshipTypeRepository typeRepository;
    private final RelationshipStatusRepository statusRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public List<RelationshipDto> getUserRelationships(UUID userId, String typeName) {

        if (!userProfileRepository.existsByUserId(userId)) {
            profileService.getOrCreateProfile(userId);
        }

        RelationshipTypeEntity type = typeRepository.findByName(typeName)
                .orElseThrow(() -> new RelationshipTypeNotFoundException(typeName));

        return relationshipRepository.findByUserIdAndType(userId, type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RelationshipDto createRelationship(UUID userId, UUID relatedUserId, String typeName) {
        RelationshipTypeEntity type = typeRepository.findByName(typeName)
                .orElseThrow(() -> new RelationshipTypeNotFoundException(typeName));

        if (userId.equals(relatedUserId)) {
            throw new SelfRelationshipException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User relatedUser = userRepository.findById(relatedUserId)
                .orElseThrow(() -> new UserNotFoundException(relatedUserId));

        Optional<UserRelationship> existing = relationshipRepository.findByUsersAndType(
                userId, relatedUserId, typeName);

        if (existing.isPresent()) {
            throw new RelationshipExistsException();
        }

        RelationshipStatusEntity initialStatus = getInitialStatus(type);

        UserRelationship relationship = UserRelationship.builder()
                .user(user)
                .relatedUser(relatedUser)
                .type(type)
                .status(initialStatus)
                .createdAt(LocalDateTime.now())
                .build();

        UserRelationship saved = relationshipRepository.save(relationship);
        return toDto(saved);
    }

    private RelationshipStatusEntity getInitialStatus(RelationshipTypeEntity type) {
        String statusName = "FRIEND".equals(type.getName()) ? "PENDING" : "ACCEPTED";
        return statusRepository.findByName(statusName)
                .orElseThrow(() -> new RelationshipStatusNotFoundException(statusName));
    }

    @Override
    @Transactional
    public RelationshipDto updateStatus(Long relationshipId, String statusName) {
        RelationshipStatusEntity status = statusRepository.findByName(statusName)
                .orElseThrow(() -> new RelationshipStatusNotFoundException(statusName));

        UserRelationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RelationshipNotFoundException(relationshipId));

        if (!"FRIEND".equals(relationship.getType().getName())) {
            throw new InvalidRelationshipTypeException("Status can only be updated for FRIEND relationships");
        }

        relationship.setStatus(status);
        relationship.setUpdatedAt(LocalDateTime.now());

        return toDto(relationshipRepository.save(relationship));
    }

    @Override
    @Transactional
    public void deleteRelationship(Long relationshipId) {
        if (!relationshipRepository.existsById(relationshipId)) {
            throw new RelationshipNotFoundException(relationshipId);
        }
        relationshipRepository.deleteById(relationshipId);
    }

    private RelationshipDto toDto(UserRelationship relationship) {
        RelationshipDto dto = modelMapper.map(relationship, RelationshipDto.class);
        dto.setUser(convertToUserDto(relationship.getUser()));
        dto.setRelatedUser(convertToUserDto(relationship.getRelatedUser()));
        return dto;
    }
    private UserDto convertToUserDto(User user) {
        try {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userDto.setProfile(profileService.getProfile(user.getId()));
            return userDto;
        } catch (ProfileNotFoundException ex) {
            // Создаем пустой профиль при его отсутствии
            ProfileDto newProfile = profileService.getOrCreateProfile(user.getId());
            UserDto userDto = modelMapper.map(user, UserDto.class); // Маппим базовые поля
            userDto.setProfile(newProfile); // Устанавливаем новый профиль в ДТО
            return userDto;
        }
    }
}