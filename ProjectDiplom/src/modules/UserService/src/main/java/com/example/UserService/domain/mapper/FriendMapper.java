package mapper;

import model.Friend;
import dto.FriendDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FriendMapper {
    FriendMapper INSTANCE = Mappers.getMapper(FriendMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "friend.id", target = "friendId")
    FriendDTO friendToFriendDTO(Friend friend);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "friendId", target = "friend.id")
    Friend friendDTOToFriend(FriendDTO friendDTO);
}
