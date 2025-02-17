package GameService.repository;


import GameService.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByOrganizerId(Long organizerId); // Поиск игр по ID организатора
    List<Game> findByLocationAndStartTimeAfter(String location, LocalDateTime startTime); // Поиск игр по локации и времени
}