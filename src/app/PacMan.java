/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Random;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import javax.swing.*;

public class PacMan extends Pane {
    private JPanel swingPanel; // Composition: JPanel as a member
    class Block {
        int pauseFrames = 0;
        int x, y, width, height;
        Image image;
        int startX, startY;
        char direction = 'U'; // U D L R
        int velocityX = 0, velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            this.direction = direction;
            switch(direction) {
                case 'U':
                    velocityX = 0;
                    velocityY = -tileSize / 4;
                    break;
                case 'D':
                    velocityX = 0;
                    velocityY = tileSize / 4;
                    break;
                case 'L':
                    velocityX = -tileSize / 4;
                    velocityY = 0;
                    break;
                case 'R':
                    velocityX = tileSize / 4;
                    velocityY = 0;
                    break;
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;
    private int frameCount = 0;

    // In your game loop or animation timer
    public void handle(long now) {
        frameCount++;
        move();
        // other game loop logic
    }

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    AnimationTimer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'}; //up down left right
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    PacMan() {
        setPrefSize(boardWidth, boardHeight);
        setStyle("-fx-background-color: black;");
        
        wallImage = new Image(getClass().getResource("resources/wall.png").toExternalForm());
        blueGhostImage = new Image(getClass().getResource("resources/blueGhost.png").toExternalForm());
        orangeGhostImage = new Image(getClass().getResource("resources/orangeGhost.png").toExternalForm());
        pinkGhostImage = new Image(getClass().getResource("resources/pinkGhost.png").toExternalForm());
        redGhostImage = new Image(getClass().getResource("resources/redGhost.png").toExternalForm());

        pacmanUpImage = new Image(getClass().getResource("resources/pacmanUp.png").toExternalForm());
        pacmanDownImage = new Image(getClass().getResource("resources/pacmanDown.png").toExternalForm());
        pacmanLeftImage = new Image(getClass().getResource("resources/pacmanLeft.png").toExternalForm());
        pacmanRightImage = new Image(getClass().getResource("resources/pacmanRight.png").toExternalForm());

        loadMap();

        // Initializing Game Loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    move();
                    redraw();
                } else {
                    gameOverDialog();
                }
            }
        };
    }

    public void startGame() {
        loadMap();
        resetPositions();
        lives = 3;
        score = 0;
        gameOver = false;
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X') { // Wall block
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                } else if (tileMapChar == 'b') { // Blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') { // Orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') { // Pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') { // Red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') { // PacMan
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') { // Food
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }
            }
        }
    }

    public void redraw() {
        getChildren().clear();

        // Drawing PacMan
        getChildren().add(new ImageView(pacman.image) {{
            setX(pacman.x);
            setY(pacman.y);
            setFitWidth(pacman.width);
            setFitHeight(pacman.height);
        }});

        // Drawing ghosts
        for (Block ghost : ghosts) {
            getChildren().add(new ImageView(ghost.image) {{
                setX(ghost.x);
                setY(ghost.y);
                setFitWidth(ghost.width);
                setFitHeight(ghost.height);
            }});
        }

        // Drawing walls
        for (Block wall : walls) {
            getChildren().add(new ImageView(wall.image) {{
                setX(wall.x);
                setY(wall.y);
                setFitWidth(wall.width);
                setFitHeight(wall.height);
            }});
        }

        // Drawing food
        for (Block food : foods) {
            getChildren().add(new javafx.scene.shape.Rectangle(food.x, food.y, food.width, food.height) {{
                setFill(Color.WHITE);
            }});
        }

        // Displaying score and lives
        Text scoreText = new Text(10, 20, "Score: " + score + " Lives: " + lives);
        scoreText.setFont(Font.font("Arial", 20));
        scoreText.setFill(Color.WHITE);
        getChildren().add(scoreText);

        if (gameOver) {
            Text gameOverText = new Text(10, 50, "Game Over! Score: " + score);
            gameOverText.setFont(Font.font("Arial", 20));
            gameOverText.setFill(Color.WHITE);
            getChildren().add(gameOverText);
        }
    }

    
    private void gameOverDialog() {
        // Stop the game loop first
        gameLoop.stop();

        // Use Platform.runLater for JavaFX thread safety
        javafx.application.Platform.runLater(() -> {
            // Custom button options
            Object[] options = {"Try Again", "Quit"};

            // Show dialog
            int choice = JOptionPane.showOptionDialog(
                null,                               // Use null instead of swingPanel
                "Game Over!\nScore: " + score,      // Message
                "Game Over",                        // Title
                JOptionPane.YES_NO_OPTION,          // Option type
                JOptionPane.INFORMATION_MESSAGE,    // Message type
                null,                               // Icon (default)
                options,                            // Custom button labels
                options[0]                          // Default selected button
            );

            // Handle button clicks
            if (choice == JOptionPane.YES_OPTION) {
                // Use Platform.runLater to ensure thread safety
                javafx.application.Platform.runLater(() -> startGame());
            } else if (choice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        });
    }

    public void move() {
    pacman.x += pacman.velocityX;
    pacman.y += pacman.velocityY;
    
    //check food collision
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

    // Check wall collisions for Pacman
    for (Block wall : walls) {
        if (collision(pacman, wall)) {
            pacman.x -= pacman.velocityX;
            pacman.y -= pacman.velocityY;
            break;
        }
    }

    // Ghost movement logic
    for (Block ghost : ghosts) {
        // Check collision with Pacman
        if (collision(ghost, pacman)) {
            lives -= 1;
            if (lives == 0) {
                gameOver = true;
                return;
            }
            resetPositions();
        }

        // Slow down ghost movement (reduce velocity)
        if (frameCount % 2 == 0) {  // Move every other frame
            // Automatic ghost movement
            // If ghost reaches a wall or screen edge, change direction
            ghost.x += ghost.velocityX / 2;  // Reduce velocity
            ghost.y += ghost.velocityY / 2;  // Reduce velocity

            boolean directionChanged = false;
            for (Block wall : walls) {
                if (collision(ghost, wall) || 
                    ghost.x <= 0 || 
                    ghost.x + ghost.width >= boardWidth ||
                    ghost.y <= 0 || 
                    ghost.y + ghost.height >= boardHeight) {

                    // Revert position
                    ghost.x -= ghost.velocityX / 2;
                    ghost.y -= ghost.velocityY / 2;

                    // Add a pause after wall collision
                    ghost.pauseFrames = 30;  // 30 frames pause

                    // Randomly choose a new direction
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    directionChanged = true;
                    break;
                }
            }

            // If no wall collision and not paused, occasionally change direction
            if (!directionChanged && ghost.pauseFrames <= 0 && random.nextInt(50) == 0) {
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection);
            }
        }
        // In your move method, decrement pause frames if applicable
        if (ghost.pauseFrames > 0) {
            ghost.pauseFrames--;
        }
    }

    

    

    
}
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }
    
    public void handleKeyInput(KeyCode key) {
    if (null != key) switch (key) {
            case UP:
                pacman.updateDirection('U');
                pacman.image = pacmanUpImage;
                break;
            case DOWN:
                pacman.updateDirection('D');
                pacman.image = pacmanDownImage;
                break;
            case LEFT:
                pacman.updateDirection('L');
                pacman.image = pacmanLeftImage;
                break;
            case RIGHT:
                pacman.updateDirection('R');
                pacman.image = pacmanRightImage;
                break;
            default:
                break;
        }
}
}
