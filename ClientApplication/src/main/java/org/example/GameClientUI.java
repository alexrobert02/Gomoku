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

    public GameClientUI(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
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
        hostButton.setOnAction(e -> showHostWindow());
        buttonBox.getChildren().add(hostButton); // Add the "Host" button to the VBox

        Button joinButton = new Button("Join");
        joinButton.setAlignment(Pos.CENTER);
        joinButton.setMinWidth(150); // Set minimum width for the button
        joinButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY))); // Set button background color
        joinButton.setTextFill(Color.WHITE); // Set button text color
        joinButton.setFont(Font.font(20)); // Set font size for the button text
        joinButton.setOnAction(e -> showJoinWindow());
        buttonBox.getChildren().add(joinButton); // Add the "Join" button to the VBox

        root.setCenter(buttonBox); // Set the VBox as the bottom of the BorderPane
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene gameScene = new Scene(root, 1000, 600);
        stage.setScene(gameScene);
        stage.show();
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
            String lobbyName = lobbyNameField.getText();
            // Process the input and create the game
            //createGame(timeLimit, rounds);
            hostStage.close();
            String command = ("create " + lobbyName + " " + username);
            gameClient.sendMessageToServer(command);
            playerIndex = 0;
            showStartScene(lobbyName);
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
        Label gameLobbyLabel = new Label("Game lobby: " + lobbyName);
        gameLobbyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gameLobbyLabel.setTextFill(Color.BLACK);
        gameLobbyLabel.setPadding(new Insets(10, 0, 0, 0));
        centerVBox.getChildren().add(gameLobbyLabel);

        Button startButton = new Button("Start");
        startButton.setMinWidth(400);
        startButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        startButton.setTextFill(Color.WHITE);
        startButton.setFont(Font.font(20));
        startButton.setOnAction(e -> {
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
                });

                // Add the cell label to the grid
                gameBoardGrid.add(cellLabel, col, row);
            }
        }

        gameBoardRoot.setCenter(gameBoardGrid);

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
            showGameSceneTest();
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
                if(playerIndex == 0) {
                    cellLabel.setText("O");
                }
                else {
                    cellLabel.setText("X");
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
}
