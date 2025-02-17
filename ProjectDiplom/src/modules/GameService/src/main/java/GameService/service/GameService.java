package GameService.service;


import GameService.model.Game;
import GameService.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    // Создание игры
    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    // Редактирование игры
    public Game updateGame(Long id, Game gameDetails) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        game.setName(gameDetails.getName());
        game.setDescription(gameDetails.getDescription());
        game.setLocation(gameDetails.getLocation());
        game.setStartTime(gameDetails.getStartTime());
        game.setEndTime(gameDetails.getEndTime());
        return gameRepository.save(game);
    }

    // Поиск игр
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // Поиск игр по локации и времени
    public List<Game> findGamesByLocationAndTime(String location, LocalDateTime startTime) {
        return gameRepository.findByLocationAndStartTimeAfter(location, startTime);
    }

    // Получение игры по ID
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    // Удаление игры
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}