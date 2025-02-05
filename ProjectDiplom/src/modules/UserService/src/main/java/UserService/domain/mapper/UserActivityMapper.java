package UserService.domain.mapper;



import UserService.domain.dto.UserActivityDTO;
import UserService.domain.model.UserActivity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserActivityMapper {
    UserActivityMapper INSTANCE = Mappers.getMapper(UserActivityMapper.class);

    UserActivityDTO toDto(UserActivity userActivity);
    UserActivity toEntity(UserActivityDTO userActivityDTO);
}