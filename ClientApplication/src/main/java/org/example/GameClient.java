package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean gameIsOver = false;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        // Create a new GameClient instance and start the client
        GameClient gameClient = new GameClient("localhost", 8000);
        gameClient.start();
    }

    public void start() {
        try {
            // Connect to the server
            socket = new Socket(host, port);
            // Set up input and output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected to server at " + host + ":" + port);
            System.out.println("Available commands: create, join, move, exit");

            // Start a separate thread to listen for server responses
            Thread responseThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                        if (response.equals("WIN") || response.equals("LOSS") || response.equals("TIME_UP")) {
                            gameIsOver = true;
                            System.out.println("Type anything to exit");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading server response: " + e.getMessage());
                } finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        System.err.println("Error closing client connection: " + e.getMessage());
                    }
                }
            });
            responseThread.start();

            // Read input from the user and send it to the server
            Scanner scanner = new Scanner(System.in);
            while (!gameIsOver) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    gameIsOver = true;
                    break;
                }
                out.println(input);
            }

            System.exit(0); // Terminate the client application
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}