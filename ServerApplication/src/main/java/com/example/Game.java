package com.example;

import com.example.DataBase.GameHistory;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


class Game {
    private String lobbyName;
    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private boolean gameStarted;
    private GameHistory gameDb;
    private Player winner;
    private String timeLimit;

    public Game(GameHistory gameDb) {
        this.gameDb=gameDb;
        board = new Board();
        players = new Player[2];
        currentPlayerIndex = 0;
        gameOver = false;
        gameStarted = false;
    }

    private boolean isPlayerTurn(Player player) {
        return player == players[currentPlayerIndex];
    }

    public synchronized boolean join(Player player) {

        if (isPlayerNameTaken(player.getName())) {
            return false; // Player name is already taken
        }

        if (players[0] == null) {
            players[0] = player;
            return true;
        } else if (players[1] == null) {
            players[1] = player;
            notifyAll(); // Notify waiting players that the game can start
            return true;
        }
        return false;
    }

    private boolean isPlayerNameTaken(String playerName) {
        for (Player player : players) {
            if (player != null && player.getName().equalsIgnoreCase(playerName)) {
                return true; // Player name is already taken
            }
        }
        return false; // Player name is not taken
    }


    public synchronized boolean isFull() {
        return players[0] != null && players[1] != null;
    }

    public synchronized void makeMove(Player player, int row, int col) {
        if (!gameOver && isPlayerTurn(player) && board.isValidMove(row, col)) {
            board.makeMove(row, col, player.getSymbol());
            players[currentPlayerIndex].notifyMove(row, col);

            // Check if the move resulted in a win
            if (board.isWinningMove(row, col)) {
                players[currentPlayerIndex].notifyWin(players[currentPlayerIndex].getName());
                players[1 - currentPlayerIndex].notifyWin(players[currentPlayerIndex].getName());
                gameOver = true;
                winner=players[currentPlayerIndex];
            }
            // Check if the board is full and the game has ended in a draw
            else if (board.isFull()) {
                players[currentPlayerIndex].notifyDraw();
                players[1 - currentPlayerIndex].notifyDraw();
                gameOver = true;
            }
            // If the game is not over, switch to the other player's turn
            else {
                currentPlayerIndex = 1 - currentPlayerIndex;
                players[currentPlayerIndex].notifyTurn();
            }
        }
    }

    public synchronized boolean isAvailable() {
        return players[0] != null && players[1] == null;
    }

    public synchronized boolean isNotCreated() {
        return players[0] == null;
    }

    public synchronized void start() {
        // Wait until both players have joined
        while (!isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameStarted = true;
        notifyAll(); // Notify all waiting threads that the game has started
    }

    public synchronized boolean isGameOver() {
        return gameOver;
    }

    public synchronized boolean isStarted() {
        return gameStarted;
    }

    public synchronized Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public synchronized void removePlayer(Player player) {
        player.remove();
    }

    public synchronized Player getPlayerBySocket(Socket socket) {
        for (Player player : players) {
            if (player.getSocket() == socket) {
                return player;
            }
        }
        return null;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Board getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public Player getWinner()
    {
        return winner;
    }
    public String getPlayerNames() {
        StringBuilder names = new StringBuilder();

        for (Player player : players) {
            names.append(player.getName()).append(", ");
        }

        // Remove the trailing comma and space
        if (names.length() > 0) {
            names.setLength(names.length() - 2);
        }

        return names.toString();
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getTimeLimit() {
        return timeLimit;
    }
}