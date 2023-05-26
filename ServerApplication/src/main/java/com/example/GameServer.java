package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GameServer {
    private int port;
    private RestTemplate restTemplate = new RestTemplate();
    private ServerSocket serverSocket;
    private boolean running;
    private Map<String, Game> games;
    private List<ClientThread> clientThreads;

    public GameServer() {
        this.port = 8095;
        this.running = true;
        this.games = new HashMap<>();
        this.clientThreads = new ArrayList<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);

            // Server starts listening on the specified port
            System.out.println("Game server started on port " + port);
            while (running) {
                // Accepts incoming client connection requests
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Starts a new thread to handle client communication
                ClientThread clientThread = new ClientThread(clientSocket, this);
                clientThreads.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Error starting server on port " + port + ": " + e.getMessage());
            }
        } finally {
            stop();
            System.out.println("Server has been closed.");
        }
    }

    public synchronized void handleCommand(ClientThread clientThread, String command) {
        // Parse the command received from the client
        String[] parts = command.split(" ");
        String action = parts[0];

        if (action.equalsIgnoreCase("create")) {
            if (clientThread.getGame() == null) {
                // Set the lobby name
                String lobbyName = parts[1];
                /**
                 * verificam daca numele jocului(lobby-ul exista deja)
                 */
                GameHistory gameDb= findGameByLobbyNameInDb(lobbyName);
                if(gameDb ==null) {
                    Game game = new Game();
                    game.setLobbyName(lobbyName);
                    // Create a new player and add them to the game
                    Player player = new Player(parts[2], 'X', clientThread.getClientSocket());
                    /**
                     * adaugam playerul in baza de date
                     */
                    DataBasePlayer dbPlayer = new DataBasePlayer(player.getName());
                    DataBasePlayer addedPlayer = restTemplate.postForObject("http://localhost:8000/api/players", dbPlayer, DataBasePlayer.class);
                    System.out.println("Added Player: " + addedPlayer.getName()+addedPlayer.getId());
                    /**
                     * adaugam jocul creat in baza de date
                     */
                    GameHistory dbGame = new GameHistory(lobbyName,addedPlayer.getId(),null, LocalDateTime.now(),null,"running");
                    GameHistory addedGame = restTemplate.postForObject("http://localhost:8000/api/game-history", dbGame, GameHistory.class);
                    System.out.println("Added Game: " + addedGame.getId());
                    boolean joined = game.join(player);
                    if (joined) {
                        games.put(lobbyName, game);
                        clientThread.setGame(game);

                        // Send confirmation message to the client
                        clientThread.sendResponse("GAME_CREATED");
                        System.out.println("Player " + player.getName() + " joined the game: " + player.getSymbol());
                        broadcastMessage("Player " + player.getName() + " joined the game: " + player.getSymbol());
                    }
                }
                else {
                    clientThread.sendResponse("NAME_TAKEN");
                }
            } else {
                clientThread.sendResponse("GAME_UNAVAILABLE");
            }
        } else if (action.equalsIgnoreCase("join")) {
            if (clientThread.getGame() == null) {
                String lobbyName = parts[1];
                // Find the game with the specified lobby name
               GameHistory gameDb = findGameByLobbyNameInDb(lobbyName);
               Game game =findGameByLobbyName(lobbyName);

                /**
                 * verificam daca numele jocului(lobby-ul exista)
                 */

                    if (gameDb!=null && gameDb.getPlayer2Id()==null) {
                    // Create a new player and add them to the game
                    Player player = new Player(parts[2], 'O', clientThread.getClientSocket());
                    DataBasePlayer dbPlayer = new DataBasePlayer(player.getName());
                    DataBasePlayer addedPlayer = restTemplate.postForObject("http://localhost:8000/api/players", dbPlayer, DataBasePlayer.class);
                    System.out.println("Added Player: " + addedPlayer.getName());
                        /**
                         * modificam in baza de date id-ul celui de al doilea jucator cu id-ul jucatorului curent
                         */
                    restTemplate.put("http://localhost:8000/api/game-history/{id}?playerId={playerId}", null, gameDb.getId(), addedPlayer.getId());
                    boolean joined = game.join(player);

                    if (joined) {
                        clientThread.setGame(game);
                        clientThread.sendResponse("GAME_JOINED");
                        // Send confirmation message to the client
                        System.out.println("Player " + player.getName() + " joined the game: " + player.getSymbol());
                        broadcastMessage("Player " + player.getName() + " joined the game: " + player.getSymbol());

                        if (game.isFull()) {
                            // Start the game once both players have joined
                            game.start();
                            Player currentPlayer = game.getCurrentPlayer();
                            System.out.println("Game started! It's " + currentPlayer.getName() + "'s turn.");
                            broadcastMessage("Game started! It's " + currentPlayer.getName() + "'s turn.");
                        }
                    } else {
                        clientThread.sendResponse("NAME_TAKEN");
                    }
                } else {
                    clientThread.sendResponse("GAME_NOT_FOUND");
                }
            } else {
                clientThread.sendResponse("GAME_UNAVAILABLE");
            }
        } else if (action.equalsIgnoreCase("move")) {
            if (clientThread.getGame() != null && clientThread.getGame().isStarted()) {
                Game game = clientThread.getGame();
                Player player = game.getCurrentPlayer();

                // Check if it's the current player's turn
                if (player.getSocket() == clientThread.getClientSocket()) {
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);
                    System.out.println("Player " + player.getName() + " made a move at (" + row + ", " + col + ")");
                    broadcastMessage("Player " + player.getName() + " made a move at (" + row + ", " + col + ")");
                    game.makeMove(player, row, col);
                    if (game.isGameOver()) {
                        clientThread.sendResponse("EXIT");
                        stop();
                    }
                } else {
                    // it's not the current player's turn, send an error message
                    clientThread.sendResponse("NOT_YOUR_TURN");
                }
            }
        } else if (action.equalsIgnoreCase("exit")) {
            // Handle client exit
            Game game = clientThread.getGame();
            Player player = game.getPlayerBySocket(clientThread.getClientSocket());
            if (player != null) {
                game.removePlayer(player);
                System.out.println("Player " + player.getName() + " has left the game");
                broadcastMessage("Player " + player.getName() + " has left the game");
            }
            clientThread.sendResponse("EXIT");
            clientThread.close();
            clientThreads.remove(clientThread);

            // Check if all clients have received the exit command
            if (clientThreads.isEmpty()) {
                stop(); // Close the server if all clients have exited
            }
        }
    }


    // Helper method to broadcast a message to all connected clients
    private void broadcastMessage(String message) {
        for (ClientThread clientThread : clientThreads) {
            clientThread.sendResponse(message);
            try {
                Thread.sleep(10); // Introduce a small delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to find a game by lobby name

    private GameHistory findGameByLobbyNameInDb(String lobbyName) {
        GameHistory[] gameHistories = restTemplate.getForObject("http://localhost:8000/api/game-history", GameHistory[].class);
        for (GameHistory history : gameHistories) {
            //System.out.println("Game History Result: " + history.getName());
            if(history.getName().equals(lobbyName))
            {
                return history;
            }
        }
        return null; // Game not found
    }
    private Game findGameByLobbyName(String lobbyName) {
        for (Game game : games.values()) {
            if (game.getLobbyName().equals(lobbyName)) {
                return game;
            }
        }
        return null; // Game not found
    }


    public void stop() {
        // Stop the server
        running = false;
        try {
            if (serverSocket != null) {
                // Close the server socket
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server on port " + port + ": " + e.getMessage());
        }
    }

}

