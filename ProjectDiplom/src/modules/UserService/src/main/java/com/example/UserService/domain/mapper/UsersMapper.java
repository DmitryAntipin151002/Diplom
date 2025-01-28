package mapper;
import model.Users;
import dto.UsersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProfileMapper.class, FriendMapper.class, SubscriptionMapper.class, UserActivityMapper.class})
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    @Mapping(source = "profiles", target = "profiles")
    @Mapping(source = "friends", target = "friends")
    @Mapping(source = "subscriptions", target = "subscriptions")
    @Mapping(source = "userActivities", target = "userActivities")
    UsersDTO usersToUsersDTO(Users users);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "profiles", target = "profiles")
    @Mapping(source = "friends", target = "friends")
    @Mapping(source = "subscriptions", target = "subscriptions")
    @Mapping(source = "userActivities", target = "userActivities")
    Users usersDTOToUsers(UsersDTO usersDTO);
}


