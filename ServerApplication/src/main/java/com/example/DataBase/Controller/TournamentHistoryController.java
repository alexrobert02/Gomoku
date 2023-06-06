package com.example.DataBase.Controller;

import com.example.DataBase.GameHistory;
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
//    @PutMapping("/{id}/update_players")
//    public TournamentHistory updatePlayerIds(@PathVariable Long id, @RequestBody List<Long> playerIds) {
//        TournamentHistory tournament = tournamentHistoryRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
//
//        // Verifică dacă numărul de ID-uri noi se potrivește cu numărul de jucători din turneu
//        if (playerIds.size() != 7) {
//            throw new IllegalArgumentException("The number of player IDs does not match the expected number of players");
//        }
//
//        // Actualizează ID-urile jucătorilor
//        tournament.setPlayer2Id(playerIds.get(1));
//        tournament.setPlayer3Id(playerIds.get(2));
//        tournament.setPlayer4Id(playerIds.get(3));
//        tournament.setPlayer5Id(playerIds.get(4));
//        tournament.setPlayer6Id(playerIds.get(5));
//        tournament.setPlayer7Id(playerIds.get(6));
//        tournament.setPlayer8Id(playerIds.get(7));
//
//        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
//    }

    @PutMapping("/{id}/player2Id")
    public TournamentHistory updatePlayer2Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer2Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player3Id")
    public TournamentHistory updatePlayer3Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer3Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player4Id")
    public TournamentHistory updatePlayer4Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer4Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player5Id")
    public TournamentHistory updatePlayer5Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer5Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player6Id")
    public TournamentHistory updatePlayer6Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer6Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player7Id")
    public TournamentHistory updatePlayer7Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer7Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/player8Id")
    public TournamentHistory updatePlayer8Id(@PathVariable Long id, @RequestParam Long playerId) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        tournament.setPlayer8Id(playerId); // Actualizează ID-ul jucătorului 2
        return tournamentHistoryRepository.save(tournament); // Salvează turneul actualizat
    }
    @PutMapping("/{id}/status")
    public TournamentHistory updatetournamentStatus(@PathVariable Long id, @RequestParam String status) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("tournament not found"));
        tournament.setStatus(status); // Update the tournament's status
        return tournamentHistoryRepository.save(tournament); // Save the updated tournament
    }
    @PutMapping("/{id}/result")
    public TournamentHistory updatetournamentResult(@PathVariable Long id, @RequestParam String result) {
        TournamentHistory tournament = tournamentHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("tournament not found"));
        tournament.setResult(result); // Update the tournament's status
        return tournamentHistoryRepository.save(tournament); // Save the updated tournament
    }
}
