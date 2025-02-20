package SocialService.controller;

import SocialService.model.Friend;
import SocialService.model.Rating;
import SocialService.service.SocialService;
import SocialService.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SocialController {
    @Autowired
    private SocialService socialService;

    // Комментарии
    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment comment) {
        return socialService.addComment(comment);
    }

    @GetMapping("/comments/{gameId}")
    public List<Comment> getComments(@PathVariable Long gameId) {
        return socialService.getCommentsByGame(gameId);
    }

    // Рейтинги
    @PostMapping("/ratings")
    public Rating addRating(@RequestBody Rating rating) {
        return socialService.addRating(rating);
    }

    // Друзья
    @PostMapping("/friends")
    public Friend addFriend(@RequestBody Friend friend) {
        return socialService.addFriend(friend);
    }

    @DeleteMapping("/friends")
    public void removeFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        socialService.removeFriend(userId, friendId);
    }
}
