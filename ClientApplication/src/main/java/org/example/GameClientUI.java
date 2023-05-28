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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameClientUI extends Application {
    private Stage stage;
    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;
    private GameClient gameClient;
    private boolean loggedIn;

    public GameClientUI(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void appendMessage(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.clear();
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
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));

        chatArea = new TextArea();
        chatArea.setEditable(false);
        root.setBottom(chatArea);
        chatArea.setMaxHeight(50);

        VBox buttonBox = new VBox(10); // VBox to hold the buttons
        buttonBox.setAlignment(Pos.CENTER);

        Button hostButton = new Button("Host");
        hostButton.setAlignment(Pos.CENTER);
        hostButton.setMinWidth(150); // Set minimum width for the button
        hostButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        hostButton.setTextFill(Color.WHITE); // Set button text color
        hostButton.setFont(Font.font(20)); // Set font size for the button text
        hostButton.setOnAction(e -> {
            showHostWindow();
        });
        buttonBox.getChildren().add(hostButton); // Add the "Host" button to the VBox

        Button joinButton = new Button("Join");
        joinButton.setAlignment(Pos.CENTER);
        joinButton.setMinWidth(150); // Set minimum width for the button
        joinButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        joinButton.setTextFill(Color.WHITE); // Set button text color
        joinButton.setFont(Font.font(20)); // Set font size for the button text
        buttonBox.getChildren().add(joinButton); // Add the "Join" button to the VBox

        root.setCenter(buttonBox); // Set the VBox as the bottom of the BorderPane
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(root, 1000, 600);
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

        Label roundsLabel = new Label("Number of rounds");
        TextField roundsField = new TextField();
        roundsField.setPrefWidth(200);

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
            String rounds = roundsField.getText();
            // Process the input and create the game
            //createGame(timeLimit, rounds);
            hostStage.close();
            showStartScene();
        });

        inputGrid.add(lobbyNameField, 0, 0);
        inputGrid.add(lobbyNameLabel, 1, 0);

        inputGrid.add(timeLimitField, 0, 1);
        inputGrid.add(timeLimitLabel, 1, 1);

        inputGrid.add(roundsField, 0, 2);
        inputGrid.add(roundsLabel, 1, 2);

        hostRoot.setCenter(inputGrid);
        hostRoot.setBottom(createGameButton);
        BorderPane.setAlignment(createGameButton, Pos.CENTER); // Align the button at the center

        Scene hostScene = new Scene(hostRoot, 400, 300);
        hostStage.setScene(hostScene);
        hostStage.showAndWait();
    }

    public void showStartScene() {
        BorderPane startRoot = new BorderPane();
        //startRoot.setPadding(new Insets(10, 10, 10, 10));

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
        startRoot.setTop(backButton);

        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setMinWidth(400);
        startButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setTextFill(Color.WHITE);
        startButton.setFont(Font.font(20));
        startButton.setOnAction(e -> {
            showGameBoardScene();
        });

        Label gameLobbyLabel = new Label("Game lobby:                                                      ");
        gameLobbyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gameLobbyLabel.setTextFill(Color.BLACK);
        gameLobbyLabel.setPadding(new Insets(10, 0, 0, 0)); // Adjust the padding for positioning

        VBox.setMargin(gameLobbyLabel, new Insets(0, 0, 40, 0)); // Add margin to the bottom of the label
        VBox.setMargin(startButton, new Insets(40, 0, 0, 0)); // Add margin to the top of the button

        centerContainer.getChildren().addAll(gameLobbyLabel, startButton);
        startRoot.setCenter(centerContainer);

        startRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(startRoot, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
    }

    public void showGameBoardScene() {
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

        // Add cells to the game board
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Create a label to represent a game board cell
                Label cellLabel = new Label();
                cellLabel.setPrefSize(40, 40);
                cellLabel.setStyle("-fx-border-color: black;");
                cellLabel.setOnMouseClicked(e -> {
                    // Fill the entire cell with an "X"
                    cellLabel.setTextFill(Color.BLACK);
                    cellLabel.setFont(Font.font("Arial", FontWeight.BOLD, 33));
                    cellLabel.setText("X");
                    cellLabel.setAlignment(Pos.CENTER);
                });

                // Add the cell label to the grid
                gameBoardGrid.add(cellLabel, col, row);
            }
        }

        gameBoardRoot.setCenter(gameBoardGrid);

        Scene gameScene = new Scene(gameBoardRoot, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
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
}
