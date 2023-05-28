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

        Label usernameLabel = new Label("USERNAME");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        usernameLabel.setTextFill(Color.BLACK);
        usernameLabel.setAlignment(Pos.CENTER);
        usernameLabel.setPadding(new Insets(50));

        StackPane usernamePane = new StackPane(usernameLabel);
        usernamePane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        BorderPane.setAlignment(usernamePane, Pos.CENTER);
        BorderPane.setMargin(usernamePane, new Insets(50));
        loginRoot.setTop(usernamePane);

        Scene loginScene = new Scene(loginRoot, 800, 600);
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

        Scene gameScene = new Scene(root, 800, 600);
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

        Scene gameScene = new Scene(root, 800, 600);
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
