package UserService.controller;

import UserService.domain.dto.FriendDTO;
import UserService.domain.model.Friend;
import UserService.domain.model.Users;
import UserService.service.FriendService;
import UserService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    @PostMapping("add")
    public ResponseEntity<Friend> addFriend(
            @RequestParam Long userId,
            @Valid @RequestBody FriendDTO friendDTO) {
        Users user = userService.getUserById(userId);
        Friend friend = friendService.addFriend(user, friendDTO);
        return ResponseEntity.ok(friend);
    }

    @GetMapping
    public ResponseEntity<List<Friend>> getFriends(@RequestParam Long userId) {
        Users user = userService.getUserById(userId);
        List<Friend> friends = friendService.getFriends(user);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(@RequestParam Long userId, @PathVariable Long friendId) {
        Users user = userService.getUserById(userId);
        friendService.removeFriend(user, friendId);
        return ResponseEntity.noContent().build();
    }
}
