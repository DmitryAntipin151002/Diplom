package UserService.service;



import UserService.dto.EventRecommendationDto;
import UserService.dto.UserRecommendationDto;
import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    List<EventRecommendationDto> getEventRecommendations(UUID userId);
    List<UserRecommendationDto> getUserRecommendations(UUID userId);
}