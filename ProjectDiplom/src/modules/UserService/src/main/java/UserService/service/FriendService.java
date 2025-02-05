package UserService.service;

import UserService.domain.dto.FriendDTO;
import UserService.domain.model.Friend;
import UserService.domain.model.Users;

import java.util.List;

public interface FriendService {
    public Friend addFriend(Users user, FriendDTO friendDTO);
    public List<Friend> getFriends(Users user);
    public void removeFriend(Users user, Long friendId);
}