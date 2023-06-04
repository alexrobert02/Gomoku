package com.example.DataBase.Controller;

import com.example.DataBase.GameHistory;
import com.example.DataBase.Repository.GameHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game-history")
public class GameHistoryController {

    private final GameHistoryRepository gameHistoryRepository; // Repository for accessing game history data

    public GameHistoryController(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    @GetMapping
    public List<GameHistory> getAllGameHistory() {
        return gameHistoryRepository.findAll(); // Retrieve all game history entries
    }

    @PostMapping
    public GameHistory addGameHistory(@RequestBody GameHistory gameHistory) {
        return gameHistoryRepository.save(gameHistory); // Add a new game history entry
    }
    @PutMapping("/{id}")
    public GameHistory updatePlayerId(@PathVariable Long id, @RequestParam Long playerId) {
        GameHistory game = gameHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        game.setPlayer2Id(playerId); // Update the player's id
        return gameHistoryRepository.save(game); // Save the updated game
    }
    @PutMapping("/{id}/status")
    public GameHistory updateGameStatus(@PathVariable Long id, @RequestParam String status) {
        GameHistory game = gameHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        game.setStatus(status); // Update the game's status
        return gameHistoryRepository.save(game); // Save the updated game
    }
    @PutMapping("/{id}/result")
    public GameHistory updateGameResult(@PathVariable Long id, @RequestParam String result) {
        GameHistory game = gameHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        game.setResult(result); // Update the game's result
        return gameHistoryRepository.save(game); // Save the updated game
    }
}
