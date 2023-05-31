package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient extends Application {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean gameIsOver = false;
    private GameClientUI ui;
    private String username;
    private String command;

    public GameClient() {
        this.host = "localhost";
        this.port = 8095;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ui = new GameClientUI(this);
        ui.setLoggedIn(false);
        ui.start(primaryStage);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void startGame() {
        ui.setLoggedIn(true);
        ui.showGameScene();
        connectToServer();
    }

    public void connectToServer() {
        try {
            // Connect to the server
            socket = new Socket(host, port);
            // Set up input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            ui.appendMessage("Connected to server at " + host + ":" + port);
            ui.appendMessage("Available commands: create, join, move, exit");

            // Start a separate thread to listen for server responses
            Thread responseThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        ui.appendMessage(response);
                        if (response.equals ("TURN")) {
                            ui.notifyTurn();
                        }
                        if (response.contains("made a move at")) {
                            ui.drawOpponentMove(response);
                        }
                        if (response.equals ("WIN") || response.equals("LOSS")) {
                            ui.showWinnerPage();
                        }
//                        if (response.equals("WIN") || response.equals("LOSS") || response.equals("TIME_UP")) {
//                            gameIsOver = true;
//                            ui.appendMessage("Type anything to exit");
//                            break;
//                        }
                    }
                } catch (IOException e) {
                    ui.appendMessage("Error reading server response: " + e.getMessage());
                } finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        ui.appendMessage("Error closing client connection: " + e.getMessage());
                    }
                }
            });
            responseThread.start();
        } catch (IOException e) {
            ui.appendMessage("Error connecting to server: " + e.getMessage());
        }
    }

    public void sendMessage() {
        if (!gameIsOver) {
            String message = ui.getInputText();
//            if (message.equalsIgnoreCase("exit")) {
//                gameIsOver = true;
//            }
            out.println(message);
            ui.clearInputField();
        }
    }

    public void sendMessageToServer(String message) {
        out.println(message);
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
