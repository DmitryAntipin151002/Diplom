package UserProfileService.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// UserProfileDTO.java
public record UserProfileDTO(
        UUID userId,
        String avatarUrl,
        String bio,
        LocalDate dateOfBirth,
        String gender,
        String location,
        String sportType,
        String fitnessLevel,
        String goals,
        String achievements,
        String personalRecords,
        Boolean isVerified,
        Integer totalActivities,
        BigDecimal totalDistance,
        Integer totalWins
) {}

// UpdateProfileRequest.java
public record UpdateProfileRequest(
        @NotBlank String bio,
        @Past LocalDate dateOfBirth,
        @Pattern(regexp = "^(MALE|FEMALE|OTHER)$") String gender,
        String location
) {}

// FriendRequest.java
public record FriendRequest(
        @NotNull UUID friendId
) {}