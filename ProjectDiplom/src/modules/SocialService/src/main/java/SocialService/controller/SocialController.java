package SocialService.controller;

import SocialService.model.Friend;
import SocialService.model.Rating;
import SocialService.service.SocialService;
import SocialService.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Social", description = "API для работы с комментариями, рейтингами и друзьями") // Описание API
public class SocialController {

    @Autowired
    private SocialService socialService;

    // Комментарии

    @Operation(summary = "Добавить комментарий",
            description = "Добавляет новый комментарий для игры",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий добавлен"),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос")
            })
    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment comment) {
        return socialService.addComment(comment);
    }

    @Operation(summary = "Получить комментарии по игре",
            description = "Возвращает список комментариев для игры по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарии получены"),
                    @ApiResponse(responseCode = "404", description = "Игра не найдена")
            })
    @GetMapping("/comments/{gameId}")
    public List<Comment> getComments(@PathVariable Long gameId) {
        return socialService.getCommentsByGame(gameId);
    }

    // Рейтинги

    @Operation(summary = "Добавить рейтинг",
            description = "Добавляет новый рейтинг для игры",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Рейтинг добавлен"),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос")
            })
    @PostMapping("/ratings")
    public Rating addRating(@RequestBody Rating rating) {
        return socialService.addRating(rating);
    }

    // Друзья

    @Operation(summary = "Добавить друга",
            description = "Добавляет нового друга для пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Друг добавлен"),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос")
            })
    @PostMapping("/friends")
    public Friend addFriend(@RequestBody Friend friend) {
        return socialService.addFriend(friend);
    }

    @Operation(summary = "Удалить друга",
            description = "Удаляет друга из списка друзей пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Друг удален"),
                    @ApiResponse(responseCode = "400", description = "Неверный запрос"),
                    @ApiResponse(responseCode = "404", description = "Пользователь или друг не найден")
            })
    @DeleteMapping("/friends")
    public void removeFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        socialService.removeFriend(userId, friendId);
    }
}
