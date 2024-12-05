package org.RokueLike.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GameScreen extends JPanel implements KeyListener {
    private BufferedImage heroSprite;
    private BufferedImage wizardSprite;
    private int heroX = 5, heroY = 5;
    private int[][] hall;
    private final int tileSize = 32;
    private final int gridWidth = 20;
    private final int gridHeight = 20;
    private int heroHealth = 10;
    private int wizardX, wizardY;
    private boolean isPaused = false; // Pause state
    private int timer = 30; // Countdown timer in seconds
    private Timer gameTimer; // Swing Timer for countdown
    private boolean gameOver = false; // Game over state
    private final Random random = new Random();

    public GameScreen() {
        this.setLayout(null); // Allow absolute positioning
        this.setFocusable(true);
        this.addKeyListener(this);

        hall = new int[gridWidth][gridHeight]; // Initialize the hall array

        // Load sprites
        try {
            heroSprite = ImageIO.read(getClass().getResource("/images/player.png"));
            wizardSprite = ImageIO.read(getClass().getResource("/images/wizard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpHall();

        // Add control buttons and timer
        addControlButtons();
        startTimer();
    }

    private void setUpHall() {
        // Populate hall with obstacles
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                hall[i][j] = random.nextInt(10) < 2 ? 1 : 0; // 20% chance for obstacle
            }
        }

        // Ensure hero's starting position is empty
        hall[heroX][heroY] = 0;

        // Place the wizard
        placeWizard();
    }

    private void placeWizard() {
        int x, y;
        do {
            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);
        } while (hall[x][y] != 0); // Ensure the spot is empty

        wizardX = x;
        wizardY = y;
    }

    private void addControlButtons() {
        // Fixed Button and Timer Dimensions
        int buttonWidth = 150;
        int buttonHeight = 40;

        // Fixed X and Y positions for buttons and timer
        int buttonX = 800; // Fixed X position for all buttons
        int closeButtonY = 80; // Close button at the top
        int pauseButtonY = closeButtonY + buttonHeight + 20; // "Pause" below "Close Game"
        int timerLabelY = pauseButtonY + buttonHeight + 20; // Timer below "Pause"
        int inventoryY = timerLabelY + buttonHeight + 80; // Inventory image below timer

        // Close Game Button
        JButton closeButton = new JButton("Close Game");
        closeButton.setBounds(buttonX, closeButtonY, buttonWidth, buttonHeight);
        closeButton.setFocusPainted(false);
        closeButton.setBackground(Color.LIGHT_GRAY);
        closeButton.setForeground(Color.BLACK);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> System.exit(0));
        this.add(closeButton);

        // Pause/Resume Button
        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(buttonX, pauseButtonY, buttonWidth, buttonHeight);
        pauseButton.setFocusPainted(false);
        pauseButton.setBackground(Color.LIGHT_GRAY);
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                togglePause(isPaused);
                pauseButton.setText(isPaused ? "Resume" : "Pause");
            }
        });
        this.add(pauseButton);

        // Timer Label
        JLabel timerLabel = new JLabel("Time Left: " + timer + "s");
        timerLabel.setBounds(buttonX, timerLabelY, buttonWidth, buttonHeight);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setBackground(Color.LIGHT_GRAY);
        timerLabel.setOpaque(true);
        this.add(timerLabel);

        try {
            // Load and resize the inventory image
            ImageIcon inventoryIcon = new ImageIcon(getClass().getResource("/images/inventory.png"));
            Image scaledInventoryImage = inventoryIcon.getImage().getScaledInstance(250, 350, Image.SCALE_SMOOTH); // Resize to 150x150
            JLabel inventoryLabel = new JLabel(new ImageIcon(scaledInventoryImage));
            inventoryLabel.setBounds(buttonX, inventoryY, 250, 350); // Adjust position and size
            this.add(inventoryLabel);
        } catch (Exception e) {
            System.err.println("Failed to load inventory image: " + e.getMessage());
        }

        // Timer Countdown Logic
        gameTimer = new Timer(1000, e -> {
            if (!isPaused && !gameOver) {
                timer--;
                timerLabel.setText("Time Left: " + timer + "s");

                if (timer <= 0) {
                    gameOver = true;
                    endGame();
                }
            }
        });
    }


    private void togglePause(boolean isPaused) {
        this.isPaused = isPaused;
        this.requestFocusInWindow(); // Refocus the game screen
    }

    private void startTimer() {
        gameTimer.start();
    }

    private void endGame() {
        gameTimer.stop();

        // Display "Game Over" and Retry button
        JLabel gameOverLabel = new JLabel("Game Over: Time is Out");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds((getWidth() - 300) / 2, getHeight() / 2 - 100, 300, 50);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(gameOverLabel);

        JButton retryButton = new JButton("Retry");
        retryButton.setFont(new Font("Arial", Font.BOLD, 16));
        retryButton.setBounds((getWidth() - 100) / 2, getHeight() / 2, 100, 40);
        retryButton.addActionListener(e -> resetGame());
        this.add(retryButton);

        this.revalidate();
        this.repaint();
    }

    private void resetGame() {
        // Reset the game state
        timer = 30;
        gameOver = false;
        heroX = 5;
        heroY = 5;
        setUpHall();
        removeAll(); // Remove all components and add them back
        addControlButtons();
        startTimer();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw brown background for the hall
        g.setColor(new Color(139, 69, 19)); // Brown color
        g.fillRect(0, 0, getWidth(), getHeight());

        // Grid positioning
        int totalGridWidth = gridWidth * tileSize;
        int totalGridHeight = gridHeight * tileSize;
        int startX = 50; // Padding from the left
        int startY = (getHeight() - totalGridHeight) / 2; // Center vertically

        // Draw the grid
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                g.setColor(hall[i][j] == 1 ? Color.DARK_GRAY : new Color(210, 180, 140)); // Wall or floor
                g.fillRect(startX + i * tileSize, startY + j * tileSize, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(startX + i * tileSize, startY + j * tileSize, tileSize, tileSize);
            }
        }

        // Draw hero
        if (heroSprite != null) {
            g.drawImage(heroSprite, startX + heroX * tileSize, startY + heroY * tileSize, tileSize, tileSize, null);
        }

        // Draw wizard
        if (wizardSprite != null) {
            g.drawImage(wizardSprite, startX + wizardX * tileSize, startY + wizardY * tileSize, tileSize, tileSize, null);
        }

        // Display hero's health
        g.setColor(Color.RED);
        g.drawString("Health: " + heroHealth, 10, 20);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (isPaused || gameOver) return; // Ignore input while paused or game is over

        int newX = heroX, newY = heroY;

        // Move hero based on key press
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> newY--;
            case KeyEvent.VK_DOWN -> newY++;
            case KeyEvent.VK_LEFT -> newX--;
            case KeyEvent.VK_RIGHT -> newX++;
        }

        // Ensure movement within bounds
        if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
            heroX = newX;
            heroY = newY;
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
