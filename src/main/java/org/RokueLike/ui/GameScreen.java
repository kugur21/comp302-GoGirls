package org.RokueLike.ui;

import org.RokueLike.domain.Hero;



import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GameScreen extends JPanel implements KeyListener {
    private final Hero hero;
    private final int tileSize = 40; // Size of each tile
    private final int gridWidth = 10, gridHeight = 10; // Grid dimensions
    private boolean[][] obstacles = new boolean[gridWidth][gridHeight];
    private BufferedImage heroSprite; // Hero sprite
    private int maxHealth = 5; // Maximum health for the hero

    public GameScreen() {
        this.setFocusable(true);
        this.addKeyListener(this);

        // Initialize hero
        hero = new Hero(5, 5, maxHealth);

        // Load hero sprite
        try {
            heroSprite = ImageIO.read(getClass().getResource("/images/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Generate random obstacles
        generateObstacles();
    }

    private void generateObstacles() {
        Random random = new Random();
        for (int i = 0; i < 15; i++) { // Add 15 random obstacles
            int x = random.nextInt(gridWidth);
            int y = random.nextInt(gridHeight);
            obstacles[x][y] = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i <= gridWidth; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, gridHeight * tileSize);
        }
        for (int i = 0; i <= gridHeight; i++) {
            g.drawLine(0, i * tileSize, gridWidth * tileSize, i * tileSize);
        }

        // Draw obstacles
        g.setColor(Color.RED);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (obstacles[x][y]) {
                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }

        // Draw hero sprite
        if (heroSprite != null) {
            g.drawImage(heroSprite, hero.getX() * tileSize, hero.getY() * tileSize, tileSize, tileSize, null);
        }

        // Draw hero health bar
        g.setColor(Color.RED);
        g.fillRect(10, 10, 100, 10);
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, (int) ((hero.getHealth() / (double) maxHealth) * 100), 10);
        g.setColor(Color.WHITE);
        g.drawRect(10, 10, 100, 10);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newX = hero.getX(), newY = hero.getY();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> newY--;
            case KeyEvent.VK_DOWN -> newY++;
            case KeyEvent.VK_LEFT -> newX--;
            case KeyEvent.VK_RIGHT -> newX++;
        }

        // Check for collisions with obstacles and boundaries
        if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && !obstacles[newX][newY]) {
            hero.setPosition(newX, newY);
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
