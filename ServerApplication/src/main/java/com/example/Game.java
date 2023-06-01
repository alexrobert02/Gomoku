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
    private int timeLimitSeconds;
    private Timer turnTimer;
    private int currentPlayerTimeRemaining;
    private long turnStartTime;
    private Player winner;

    public Game(GameHistory gameDb) {
        board = new Board();
        players = new Player[2];
        currentPlayerIndex = 0;
        gameOver = false;
        gameStarted = false;
        timeLimitSeconds = 1000; // 60 seconds by default
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

                // Calculate the remaining time for the next player's turn
                long elapsedTime = System.currentTimeMillis() - turnStartTime;
                currentPlayerTimeRemaining = players[currentPlayerIndex].getTimeRemaining() - (int) (elapsedTime / 1000);
                if (currentPlayerTimeRemaining <= 0) {
                    players[currentPlayerIndex].notifyTimeout();
                    players[1 - currentPlayerIndex].notifyWin(players[1 - currentPlayerIndex].getName());
                    gameOver = true;
                } else {
                    players[currentPlayerIndex].notifyTurn();
                }

                // Update the turn start time
                turnStartTime = System.currentTimeMillis();
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

        // Set initial turn start time
        turnStartTime = System.currentTimeMillis();

        // Start the turn-based timer
        turnTimer = new Timer();
        currentPlayerTimeRemaining = players[currentPlayerIndex].getTimeRemaining();
        turnTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentPlayerTimeRemaining--;

                // Check if the current player's time has run out
                if (currentPlayerTimeRemaining <= 0) {
                    players[currentPlayerIndex].notifyTimeout();
                    players[1 - currentPlayerIndex].notifyWin(players[1 - currentPlayerIndex].getName());
                    gameOver = true;
                }
            }
        }, 1000, 1000); // Decrease currentPlayerTimeRemaining every second
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

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public Timer getTurnTimer() {
        return turnTimer;
    }

    public int getCurrentPlayerTimeRemaining() {
        return currentPlayerTimeRemaining;
    }

    public long getTurnStartTime() {
        return turnStartTime;
    }
    public Player getWinner()
    {
        return winner;
    }
}