package GameService.controller;


import GameService.model.Game;
import GameService.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    // Создание игры
    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameService.createGame(game);
    }

    // Редактирование игры
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable Long id, @RequestBody Game gameDetails) {
        return gameService.updateGame(id, gameDetails);
    }

    // Получение списка всех игр
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    // Поиск игр по локации и времени
    @GetMapping("/search")
    public List<Game> findGamesByLocationAndTime(
            @RequestParam String location,
            @RequestParam LocalDateTime startTime) {
        return gameService.findGamesByLocationAndTime(location, startTime);
    }

    // Получение игры по ID
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    // Удаление игры
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }
}
