package app;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.ArrayList;

public class PacManBlock {
    private int x;
    private int y;
    private int width;
    private int height;
    private ImageView imageView;
    private int startX;
    private int startY;
    private char direction;
    private int velocityX;
    private int velocityY;
    private Pane gamePane;
    private List<PacManBlock> walls;
    private int tileSize;

    // Constructor
    public PacManBlock(Pane gamePane, Image image, int x, int y, int width, int height, int tileSize) {
        this.gamePane = gamePane;
        this.tileSize = tileSize;
        this.walls = new ArrayList<>();
        
        // Initialize direction and velocity
        this.direction = 'U';
        this.velocityX = 0;
        this.velocityY = 0;

        // Create ImageView
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);

        // Set position
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;

        // Position ImageView
        this.imageView.setX(x);
        this.imageView.setY(y);

        // Add to game pane
        gamePane.getChildren().add(this.imageView);
    }

    // Add method to set wall blocks
    public void addWallBlock(PacManBlock wallBlock) {
        this.walls.add(wallBlock);
    }

    // Update direction method
    public void updateDirection(char newDirection) {
        char previousDirection = this.direction;
        
        // Update direction and velocity
        this.direction = newDirection;
        this.updateVelocity();

        // Tentative move
        this.x += this.velocityX;
        this.y += this.velocityY;

        // Check for collisions
        boolean collision = false;
        for (PacManBlock wall : walls) {
            if (this != wall && collision(this, wall)) {
                collision = true;
                break;
            }
        }

        // If collision, revert
        if (collision) {
            this.x -= this.velocityX;
            this.y -= this.velocityY;
            this.direction = previousDirection;
            this.updateVelocity();
        }

        // Update ImageView position
        this.imageView.setX(this.x);
        this.imageView.setY(this.y);
    }

    // Update velocity based on direction
    private void updateVelocity() {
        switch (this.direction) {
            case 'U':
                this.velocityX = 0;
                this.velocityY = -this.tileSize / 4;
                break;
            case 'D':
                this.velocityX = 0;
                this.velocityY = this.tileSize / 4;
                break;
            case 'L':
                this.velocityX = -this.tileSize / 4;
                this.velocityY = 0;
                break;
            case 'R':
                this.velocityX = this.tileSize / 4;
                this.velocityY = 0;
                break;
        }
    }

    // Collision detection method
    private boolean collision(PacManBlock block1, PacManBlock block2) {
        return block1.x < block2.x + block2.width &&
               block1.x + block1.width > block2.x &&
               block1.y < block2.y + block2.height &&
               block1.y + block1.height > block2.y;
    }

    // Reset method
    public void reset() {
        this.x = this.startX;
        this.y = this.startY;
        this.imageView.setX(this.x);
        this.imageView.setY(this.y);
    }

    // Getters
    public ImageView getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}