package com.example.homework;

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
}
