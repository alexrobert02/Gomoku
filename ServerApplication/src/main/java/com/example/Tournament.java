package com.example;

import com.example.DataBase.TournamentHistory;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    private String lobbyName;
    private List<ClientThread> clientThreads=new ArrayList<>();
    private TournamentHistory tournamentDb;
    private List<Player> tournamentPlayers=new ArrayList<>();
    private List<Player> tournamentWinners=new ArrayList<>();
    private List<Game> tournamentGames = new ArrayList<>();
    private boolean tournamentOver;
    private boolean tournamentStarted;
    private Game finalGame;
    private int numberOfPlayerForRound;

    public Tournament(TournamentHistory tournamentDb) {
        this.tournamentDb=tournamentDb;
        this.tournamentOver=false;
        this.tournamentStarted=false;
        this.numberOfPlayerForRound=8;
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

    public void setTournamentDb(TournamentHistory tournamentDb) {
        this.tournamentDb = tournamentDb;
    }

    public void setTournamentOver(boolean tournamentOver) {
        this.tournamentOver = tournamentOver;
    }

    public void setTournamentStarted(boolean tournamentStarted) {
        this.tournamentStarted = tournamentStarted;
    }

    public void setNumberOfPlayerForRound(int numberOfPlayerForRound) {
        this.numberOfPlayerForRound = numberOfPlayerForRound;
    }

    public boolean isTournamentOver() {
        return tournamentOver;
    }

    public boolean isTournamentStarted() {
        return tournamentStarted;
    }

    public int getNumberOfPlayerForRound() {
        return numberOfPlayerForRound;
    }

    public String getPlayerNames() {
        StringBuilder names = new StringBuilder();

        for (Player player : tournamentPlayers) {
            names.append(player.getName()).append(", ");
        }

        // Remove the trailing comma and space
        if (names.length() > 0) {
            names.setLength(names.length() - 2);
        }

        return names.toString();
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public void setClientThreads(List<ClientThread> clientThreads) {
        this.clientThreads = clientThreads;
    }
}
