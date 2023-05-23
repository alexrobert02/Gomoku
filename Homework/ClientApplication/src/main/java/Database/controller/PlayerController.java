package Database.controller;


import Database.Player;
import Database.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // get
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // post
    @PostMapping("/players")
    public  Player addPlayer (@RequestBody Player newPlayer){
        return playerRepository.save(newPlayer);
    }
    // update name
    @PutMapping("/players/{id}")
    Player updatePlayer(@PathVariable Long id, @RequestParam String name) {

        return playerRepository.findById(id)
                .map(player -> {
                    player.setName(name);
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    // delete
    @DeleteMapping("/player/{id}")
    void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }

}