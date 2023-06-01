package com.example;

import com.example.DataBase.TournamentHistory;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String lobbyName;
    private TournamentHistory tournamentDb;
    private List<Player> tournamentPlayers=new ArrayList<>();
    private List<Player> tournamentWinners=new ArrayList<>();
    private List<Game> tournamentGames = new ArrayList<>();
    private boolean tournamentOver;
    private boolean tournamentStarted;
    private Game finalGame;

    public Tournament(TournamentHistory tournamentDb) {
        this.tournamentDb=tournamentDb;
        this.tournamentOver=false;
        this.tournamentStarted=false;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public TournamentHistory getTournamentDb() {
        return tournamentDb;
    }

    public List<Game> getTournamentGames() {
        return tournamentGames;
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

    public void setTournamentGames(List<Game> tournamentGames) {
        this.tournamentGames = tournamentGames;
    }
}
