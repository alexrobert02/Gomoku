package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private GameServer server;
    private Game game;
    private Tournament tournament;
    private Player clientPlayer;

    public ClientThread(Socket clientSocket, GameServer server) {
        // Initializes the client socket, input/output streams, and server instance
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error creating input/output streams for client socket: " + e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void sendResponse(String response) {
        out.println(response);
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String command;
            while ((command = in.readLine()) != null) {
                // Continuously read input from client and handle commands
                server.handleCommand(this, command);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (in != null) {
                // Close the input stream
                in.close();
            }
            if (out != null) {
                // Close the output stream
                out.close();
            }
            if (clientSocket != null) {
                // Close the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    public void setClientPlayer(Player clientPlayer) {
        this.clientPlayer = clientPlayer;
    }

}

