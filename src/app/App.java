package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Game Window Configuration
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        // MENU PANEL
        BorderPane menuPane = new BorderPane();
        menuPane.setStyle("-fx-background-color: black;"); // Black background

        // Title Text
        Text titleLabel = new Text("PAC MAN");
        titleLabel.setFill(Color.YELLOW); // Yellow text
        titleLabel.setFont(Font.font("Arial", 36)); // Bold large font
        menuPane.setCenter(titleLabel);

        // Start Button
        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Arial", 20));
        startButton.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
        menuPane.setBottom(startButton);
        BorderPane.setAlignment(startButton, javafx.geometry.Pos.CENTER); // Align to center bottom

        // Menu Scene
        Scene menuScene = new Scene(menuPane, boardWidth, boardHeight);

        // Primary Stage Configuration
        primaryStage.setTitle("PacMan Game");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // START BUTTON LOGIC: Transition to the Game Scene
        startButton.setOnAction(e -> {
            // Game Panel (PacMan)
            PacMan game = new PacMan();
            Scene gameScene = new Scene(game);

            // Add Key Input Handling
            gameScene.setOnKeyPressed(event -> game.handleKeyInput(event.getCode()));

            // Set the new Scene to the Stage
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("PacMan Game");
            game.startGame(); // Start the game
            game.requestFocus(); // Focus on the game canvas
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
