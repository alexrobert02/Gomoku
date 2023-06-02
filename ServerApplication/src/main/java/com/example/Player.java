package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;

class Player {
    private String name;
    private char symbol;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private Timer timer;
    private int timeRemaining;

    public Player(String name, char symbol, Socket socket) {
        this.name = name;
        this.symbol = symbol;
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error creating input/output streams for player: " + e.getMessage());
        }
        timer = new Timer();
        timeRemaining = 600; // 60 seconds by default
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public Socket getSocket() {
        return socket;
    }

    public void notifyMove(int row, int col) {
        output.println("MOVE " + row + " " + col);
    }

    public void notifyTurn() {
        output.println("TURN");
    }

    public void notifyWin(String name) {
        output.println("WINNER: " + name);
    }

    public void notifyLoss() {
        output.println("LOSS");
    }

    public void notifyDraw() {
        output.println("DRAW");
    }

    public void notifyTimeout() {
        output.println("TIME_UP");
    }

    public void remove() {
        try {
            // Close the player's socket
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing player's socket: " + e.getMessage());
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}