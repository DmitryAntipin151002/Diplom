package UserProfileService.controller;

import UserProfileService.model.Friendship;
import UserProfileService.service.FriendshipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/requests")
    public ResponseEntity<Friendship> sendRequest(
            @RequestParam UUID userId,
            @RequestParam UUID friendId) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(userId, friendId));
    }

    @GetMapping
    public ResponseEntity<Page<Friendship>> getFriends(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "ACCEPTED") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(friendshipService.getFriends(
                userId,
                status,
                PageRequest.of(page, size, Sort.by("createdAt").descending()))
        );
    }
}