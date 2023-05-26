package com.example;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerRepository playerRepository; // Repository for accessing player data

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping()
    public List<DataBasePlayer> getAllPlayers() {
        return playerRepository.findAll(); // Retrieve all players
    }

    @PostMapping
    public DataBasePlayer addPlayer(@RequestBody DataBasePlayer player) {
        return playerRepository.save(player); // Add a new player
    }

    @PutMapping("/{id}")
    public DataBasePlayer updatePlayerName(@PathVariable Long id, @RequestParam String name) {
        DataBasePlayer player = playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.setName(name); // Update the player's name
        return playerRepository.save(player); // Save the updated player
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id); // Delete the player with the given ID
    }
}