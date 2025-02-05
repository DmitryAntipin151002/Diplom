package UserService.domain.mapper;


import UserService.domain.dto.UserProfileDTO;
import UserService.domain.dto.request.ProfileUpdateRequest;
import UserService.domain.dto.response.UserProfileResponse;
import UserService.domain.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")

public interface UserProfileMapper {

    // Маппинг UserProfile -> UserProfileDTO, используя user.userId
    @Mapping(target = "userId", source = "user.userId")
    UserProfileDTO toDto(UserProfile userProfile);

    // Маппинг UserProfileDTO -> UserProfile, игнорируем user (его задаем вручную)
    @Mapping(target = "user", ignore = true)
    UserProfile toEntity(UserProfileDTO dto);

    // Маппинг UserProfile -> UserProfileResponse
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    // Маппинг ProfileUpdateRequest -> UserProfile, игнорируем user (он устанавливается отдельно)
    @Mapping(target = "user", ignore = true)
    UserProfile toUserProfile(ProfileUpdateRequest request);

    // Обновление UserProfile из запроса
    void updateUserProfileFromRequest(ProfileUpdateRequest request, @MappingTarget UserProfile userProfile);
}


