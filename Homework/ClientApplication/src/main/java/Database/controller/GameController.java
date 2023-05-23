package Database.controller;
import Database.Game;
import Database.GameRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // get
    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

}