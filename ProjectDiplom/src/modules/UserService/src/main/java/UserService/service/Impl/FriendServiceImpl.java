package UserService.service.Impl;


import UserService.domain.dto.FriendDTO;
import UserService.domain.model.Friend;
import UserService.domain.model.Users;
import UserService.exception.ResourceNotFoundException;
import UserService.repository.FriendRepository;
import UserService.repository.UsersRepository;
import UserService.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UsersRepository usersRepository;

    @Override
    public Friend addFriend(Users user, FriendDTO friendDTO) {
        Users friend = usersRepository.findById(friendDTO.getFriendId())
                .orElseThrow(() -> new ResourceNotFoundException("Друг не найден"));

        // Проверяем, существует ли уже дружба
        Optional<Friend> existingFriendship = friendRepository.findByUserAndFriend(user, friend);
        if (existingFriendship.isPresent()) {
            throw new IllegalArgumentException("Этот пользователь уже в списке друзей");
        }

        Friend friendRelation = new Friend();
        friendRelation.setUser(user);
        friendRelation.setFriend(friend);
        return friendRepository.save(friendRelation);
    }
@Override
    public List<Friend> getFriends(Users user) {
        return friendRepository.findByUser(user);
    }
@Override
    @Transactional
    public void removeFriend(Users user, Long friendId) {
        // Используем findByUserAndFriendId, чтобы передать только ID
        Friend friendship = friendRepository.findByUserAndFriendId(user.getUserId(), friendId)
                .orElseThrow(() -> new ResourceNotFoundException("Друг не найден"));

        friendRepository.delete(friendship);
    }
}
