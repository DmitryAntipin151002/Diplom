package SocialService.service;

import SocialService.model.Comment;
import SocialService.model.Friend;
import SocialService.model.Rating;
import SocialService.repository.CommentRepository;
import SocialService.repository.FriendRepository;
import SocialService.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SocialService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private FriendRepository friendRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Friend addFriend(Friend friend) {
        return friendRepository.save(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        friendRepository.deleteByFriendIdAndUserId(userId, friendId);
    }

    public List<Comment> getCommentsByGame(Long gameId) {
        return commentRepository.findByGameId(gameId);
    }
}