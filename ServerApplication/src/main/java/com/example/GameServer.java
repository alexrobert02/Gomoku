package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Connection;

import com.example.DataBase.DataBasePlayer;
import com.example.DataBase.GameHistory;
import com.example.DataBase.TournamentHistory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GameServer {
    private int port;
    private RestTemplate restTemplate = new RestTemplate();
    private ServerSocket serverSocket;
    private boolean running;
    private Map<String, Game> games;
    private Map<String, Tournament> tournaments;
    private boolean tournamentInProgress;
    private String type;
    private List<ClientThread> clientThreads;

    public GameServer() {
        this.port = 8095;
        this.running = true;
        this.games = new HashMap<>();
        this.clientThreads = new ArrayList<>();
        this.tournaments= new HashMap<>();
        this.tournamentInProgress=false;
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
            //stop();
            //System.out.println("Server has been closed.");
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
                if(gameDb ==null || gameDb.getStatus().equals("stopped")) {
                    Game game = new Game(gameDb);
                    game.setLobbyName(lobbyName);
                    // Create a new player and add them to the game
                    Player player = new Player(parts[2], 'X', clientThread.getClientSocket());
                    /**
                     * adaugam playerul in baza de date
                     */
                    DataBasePlayer dbPlayer = new DataBasePlayer(player.getName());
                    DataBasePlayer addedPlayer = restTemplate.postForObject("http://localhost:8000/api/players", dbPlayer, DataBasePlayer.class);
                    System.out.println("Added Player: " + addedPlayer.getName()+addedPlayer.getId());
                    broadcastMessage(game, "Added player" + addedPlayer.getName());
                    //triggerToInsertPlayerToDatabase(dbPlayer);
                    /**
                     * adaugam jocul creat in baza de date
                     */
                    DataBasePlayer dbPlayerForId=findPlayerByNameInDb(dbPlayer.getName());
                    GameHistory dbGame = new GameHistory(lobbyName, dbPlayerForId.getId(),null, LocalDateTime.now(),null,"running");
                    GameHistory addedGame = restTemplate.postForObject("http://localhost:8000/api/game-history", dbGame, GameHistory.class);
                    System.out.println("Added Game: " + addedGame.getId());
                    boolean joined = game.join(player);
                    if (joined) {
                        games.put(lobbyName, game);
                        clientThread.setGame(game);

                        // Send confirmation message to the client
                        clientThread.sendResponse("GAME_CREATED");
                        System.out.println("Player " + player.getName() + " created the game: " + player.getSymbol());
                        broadcastMessage(game, "Player " + player.getName() + " created the game: " + player.getSymbol());
                    }
                }
                else {
                    clientThread.sendResponse("NAME_TAKEN");
                }
            } else {
                clientThread.sendResponse("GAME_UNAVAILABLE");
            }
        }
        else if (action.equalsIgnoreCase("create_tournament")) {
            if (clientThread.getGame() == null) {
                // Set the lobby name
                String lobbyName = parts[1];
                if (tournamentInProgress) {
                    System.out.println("Server is busy with another tournement!");
                    return; // Încheiem executarea metodei, deoarece nu putem crea un nou turneu
                }
                tournamentInProgress=true;
                Player player = new Player(parts[2], 'X', clientThread.getClientSocket());
                // adaugam playerii in baza de date
                DataBasePlayer dbPlayer = new DataBasePlayer(player.getName());
                DataBasePlayer addedPlayer = restTemplate.postForObject("http://localhost:8000/api/players", dbPlayer, DataBasePlayer.class);
                System.out.println("Added Player: " + addedPlayer.getName());
                // adaugam turneul in baza de date
                TournamentHistory tournamentDb = new TournamentHistory(lobbyName, addedPlayer.getId(), LocalDateTime.now(), null, "running");
                restTemplate.postForObject("http://localhost:8000/api/tournament-history", tournamentDb, TournamentHistory.class);
                // creem turneul
                Tournament tournament = new Tournament(tournamentDb);
                tournament.setLobbyName(lobbyName);
                // Adăugați turneul în lista de turnee a serverului
                tournaments.put(lobbyName,tournament);
                // adaugam playerii la lista de jucatori a turneului
                tournament.getTournamentPlayers().add(player);
                clientThread.sendResponse("TOURNAMENT_CREATED");
                System.out.println("Player " + player.getName() + " joined the game: " + player.getSymbol());

            }
            else {
                clientThread.sendResponse("TOURNAMENT_UNAVAILABLE");
            }
        }
        else if (action.equalsIgnoreCase("join_tournament")) {
            if (clientThread.getGame() == null) {
                String lobbyName = parts[1];
                // Find the tournament in database
                TournamentHistory tournamentDb = findTournamentByLobbyNameInDb(lobbyName);
                Tournament tournament = findTournamentByLobbyName(lobbyName);
                if (tournamentDb != null && tournament.getTournamentPlayers().size() < 8) {
                    // Create a new player and add them to the game
                    Player player = new Player(parts[2], 'O', clientThread.getClientSocket());
                    tournament.getTournamentPlayers().add(player);
                    DataBasePlayer dbPlayer = new DataBasePlayer(player.getName());
                    DataBasePlayer addedPlayer = restTemplate.postForObject("http://localhost:8000/api/players", dbPlayer, DataBasePlayer.class);
                    System.out.println("Added Player: " + addedPlayer.getName());
                    clientThread.sendResponse("TOURNAMENT_JOINED");
                    //System.out.println(tournament.getTournamentPlayers().size());
                    if (tournament.getTournamentPlayers().size()==8) {
                        // Start the tournament once the players have joined
                        createTournament(tournament);
                    }
                } else {
                    clientThread.sendResponse("TOURNAMENT_UNAVAILABLE");
                }

            }
            else {
                clientThread.sendResponse("TOURNAMENT_UNAVAILABLE");
            }

        }

        else if (action.equalsIgnoreCase("join")) {
            if (clientThread.getGame() == null) {
                String lobbyName = parts[1];
                // Find the game with the specified lobby name
                GameHistory gameDb = findGameByLobbyNameInDb(lobbyName);
                Game game = findGameByLobbyName(lobbyName);

                /**
                 * verificam daca numele jocului(lobby-ul exista)
                 */

                if (gameDb != null && gameDb.getPlayer2Id() == null) {
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
                        //broadcastMessage(game, "Player " + player.getName() + " joined the game: " + player.getSymbol());
                        clientThread.sendResponse("Joined: " + game.getPlayerNames());
                        System.out.println("Joined: " + game.getPlayerNames());
                        for (ClientThread differentClientThread: clientThreads) {
                            if (differentClientThread.getGame() == game && differentClientThread != clientThread) {
                                differentClientThread.sendResponse("Second player: " + player.getName());
                                System.out.println("Second player: " + player.getName());
                            }
                        }

//                        if (game.isFull()) {
//                            // Start the game once both players have joined
//                            game.start();
//                            Player currentPlayer = game.getCurrentPlayer();
//                            System.out.println("Game started! It's " + currentPlayer.getName() + "'s turn.");
//                            broadcastMessage(game, "Game started! It's " + currentPlayer.getName() + "'s turn.");
//                            currentPlayer.notifyTurn();
//                        }
                    } else {
                        clientThread.sendResponse("NAME_TAKEN");
                    }
                } else {
                    clientThread.sendResponse("GAME_NOT_FOUND");
                }
            } else {
                clientThread.sendResponse("GAME_UNAVAILABLE");
            }
        }
        else if (action.equalsIgnoreCase("START")) {
            if (clientThread.getGame() != null && !clientThread.getGame().isStarted()) {
                Game game = clientThread.getGame();
                game.start();
                Player currentPlayer = game.getCurrentPlayer();
                System.out.println("Game started! It's " + currentPlayer.getName() + "'s turn.");
                broadcastMessage(game, "Game started! It's " + currentPlayer.getName() + "'s turn.");
                currentPlayer.notifyTurn();
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
                    broadcastMessage(game, "Player " + player.getName() + " made a move at (" + row + ", " + col + ")");
                    game.makeMove(player, row, col);
                    if (game.isGameOver()) {
                        System.out.println("gameover");
                        GameHistory gameDb=findGameByLobbyNameInDb(game.getLobbyName());
                        restTemplate.put("http://localhost:8000/api/game-history/{id}/status?status={status}", null,gameDb.getId(),"stopped");
                        exportDataToCSV();
                        setGameToNullToAllClientThreads(game);
                        games.remove(game);
                        //clientThread.sendResponse("EXIT");
                        //stop();
                        // luam castigatorul si il punem in lista de castigatori a turneului
                        System.out.println(tournamentInProgress);
                        if(tournamentInProgress==true)
                        {
                            for(Tournament findTournament:tournaments.values())
                            {
                                System.out.println(findTournament.getLobbyName()+ findTournament.isTournamentOver());
                                if(findTournament.isTournamentOver()==false)
                                {
                                    findTournament.getTournamentWinners().add(game.getWinner());
                                    System.out.println(findTournament.getTournamentWinners().size());
                                    if(findTournament.getNumberOfPlayerForRound()==findTournament.getTournamentWinners().size())
                                    {
                                        System.out.println("incepe runda doi");
                                        newRound(findTournament);
                                    }
                                }
                                // verificam daca s-a terminat turneul
                                if(findTournament.getTournamentGames().size()==1)
                                {
                                    System.out.println("tournament over");
                                    tournamentInProgress=false;
                                    findTournament.setFinalGame(game);
                                }

                            }
                        }
                    }
                } else {
                    // it's not the current player's turn, send an error message
                    clientThread.sendResponse("NOT_YOUR_TURN");
                }
            }
        } else if (action.equalsIgnoreCase("exit")) {
            System.out.println("aici");
            // Handle client exit
            Game game = clientThread.getGame();
            Player player = game.getPlayerBySocket(clientThread.getClientSocket());
            DataBasePlayer dbplayer =findPlayerByNameInDb(player.getName());
            restTemplate.delete("http://localhost:8095/api/players/{id}", dbplayer.getId());
            if (player != null) {
                game.removePlayer(player);
                System.out.println("Player " + player.getName() + " has left the game");
                broadcastMessage(game, "Player " + player.getName() + " has left the game");
            }
            //clientThread.sendResponse("EXIT");
            //clientThread.close();
            //clientThreads.remove(clientThread);

            // Check if all clients have received the exit command
            //if (clientThreads.isEmpty()) {
            // stop(); // Close the server if all clients have exited
            //}
        }
    }


    // Helper method to broadcast a message to all connected clients
    private void broadcastMessage(Game game, String message) {
        for (ClientThread clientThread : clientThreads) {
            if (clientThread.getGame() == game) {
                clientThread.sendResponse(message);
            }
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
    private TournamentHistory findTournamentByLobbyNameInDb(String lobbyName) {
        TournamentHistory[] tournamentHistories = restTemplate.getForObject("http://localhost:8000/api/tournament-history", TournamentHistory[].class);
        for (TournamentHistory history : tournamentHistories) {
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
    private Tournament findTournamentByLobbyName(String lobbyName) {
        for (Tournament tournament : tournaments.values()) {
            if (tournament.getLobbyName().equals(lobbyName)) {
                return tournament;
            }
        }
        return null; // Game not found
    }
    private DataBasePlayer findPlayerByNameInDb(String Name) {
        DataBasePlayer[] dataBasePlayers = restTemplate.getForObject("http://localhost:8000/api/players", DataBasePlayer[].class);
        for (DataBasePlayer player : dataBasePlayers) {
            if(player.getName().equals(Name))
            {
                return player;
            }
        }
        return null; // player not found
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

    public void exportDataToCSV() {
        String url = "jdbc:postgresql://localhost:5432/Gomoku";
        String username = "postgres";
        String password = "password";

        try {
            // Establish a connection to the PostgreSQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Call the stored procedure
            CallableStatement statement = connection.prepareCall("CALL export_data()");
            statement.execute();

            // Close the statement and connection
            statement.close();
            connection.close();

            System.out.println("Data exported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // trigger
    private void triggerToInsertPlayerToDatabase(DataBasePlayer player) {
        String url = "jdbc:postgresql://localhost:5432/Gomoku";
        String username = "postgres";
        String password = "password";

        try {
            // Establish a connection to the PostgreSQL database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Prepare the SQL statement for inserting a player
            String sql = "INSERT INTO player (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the player name as a parameter
            statement.setString(1, player.getName());

            // Execute the SQL statement
            statement.executeUpdate();

            // Close the statement and connection
            statement.close();
            connection.close();

            System.out.println("Player inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void createTournament(Tournament tournament) {
        // Verificam daca s-au conectat toti playerii
        if(tournament.getTournamentPlayers().size()==8)
        {
            // setam jucatorii pentru urmatoarea runda
            tournament.setNumberOfPlayerForRound(4);
            //identificam id-urile jucatorilor
            long id1=0,id2=0,id3=0,id4=0,id5=0,id6=0,id7=0,id8=0;
            for(int i=0;i<tournament.getTournamentPlayers().size();i++)
            {
                long id=findPlayerByNameInDb(tournament.getTournamentPlayers().get(i).getName()).getId();
                switch (i){
                    case 0: id1=id;
                    case 1: id2=id;
                    case 2: id3=id;
                    case 3: id4=id;
                    case 4: id5=id;
                    case 5: id6=id;
                    case 6: id7=id;
                    case 7: id8=id;
                }
            }
            List<Long> playerIds = Arrays.asList(id1, id2, id3, id4, id5, id6, id7, id8);
            long tournementId=findTournamentByLobbyNameInDb(tournament.getLobbyName()).getId();
            // adaugam id-ul jucatorilor in baza de date a turneului
            // restTemplate.put("http://localhost:8000/api/tournament-history/{id}/update-players", null, tournementId,playerIds);
            // Generam perechi aleatorii de jucători
            while (!tournament.getTournamentPlayers().isEmpty()) {
                int index1 = (int) (Math.random() * tournament.getTournamentPlayers().size());
                Player player1 = tournament.getTournamentPlayers().remove(index1);

                int index2 = (int) (Math.random() * tournament.getTournamentPlayers().size());
                Player player2 = tournament.getTournamentPlayers().remove(index2);

                // Creem jocul pentru perechea de jucători si il adaugam in baza de date
                String[] words = {"apple", "banana", "orange", "kiwi", "grape"};
                Random random = new Random();
                String randomName = words[random.nextInt(words.length)] + random.nextInt(100);
                GameHistory gameDb= findGameByLobbyNameInDb(randomName);
                if(gameDb ==null || gameDb.getStatus().equals("stopped")) {
                    Game game = new Game(gameDb);
                    //Adaugam jocul in baza de date
                    DataBasePlayer dbPlayerForId=findPlayerByNameInDb(player1.getName());
                    GameHistory dbGame = new GameHistory(randomName, dbPlayerForId.getId(),null, LocalDateTime.now(),null,"running");
                    GameHistory addedGame = restTemplate.postForObject("http://localhost:8000/api/game-history", dbGame, GameHistory.class);
                    restTemplate.put("http://localhost:8000/api/game-history/{id}?playerId={playerId}", null, addedGame.getId(), findPlayerByNameInDb(player2.getName()).getId());
                    System.out.println("Added Game: " + addedGame.getId());
                    // repartizam cate un simbol playerilor
                    player1.setSymbol('X');
                    player2.setSymbol('O');
                    // bagam playerii in joc
                    game.setLobbyName(randomName);
                    game.join(player1);
                    System.out.println("Player " + player1.getName() + " joined the game: " + player1.getSymbol());
                    broadcastMessage(game, "Player " + player2.getName() + " joined the game: " + player2.getSymbol());
                    game.join(player2);
                    System.out.println("Player " + player1.getName() + " joined the game: " + player1.getSymbol());
                    broadcastMessage(game, "Player " + player2.getName() + " joined the game: " + player2.getSymbol());
                    // Adăugam jocul la lista de jocuri din turneu
                    tournament.getTournamentGames().add(game);
                    //game.start();
                }
                for(Game game:tournament.getTournamentGames())
                {
                    game.start();
                    Player currentPlayer = game.getCurrentPlayer();
                    System.out.println("Game started! It's " + currentPlayer.getName() + "'s turn.");
                    broadcastMessage(game, "Game started! It's " + currentPlayer.getName() + "'s turn.");
                    currentPlayer.notifyTurn();
                }
            }
        }
    }

    public void setGameToNullToAllClientThreads(Game game) {
        for (ClientThread clientThread : clientThreads) {
            if (clientThread.getGame() == game) {
                clientThread.setGame(null);
            }
        }
    }
    public void newRound(Tournament tournament)
    {
        if(tournament.getTournamentWinners().size()==tournament.getNumberOfPlayerForRound()) {

            // Obținem câștigătorii din cele patru jocuri inițiale
            for (Game game : tournament.getTournamentGames()) {
                Player winner = game.getWinner();
                if (winner != null) {
                    tournament.getTournamentWinners().add(winner);
                }
                tournament.getTournamentGames().remove(game);
            }

            // Amestecăm lista de câștigători
            Collections.shuffle(tournament.getTournamentWinners());

            // Împărțim câștigătorii în perechi și îi adăugăm în jocurile noi
            for (int i = 0; i < tournament.getTournamentWinners().size(); i += 2) {
                Player player1 = tournament.getTournamentWinners().get(i);
                Player player2 = tournament.getTournamentWinners().get(i + 1);
            // creem jocurile pentru o noua runda si le adaugam in baza de date
                String[] words = {"apple", "banana", "orange", "kiwi", "grape"};
                Random random = new Random();
                String randomName = words[random.nextInt(words.length)] + random.nextInt(100);
                GameHistory gameDb= findGameByLobbyNameInDb(randomName);
                if(gameDb ==null || gameDb.getStatus().equals("stopped"))
                {
                    Game game = new Game(gameDb);
                    //Adaugam jocul in baza de date
                    DataBasePlayer dbPlayerForId=findPlayerByNameInDb(player1.getName());
                    GameHistory dbGame = new GameHistory(randomName, dbPlayerForId.getId(),null, LocalDateTime.now(),null,"running");
                    GameHistory addedGame = restTemplate.postForObject("http://localhost:8000/api/game-history", dbGame, GameHistory.class);
                    restTemplate.put("http://localhost:8000/api/game-history/{id}?playerId={playerId}", null, addedGame.getId(), findPlayerByNameInDb(player2.getName()).getId());
                    System.out.println("Added Game: " + addedGame.getId());
                    // repartizam cate un simbol playerilor
                    player1.setSymbol('X');
                    player2.setSymbol('O');
                    // bagam playerii in joc
                    game.setLobbyName(randomName);
                    game.join(player1);
                    System.out.println("Player " + player1.getName() + " joined the game: " + player1.getSymbol());
                    broadcastMessage(game, "Player " + player2.getName() + " joined the game: " + player2.getSymbol());
                    game.join(player2);
                    System.out.println("Player " + player1.getName() + " joined the game: " + player1.getSymbol());
                    broadcastMessage(game, "Player " + player2.getName() + " joined the game: " + player2.getSymbol());
                    // Adăugam jocul la lista de jocuri din turneu
                    tournament.getTournamentGames().add(game);
                }
            }

            // Setăm starea jocurilor noi ca fiind "running"
            for (Game game : games.values()) {
                game.start();
            }
            // setam jucatorii pentru urmatoarea runda
            tournament.setNumberOfPlayerForRound(2);
        }
    }

}
