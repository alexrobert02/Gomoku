package com.example;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String lobbyName;
    private List<Player> tournamentPlayers=new ArrayList<>();;
    private List<Player> tournamentWinners=new ArrayList<>();;
    private Game finalGame;

    public Tournament(String lobbyName, List<Player> tournamentPlayers) {
        this.lobbyName = lobbyName;
        this.tournamentPlayers = tournamentPlayers;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public List<Player> getTournamentPlayers() {
        return tournamentPlayers;
    }

    public List<Player> getTournamentWinners() {
        return tournamentWinners;
    }

    public Game getFinalGame() {
        return finalGame;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public void setTournamentPlayers(List<Player> tournamentPlayers) {
        this.tournamentPlayers = tournamentPlayers;
    }

    public void setTournamentWinners(List<Player> tournamentWinners) {
        this.tournamentWinners = tournamentWinners;
    }

    public void setFinalGame(Game finalGame) {
        this.finalGame = finalGame;
    }

}
