package org.RokueLike.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private final int gridWidth = 20; // Ensure positive value
    private final int gridHeight = 20; // Ensure positive value
    private int heroHealth = 10;
    private int wizardX, wizardY;
    private Random random = new Random();

    public GameScreen() {
        this.setFocusable(true);
        this.addKeyListener(this);

        if (gridWidth <= 0 || gridHeight <= 0) {
            throw new IllegalArgumentException("Grid dimensions must be positive.");
        }

        hall = new int[gridWidth][gridHeight]; // Initialize the hall array

        // Load sprites
        try {
            heroSprite = ImageIO.read(getClass().getResource("/images/player.png"));
            wizardSprite = ImageIO.read(getClass().getResource("/images/wizard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpHall();
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

        // Find a random empty spot for the wizard
        do {
            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);
        } while (hall[x][y] != 0); // Ensure the spot is empty

        wizardX = x;
        wizardY = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                g.setColor(hall[i][j] == 1 ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(i * tileSize, j * tileSize, tileSize, tileSize);
            }
        }

        // Draw the hero
        if (heroSprite != null) {
            g.drawImage(heroSprite, heroX * tileSize, heroY * tileSize, tileSize, tileSize, null);
        }

        // Draw the wizard
        if (wizardSprite != null) {
            g.drawImage(wizardSprite, wizardX * tileSize, wizardY * tileSize, tileSize, tileSize, null);
        }

        // Display hero's health
        g.setColor(Color.RED);
        g.drawString("Health: " + heroHealth, 10, 20);
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
