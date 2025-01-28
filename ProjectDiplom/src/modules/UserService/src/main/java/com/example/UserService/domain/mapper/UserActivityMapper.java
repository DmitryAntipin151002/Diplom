package mapper;

import model.UserActivity;
import dto.UserActivityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserActivityMapper {
    UserActivityMapper INSTANCE = Mappers.getMapper(UserActivityMapper.class);

    @Mapping(source = "user.id", target = "userId")
    UserActivityDTO userActivityToUserActivityDTO(UserActivity userActivity);

    @Mapping(source = "userId", target = "user.id")
    UserActivity userActivityDTOToUserActivity(UserActivityDTO userActivityDTO);
}

