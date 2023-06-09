package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameClientUI extends Application {
    private Stage stage;
    private BorderPane root;
    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;
    private GameClient gameClient;
    private boolean loggedIn;
    private String username;
    private int playerIndex;
    private boolean playerTurn;
    private Label[][] gameBoardCells;
    private StringBuilder joinedPlayersContent;
    private Label joinedPlayersLabel;
    private Label player1TimerLabel;
    private Label player2TimerLabel;
    private long player1Timer;
    private long player2Timer;
    private TimerTask player1TimerTask;
    private TimerTask player2TimerTask;
    private Timer player1TimerObj;
    private Timer player2TimerObj;
    private Label player1TurnLabel;
    private Label player2TurnLabel;

    public GameClientUI(GameClient gameClient) {
        this.gameClient = gameClient;
        player1Timer = 60;
        player2Timer = 60;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setPlayer1Timer(long player1Timer) {
        this.player1Timer = player1Timer;
    }

    public void setPlayer2Timer(long player2Timer) {
        this.player2Timer = player2Timer;
    }

    public void appendMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }
    public void notifyTurn () {
        Platform.runLater(() -> playerTurn = true);
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.clear();
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void showLoginScene() {
        BorderPane loginRoot = new BorderPane();
        loginRoot.setPadding(new Insets(100, 100, 100, 100));

        TextField usernameField = new TextField();
        usernameField.setMaxWidth(200); // Set preferred width for the input field

        Button loginButton = new Button("Login");
        loginButton.setAlignment(Pos.CENTER);
        loginButton.setMinWidth(150); // Set minimum width for the button
        loginButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        loginButton.setTextFill(Color.WHITE); // Set button text color
        loginButton.setFont(Font.font(16));
        loginButton.setOnAction(e -> {
            gameClient.setUsername(usernameField.getText());
            loggedIn = true;
            username = usernameField.getText();
            startGame();
        });

        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getChildren().addAll(usernameField, loginButton);
        loginRoot.setCenter(loginBox);
        loginRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label usernameLabel = new Label("Insert your username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));
        usernameLabel.setTextFill(Color.BLACK);
        usernameLabel.setAlignment(Pos.CENTER);
        usernameLabel.setPadding(new Insets(50, 0, 0, 0));

        StackPane usernamePane = new StackPane(usernameLabel);
        usernamePane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        BorderPane.setAlignment(usernamePane, Pos.CENTER);
        BorderPane.setMargin(usernamePane, new Insets(50, 0, 0, 0));
        loginRoot.setTop(usernamePane);

        Scene loginScene = new Scene(loginRoot, 1000, 600);
        stage.setScene(loginScene);
        stage.show();
    }

    public void showGameSceneTest() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        chatArea = new TextArea();
        chatArea.setEditable(false);
        root.setCenter(chatArea);

        inputField = new TextField();
        inputField.setOnAction(e -> gameClient.sendMessage());
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> gameClient.sendMessage());

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(inputField, sendButton);
        root.setBottom(inputBox);
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(root, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
    }

    public void showGameScene() {
        root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        Text title = new Text("GOMOKU");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        title.setFill(Color.ORANGE); // Setează culoarea dorită pentru titlu

        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(title);
        titleBox.setPadding(new Insets(100, 0, 0, 0)); // Ajustează marginea superioară

        root.setTop(titleBox);


        chatArea = new TextArea();
        chatArea.setEditable(false);
        root.setBottom(chatArea);
        chatArea.setMaxHeight(50);

        VBox gameButtonsBox = new VBox(10); // VBox to hold the buttons
        gameButtonsBox.setAlignment(Pos.CENTER);
        gameButtonsBox.setPadding(new Insets(0,0,0,200));

        Button hostGameButton = new Button("Host game");
        hostGameButton.setAlignment(Pos.CENTER);
        hostGameButton.setMinWidth(200); // Set minimum width for the button
        hostGameButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        hostGameButton.setTextFill(Color.WHITE); // Set button text color
        hostGameButton.setFont(Font.font(20)); // Set font size for the button text
        hostGameButton.setOnAction(e -> showHostWindow());
        gameButtonsBox.getChildren().add(hostGameButton); // Add the "Host" button to the VBox

        Button joinGameButton = new Button("Join game");
        joinGameButton.setAlignment(Pos.CENTER);
        joinGameButton.setMinWidth(200); // Set minimum width for the button
        joinGameButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        joinGameButton.setTextFill(Color.WHITE); // Set button text color
        joinGameButton.setFont(Font.font(20)); // Set font size for the button text
        joinGameButton.setOnAction(e -> showJoinWindow());
        gameButtonsBox.getChildren().add(joinGameButton); // Add the "Join" button to the VBox

        root.setLeft(gameButtonsBox); // Set the VBox as the bottom of the BorderPane


        VBox tournamentButtonsBox = new VBox(10); // VBox to hold the buttons
        tournamentButtonsBox.setAlignment(Pos.CENTER);
        tournamentButtonsBox.setPadding(new Insets(0,200,0,0));

        Button hostTournamentButton = new Button("Host tournament");
        hostTournamentButton.setAlignment(Pos.CENTER);
        hostTournamentButton.setMinWidth(200); // Set minimum width for the button
        hostTournamentButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        hostTournamentButton.setTextFill(Color.WHITE); // Set button text color
        hostTournamentButton.setFont(Font.font(20)); // Set font size for the button text
        hostTournamentButton.setOnAction(e -> showHostTournamentWindow());
        tournamentButtonsBox.getChildren().add(hostTournamentButton); // Add the "Host" button to the VBox

        Button joinTournamentButton = new Button("Join tournament");
        joinTournamentButton.setAlignment(Pos.CENTER);
        joinTournamentButton.setMinWidth(200); // Set minimum width for the button
        joinTournamentButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        joinTournamentButton.setTextFill(Color.WHITE); // Set button text color
        joinTournamentButton.setFont(Font.font(20)); // Set font size for the button text
        joinTournamentButton.setOnAction(e -> showJoinTournamentWindow());
        tournamentButtonsBox.getChildren().add(joinTournamentButton); // Add the "Join" button to the VBox

        root.setRight(tournamentButtonsBox); // Set the VBox as the bottom of the BorderPane


        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(root, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
    }

    private void showJoinTournamentWindow() {
        BorderPane joinTournamentRoot = new BorderPane();
        //startRoot.setPadding(new Insets(10, 10, 10, 10));

        TextField lobbyNameField = new TextField();
        lobbyNameField.setMaxWidth(300); // Set preferred width for the input field

        // Create the arrow button
        Button backButton = new Button("←");
        backButton.setMinWidth(150);
        backButton.setFont(Font.font(20));
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(e -> {
            showGameScene();
        });

        // Apply CSS styles to center the arrow inside the button
        backButton.setStyle("-fx-alignment: center; -fx-content-display: center;");
        backButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        joinTournamentRoot.setTop(backButton);

        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);

        Label joinGameLabel = new Label("Insert tournament lobby:");
        joinGameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        joinGameLabel.setTextFill(Color.BLACK);
        joinGameLabel.setAlignment(Pos.CENTER);
        joinGameLabel.setPadding(new Insets(0, 0, 0, 0));



        joinedPlayersContent = new StringBuilder();
        joinedPlayersContent.append("JOINED PLAYERS\n");


        // Set the text content of the label
        joinedPlayersLabel = new Label(joinedPlayersContent.toString());
        joinedPlayersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        joinedPlayersLabel.setTextFill(Color.BLACK);
        joinedPlayersLabel.setAlignment(Pos.CENTER_LEFT);
        joinedPlayersLabel.setPadding(new Insets(50, 0, 50, 50));
        joinTournamentRoot.setLeft(joinedPlayersLabel);
        joinedPlayersLabel.setVisible(false);


        Label testLabel = new Label("JOINED PLAYERS");
        testLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        testLabel.setTextFill(Color.BLACK);
        testLabel.setAlignment(Pos.CENTER_RIGHT);
        testLabel.setPadding(new Insets(50,50,50,0));
        testLabel.setVisible(false);
        joinTournamentRoot.setRight(testLabel);

        Button joinButton = new Button("Join");
        joinButton.setMinWidth(400);
        joinButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        joinButton.setTextFill(Color.WHITE);
        joinButton.setFont(Font.font(20));
        joinButton.setOnAction(e -> {
            String lobbyName = lobbyNameField.getText();
            String command = ("join_tournament " + lobbyName + " " + username);
            gameClient.sendMessageToServer(command);
            //joinButton.setVisible(false);
            //playerIndex = 1;
            joinButton.setVisible(false);
            joinGameLabel.setVisible(false);
        });

        centerContainer.getChildren().addAll(joinGameLabel, lobbyNameField, joinButton);
        joinTournamentRoot.setCenter(centerContainer);

        joinTournamentRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //joinRoot.setTop(joinGamePane);

        Scene gameTournamentScene = new Scene(joinTournamentRoot, 1000, 600);
        stage.setScene(gameTournamentScene);
        stage.show();
    }

    private void showHostTournamentWindow() {
        Stage hostTournamentStage = new Stage();
        hostTournamentStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
        hostTournamentStage.setTitle("Create Tournament");

        BorderPane hostTournamentRoot = new BorderPane();
        hostTournamentRoot.setPadding(new Insets(10, 10, 10, 10));
        hostTournamentRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label gameDataLabel = new Label("Tournament Data");
        gameDataLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gameDataLabel.setTextFill(Color.BLACK);
        gameDataLabel.setPadding(new Insets(50,10,10,10));
        hostTournamentRoot.setTop(gameDataLabel);
        BorderPane.setAlignment(gameDataLabel, Pos.CENTER);

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        Label timeLimitLabel = new Label("Time limit (in seconds)");
        TextField timeLimitField = new TextField();
        timeLimitField.setPrefWidth(200);

        Label lobbyNameLabel = new Label("Lobby name ");
        TextField lobbyNameField = new TextField();
        lobbyNameField.setPrefWidth(200);

        Button createGameButton = new Button("CREATE GAME");
        createGameButton.setMinWidth(100); // Set minimum width for the button
        createGameButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        createGameButton.setTextFill(Color.WHITE); // Set button text color
        createGameButton.setFont(Font.font(15)); // Set font size for the button text
        createGameButton.setOnAction(e -> {
            String timeLimit = timeLimitField.getText();
            String lobbyName = lobbyNameField.getText();

            //Process the input and create the game
            if (timeLimitField.getText().isEmpty()) {
                player1Timer = 0;
                player2Timer = 0;
                timeLimit = "0";
            }
            else {
                player1Timer = Long.parseLong(timeLimitField.getText());
                player2Timer = Long.parseLong(timeLimitField.getText());
            }
            hostTournamentStage.close();
            String command = ("create_tournament " + lobbyName + " " + username + " " + timeLimit);
            gameClient.sendMessageToServer(command);
            //playerIndex = 0;
            showStartScene(lobbyName);
        });

        inputGrid.add(lobbyNameField, 0, 0);
        inputGrid.add(lobbyNameLabel, 1, 0);

        inputGrid.add(timeLimitField, 0, 1);
        inputGrid.add(timeLimitLabel, 1, 1);

        hostTournamentRoot.setCenter(inputGrid);
        hostTournamentRoot.setBottom(createGameButton);
        BorderPane.setAlignment(createGameButton, Pos.CENTER); // Align the button at the center

        Scene hostScene = new Scene(hostTournamentRoot, 400, 300);
        hostTournamentStage.setScene(hostScene);
        hostTournamentStage.showAndWait();
    }

    private void showJoinWindow() {
        BorderPane joinRoot = new BorderPane();
        //startRoot.setPadding(new Insets(10, 10, 10, 10));

        TextField lobbyNameField = new TextField();
        lobbyNameField.setMaxWidth(300); // Set preferred width for the input field

        // Create the arrow button
        Button backButton = new Button("←");
        backButton.setMinWidth(150);
        backButton.setFont(Font.font(20));
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(e -> {
            showGameScene();
        });

        // Apply CSS styles to center the arrow inside the button
        backButton.setStyle("-fx-alignment: center; -fx-content-display: center;");
        backButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        joinRoot.setTop(backButton);

        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);

        Label joinGameLabel = new Label("Insert game lobby:");
        joinGameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        joinGameLabel.setTextFill(Color.BLACK);
        joinGameLabel.setAlignment(Pos.CENTER);
        joinGameLabel.setPadding(new Insets(0, 0, 0, 0));



        joinedPlayersContent = new StringBuilder();
        joinedPlayersContent.append("JOINED PLAYERS\n");


        // Set the text content of the label
        joinedPlayersLabel = new Label(joinedPlayersContent.toString());
        joinedPlayersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        joinedPlayersLabel.setTextFill(Color.BLACK);
        joinedPlayersLabel.setAlignment(Pos.CENTER_LEFT);
        joinedPlayersLabel.setPadding(new Insets(50, 0, 50, 50));
        joinRoot.setLeft(joinedPlayersLabel);
        joinedPlayersLabel.setVisible(false);


        Label testLabel = new Label("JOINED PLAYERS");
        testLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        testLabel.setTextFill(Color.BLACK);
        testLabel.setAlignment(Pos.CENTER_RIGHT);
        testLabel.setPadding(new Insets(50,50,50,0));
        testLabel.setVisible(false);
        joinRoot.setRight(testLabel);

        Button joinButton = new Button("Join");
        joinButton.setMinWidth(400);
        joinButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        joinButton.setTextFill(Color.WHITE);
        joinButton.setFont(Font.font(20));
        joinButton.setOnAction(e -> {
            String lobbyName = lobbyNameField.getText();
            String command = ("join " + lobbyName + " " + username);
            gameClient.sendMessageToServer(command);
            //joinButton.setVisible(false);
            playerIndex = 1;
            joinButton.setVisible(false);
            joinGameLabel.setVisible(false);
        });

        centerContainer.getChildren().addAll(joinGameLabel, lobbyNameField, joinButton);
        joinRoot.setCenter(centerContainer);

        joinRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //joinRoot.setTop(joinGamePane);

        Scene gameScene = new Scene(joinRoot, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
    }

    public void showHostWindow() {
        Stage hostStage = new Stage();
        hostStage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
        hostStage.setTitle("Create Game");

        BorderPane hostRoot = new BorderPane();
        hostRoot.setPadding(new Insets(10, 10, 10, 10));
        hostRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label gameDataLabel = new Label("Game Data");
        gameDataLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gameDataLabel.setTextFill(Color.BLACK);
        gameDataLabel.setPadding(new Insets(50,10,10,10));
        hostRoot.setTop(gameDataLabel);
        BorderPane.setAlignment(gameDataLabel, Pos.CENTER);

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        Label timeLimitLabel = new Label("Time limit (in seconds)");
        TextField timeLimitField = new TextField();
        timeLimitField.setPrefWidth(200);

        Label lobbyNameLabel = new Label("Lobby name ");
        TextField lobbyNameField = new TextField();
        lobbyNameField.setPrefWidth(200);

        Button createGameButton = new Button("CREATE GAME");
        createGameButton.setMinWidth(100); // Set minimum width for the button
        createGameButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        createGameButton.setTextFill(Color.WHITE); // Set button text color
        createGameButton.setFont(Font.font(15)); // Set font size for the button text
        createGameButton.setOnAction(e -> {
            String timeLimit = timeLimitField.getText();
            String lobbyName = lobbyNameField.getText();

            //Process the input and create the game
            if (timeLimitField.getText().isEmpty()) {
                player1Timer = 60;
                player2Timer = 60;
                timeLimit = "60";
            }
            else {
                player1Timer = Long.parseLong(timeLimitField.getText());
                player2Timer = Long.parseLong(timeLimitField.getText());
            }
            hostStage.close();
            String command = ("create " + lobbyName + " " + username + " " + timeLimit);
            gameClient.sendMessageToServer(command);
            playerIndex = 0;
            showStartScene(lobbyName);
        });

        inputGrid.add(lobbyNameField, 0, 0);
        inputGrid.add(lobbyNameLabel, 1, 0);

        inputGrid.add(timeLimitField, 0, 1);
        inputGrid.add(timeLimitLabel, 1, 1);

        hostRoot.setCenter(inputGrid);
        hostRoot.setBottom(createGameButton);
        BorderPane.setAlignment(createGameButton, Pos.CENTER); // Align the button at the center

        Scene hostScene = new Scene(hostRoot, 400, 300);
        hostStage.setScene(hostScene);
        hostStage.showAndWait();
    }

    public void showStartScene(String lobbyName) {
        BorderPane startRoot = new BorderPane();

        // Create the arrow button and set it at the top-left corner
        Button backButton = new Button("←");
        backButton.setMinWidth(150);
        backButton.setFont(Font.font(20));
        backButton.setAlignment(Pos.TOP_LEFT);
        backButton.setOnAction(e -> {
            showGameScene();
        });
        backButton.setStyle("-fx-alignment: center; -fx-content-display: center;");
        backButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        startRoot.setTop(backButton);

        // Create a StringBuilder to construct the text content
        joinedPlayersContent = new StringBuilder();
        joinedPlayersContent.append("JOINED PLAYERS\n");
        joinedPlayersContent.append(username).append("\n");


        Platform.runLater(() -> {
            // Set the text content of the label
            joinedPlayersLabel = new Label(joinedPlayersContent.toString());
            joinedPlayersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            joinedPlayersLabel.setTextFill(Color.BLACK);
            joinedPlayersLabel.setAlignment(Pos.CENTER_LEFT);
            joinedPlayersLabel.setPadding(new Insets(50, 0, 50, 50));
            startRoot.setLeft(joinedPlayersLabel);
        });

        Label testLabel = new Label("JOINED PLAYERS");
        testLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        testLabel.setTextFill(Color.BLACK);
        testLabel.setAlignment(Pos.CENTER_RIGHT);
        testLabel.setPadding(new Insets(50,50,50,0));
        testLabel.setVisible(false);
        startRoot.setRight(testLabel);

        // Create a VBox to contain the center content
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setSpacing(20);
        centerVBox.maxWidth(400);

        // Add the elements to the VBox as needed
        Label gameLobbyLabel = new Label("Lobby name: " + lobbyName);
        gameLobbyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gameLobbyLabel.setTextFill(Color.BLACK);
        gameLobbyLabel.setPadding(new Insets(10, 0, 0, 0));
        centerVBox.getChildren().add(gameLobbyLabel);

        Button startButton = new Button("Start");
        startButton.setMinWidth(400);
        startButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setTextFill(Color.WHITE);
        startButton.setFont(Font.font(20));
        startButton.setOnAction(e -> {
            System.out.println("a trimis start");
            gameClient.sendMessageToServer("START");
        });
        centerVBox.getChildren().add(startButton);

        // Create a BorderPane to hold the left and center content
        BorderPane contentPane = new BorderPane();
        contentPane.setCenter(centerVBox);

        // Adjust alignment of the contentPane within the startRoot
        startRoot.setCenter(contentPane);
        BorderPane.setAlignment(contentPane, Pos.CENTER);

        startRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(startRoot, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
    }

    public void showGameBoardScene() {
        System.out.println("uite tabla");
        BorderPane gameBoardRoot = new BorderPane();
        gameBoardRoot.setPadding(new Insets(10, 10, 10, 10));
        gameBoardRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        // Game board dimensions
        int rows = 10;
        int columns = 10;

        // Create a GridPane to hold the game board cells
        GridPane gameBoardGrid = new GridPane();
        gameBoardGrid.setAlignment(Pos.CENTER);
        gameBoardGrid.setHgap(5);
        gameBoardGrid.setVgap(5);

        player1TimerLabel = new Label("Player 1 Timer: " + player1Timer);
        player2TimerLabel = new Label("Player 2 Timer: " + player2Timer);

        player1TurnLabel = new Label("YOUR TURN");
        player2TurnLabel = new Label("YOUR TURN");

        VBox player1Label = new VBox(player1TimerLabel, player1TurnLabel);
        VBox player2Label = new VBox(player2TimerLabel, player2TurnLabel);


        if(playerIndex == 1) {
            player1TurnLabel.setVisible(false);
        }
        player2TurnLabel.setVisible(false);

        gameBoardCells = new Label[rows][columns];
        // Add cells to the game board
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Create a label to represent a game board cell
                Label cellLabel = new Label();
                gameBoardCells[row][col] = cellLabel;
                cellLabel.setPrefSize(40, 40);
                cellLabel.setStyle("-fx-border-color: black;");
                int finalRow = row;
                int finalCol = col;

                startPlayer1Timer();

                cellLabel.setOnMouseClicked(e -> {
                    // Check if it's the player's turn and the cell is not already filled
                    if (playerTurn && cellLabel.getText().isEmpty()) {
                        String symbol = (playerIndex == 0) ? "X" : "O";
                        cellLabel.setText(symbol);
                        cellLabel.setTextFill(Color.BLACK);
                        cellLabel.setFont(Font.font("Arial", FontWeight.BOLD, 33));
                        playerTurn = false;
                        gameBoardCells[finalRow][finalCol] = cellLabel; // Update the game board state
                    }
                    cellLabel.setAlignment(Pos.CENTER);
                    gameClient.sendMessageToServer("move " + finalRow + " " + finalCol);

                    if(playerIndex == 1) {
                        stopPlayer2Timer();
                        startPlayer1Timer();
                        //player1TurnLabel.setVisible(true);
                        player2TurnLabel.setVisible(false);
                    }
                    else {
                        startPlayer2Timer();
                        stopPlayer1Timer();
                        player1TurnLabel.setVisible(false);
                        //player2TurnLabel.setVisible(true);
                    }
                });

                // Add the cell label to the grid
                gameBoardGrid.add(cellLabel, col, row);
            }
        }

        gameBoardRoot.setCenter(gameBoardGrid);


        HBox timerBox = new HBox(10, player1Label, player2Label);
        timerBox.setAlignment(Pos.CENTER);


        gameBoardRoot.setTop(timerBox);

        chatArea = new TextArea();
        chatArea.setEditable(false);
        gameBoardRoot.setBottom(chatArea);
        chatArea.setMaxHeight(50);

        Scene gameScene = new Scene(gameBoardRoot, 1000, 600);

        // Wrap the UI update code inside Platform.runLater()
        Platform.runLater(() -> {
            stage.setScene(gameScene);
            stage.show();
        });
    }


    public void startGame() {
        if (loggedIn) {
            showGameScene();
            gameClient.connectToServer();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Gomoku");
        showLoginScene();
    }

    public void drawOpponentMove(String response) {
        if(!response.contains(username)) {
            // Find the opening and closing parentheses
            int openingParenthesisIndex = response.indexOf("(");
            int closingParenthesisIndex = response.indexOf(")");

            // Extract the substring between the parentheses
            String numbersSubstring = response.substring(openingParenthesisIndex + 1, closingParenthesisIndex);

            // Split the substring using comma as the delimiter
            String[] numbers = numbersSubstring.split(",");

            // Remove any whitespace from the numbers
            String number1 = numbers[0].trim();
            String number2 = numbers[1].trim();

            // Convert the extracted numbers from string to integers if needed
            int x = Integer.parseInt(number1);
            int y = Integer.parseInt(number2);

            Platform.runLater(() -> {
                Label cellLabel = gameBoardCells[x][y];
                cellLabel.setTextFill(Color.BLACK);
                cellLabel.setFont(Font.font("Arial", FontWeight.BOLD, 33));
                cellLabel.setAlignment(Pos.CENTER);
                if (playerIndex == 0) {
                    cellLabel.setText("O");
                    startPlayer1Timer();
                    stopPlayer2Timer();
                    player1TurnLabel.setVisible(true);
                } else {
                    cellLabel.setText("X");
                    stopPlayer1Timer();
                    startPlayer2Timer();
                    player2TurnLabel.setVisible(true);
                }
            });
        }
    }

    public void showWinnerPage(String response) {
        Platform.runLater(() -> {
            BorderPane winnerRoot = new BorderPane();
            winnerRoot.setPadding(new Insets(10, 10, 10, 10));

            Label winnerLabel = new Label(response);
            winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            winnerRoot.setCenter(winnerLabel);

            Button returnButton = new Button("Return to Main Menu");
            returnButton.setOnAction(e -> {
                showGameScene();
            });
            winnerRoot.setBottom(returnButton);
            BorderPane.setAlignment(returnButton, Pos.CENTER);

            Scene winnerScene = new Scene(winnerRoot, 1000, 600);
//            Stage stage = (Stage) gameScenePane.getScene().getWindow();
            stage.setScene(winnerScene);
            stage.show();
        });
    }

    public void modifyJoinedPlayersLabel(String name) {
        joinedPlayersContent.append(name).append("\n");
        Platform.runLater(() -> {
            joinedPlayersLabel.setText(joinedPlayersContent.toString());
        });
    }

    public void addJoinedPlayersLabel(String names) {
        String[] namesArray = names.split(", ");
        Platform.runLater(() -> {
            for (String name : namesArray) {
                joinedPlayersContent.append(name).append("\n");
            }
            joinedPlayersLabel.setText(joinedPlayersContent.toString());
            joinedPlayersLabel.setVisible(true);
        });
    }

    public void startPlayer1Timer() {
        stopPlayer1Timer(); // Stop the timer if it's already running
        player1TimerObj = new Timer();
        player1TimerTask = new TimerTask() {
            @Override
            public void run() {
                player1Timer--;
                updatePlayer1TimerLabel();
            }
        };
        player1TimerObj.scheduleAtFixedRate(player1TimerTask, 1000, 1000); // Update timer label every second
    }

    public void stopPlayer1Timer() {
        if (player1TimerTask != null) {
            player1TimerTask.cancel();
            player1TimerTask = null;
            player1TimerObj.cancel();
            player1TimerObj.purge();
            player1TimerObj = null;
        }
    }

    public void startPlayer2Timer() {
        stopPlayer2Timer(); // Stop the timer if it's already running

        player2TimerObj = new Timer();
        player2TimerTask = new TimerTask() {
            @Override
            public void run() {
                player2Timer--;
                updatePlayer2TimerLabel();
            }
        };
        player2TimerObj.scheduleAtFixedRate(player2TimerTask, 500, 1000); // Update timer label every second
    }

    public void stopPlayer2Timer() {
        if (player2TimerTask != null) {
            player2TimerTask.cancel();
            player2TimerTask = null;
            player2TimerObj.cancel();
            player2TimerObj.purge();
            player2TimerObj = null;
        }
    }

    public void updatePlayer1TimerLabel() {
        Platform.runLater(() -> player1TimerLabel.setText("Player 1 Timer: " + player1Timer));
    }

    public void updatePlayer2TimerLabel() {
        Platform.runLater(() -> player2TimerLabel.setText("Player 2 Timer: " + player2Timer));
    }

    public void showNewRoundPage(String response) {
        Platform.runLater(() -> {
            BorderPane winnerRoot = new BorderPane();
            winnerRoot.setPadding(new Insets(10, 10, 10, 10));

            Label newRoundLabel = new Label("NEW ROUND");
            newRoundLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            winnerRoot.setCenter(newRoundLabel);

            Scene newRoundScene = new Scene(winnerRoot, 1000, 600);
//            Stage stage = (Stage) gameScenePane.getScene().getWindow();
            stage.setScene(newRoundScene);
            stage.show();

        });
    }
}
