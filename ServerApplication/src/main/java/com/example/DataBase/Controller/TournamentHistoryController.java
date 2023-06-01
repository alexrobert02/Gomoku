package com.example.DataBase.Controller;

import com.example.DataBase.TournamentHistory;
import com.example.DataBase.Repository.TournamentHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournament-history")
public class TournamentHistoryController {

    private final TournamentHistoryRepository tournamentHistoryRepository; // Repository for accessing tournament history data

    public TournamentHistoryController(TournamentHistoryRepository tournamentHistoryRepository) {
        this.tournamentHistoryRepository = tournamentHistoryRepository;
    }

    @GetMapping
    public List<TournamentHistory> getAlltournamentHistory() {
        return tournamentHistoryRepository.findAll(); // Retrieve all tournament history entries
    }

    @PostMapping
    public TournamentHistory addtournamentHistory(@RequestBody TournamentHistory tournamentHistory) {
        return tournamentHistoryRepository.save(tournamentHistory); // Add a new tournament history entry
    }
    @PutMapping("/{id}/update-players")
    public TournamentHistory updatePlayerIds(@PathVariable Long id, @RequestBody List<Long> playerIds) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        // Verifică dacă numărul de ID-uri noi se potrivește cu numărul de jucători din turneu
        if (playerIds.size() != 7) {
            throw new IllegalArgumentException("The number of player IDs does not match the expected number of players");
        }

        // Actualizează ID-urile jucătorilor
        tournament.setPlayer2Id(playerIds.get(1));
        tournament.setPlayer3Id(playerIds.get(2));
        tournament.setPlayer4Id(playerIds.get(3));
        tournament.setPlayer5Id(playerIds.get(4));
        tournament.setPlayer6Id(playerIds.get(5));
        tournament.setPlayer7Id(playerIds.get(6));
        tournament.setPlayer8Id(playerIds.get(7));

        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }

    @PutMapping("/{id}/status")
    public TournamentHistory updatetournamentStatus(@PathVariable Long id, @RequestParam String status) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("tournament not found"));
        tournament.setStatus(status); // Update the tournament's status
        return tournamentHistoryRepository.save(tournament); // Save the updated tournament
    }
}
