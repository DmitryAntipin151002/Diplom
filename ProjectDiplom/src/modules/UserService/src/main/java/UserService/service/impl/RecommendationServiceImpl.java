package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.UserNotFoundException;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final UserActivityRepository activityRepository;
    @Qualifier("userRelationshipRepository")
    private final UserRelationshipRepository relationshipRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<EventRecommendationDto> getEventRecommendations(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getProfile() == null) {
            return Collections.emptyList();
        }

        String preferredSport = user.getSportType();
        String location = user.getLocation();
        List<UUID> friendIds = relationshipRepository.findFriendIds(userId);

        List<Event> events = eventRepository.findRecommendedEvents(
                preferredSport,
                location,
                friendIds);

        return events.stream()
                .map(event -> {
                    EventRecommendationDto dto = modelMapper.map(event, EventRecommendationDto.class);
                    dto.setMatchScore(calculateEventMatchScore(event, user));
                    dto.setOrganizerId(event.getOrganizer().getId());
                    dto.setOrganizerName(event.getOrganizer().getEmail()); // или другое поле с именем
                    return dto;
                })
                .sorted(Comparator.comparingDouble(EventRecommendationDto::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRecommendationDto> getUserRecommendations(UUID userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (currentUser.getProfile() == null) {
            return Collections.emptyList();
        }

        return userRepository.findRecommendedUsers(userId).stream()
                .filter(user -> user.getProfile() != null)
                .map(user -> {
                    UserRecommendationDto dto = modelMapper.map(user, UserRecommendationDto.class);
                    dto.setMatchScore(calculateUserMatchScore(user, currentUser));
                    dto.setMutualFriendsCount(
                            relationshipRepository.countMutualFriends(userId, user.getId()));
                    dto.setAvatarUrl(user.getProfile().getAvatarUrl());
                    return dto;
                })
                .sorted(Comparator.comparingDouble(UserRecommendationDto::getMatchScore).reversed())
                .collect(Collectors.toList());
    }

    private double calculateEventMatchScore(Event event, User user) {
        double score = 0.0;

        // Совпадение вида спорта (если указан у пользователя и мероприятия)
        if (user.getSportType() != null && user.getSportType().equals(event.getSportType())) {
            score += 0.4;
        }

        // Близость локации (если указана у пользователя и мероприятия)
        if (user.getLocation() != null && user.getLocation().equals(event.getLocation())) {
            score += 0.3;
        }

        // Участие друзей (если есть друзья)
        List<UUID> friendIds = relationshipRepository.findFriendIds(user.getId());
        if (!friendIds.isEmpty()) {
            long participatingFriends = event.getParticipants().stream()
                    .filter(p -> friendIds.contains(p.getUser().getId()))
                    .count();
            score += Math.min(participatingFriends * 0.1, 0.3);
        }

        return Math.min(score, 1.0);
    }

    private double calculateUserMatchScore(User recommendedUser, User currentUser) {
        double score = 0.0;

        // Совпадение вида спорта (если указан у обоих пользователей)
        if (currentUser.getSportType() != null &&
                currentUser.getSportType().equals(recommendedUser.getSportType())) {
            score += 0.5;
        }

        // Совпадение локации (если указана у обоих пользователей)
        if (currentUser.getLocation() != null &&
                currentUser.getLocation().equals(recommendedUser.getLocation())) {
            score += 0.3;
        }

        // Количество общих друзей (нормализованное значение)
        int mutualFriends = relationshipRepository.countMutualFriends(
                currentUser.getId(),
                recommendedUser.getId());
        score += Math.min(mutualFriends * 0.1, 0.2);

        return Math.min(score, 1.0);
    }
}