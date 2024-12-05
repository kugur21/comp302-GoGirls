package org.RokueLike.ui;

import org.RokueLike.domain.Enemy;
import org.RokueLike.domain.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameScreen extends JPanel implements KeyListener {
    private final Hero hero;
    private final Enemy enemy;
    private final int tileSize = 40; // Size of each tile
    private final int gridWidth = 10, gridHeight = 10; // Grid size
    private boolean[][] obstacles = new boolean[gridWidth][gridHeight];
    private int maxHealth = 5; // Maximum health for the hero

    public GameScreen() {
        this.setFocusable(true);
        this.addKeyListener(this);

        // Initialize the hero and enemy
        hero = new Hero(5, 5, maxHealth, 10);
        enemy = new Enemy(2, 2);

        // Initialize obstacles
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

        // Draw the hero
        g.setColor(Color.BLUE);
        g.fillOval(hero.getX() * tileSize, hero.getY() * tileSize, tileSize, tileSize);

        // Draw the enemy
        g.setColor(Color.YELLOW);
        g.fillRect(enemy.getX() * tileSize, enemy.getY() * tileSize, tileSize, tileSize);

        // Draw hero's health bar
        UI.drawHealthBar(g, 10, 10, hero.getHealth(), maxHealth);
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

        // Check collisions with obstacles and boundaries
        if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight && !obstacles[newX][newY]) {
            hero.setPosition(newX, newY);
        }

        // Check for enemy interaction
        if (newX == enemy.getX() && newY == enemy.getY()) {
            hero.decreaseHealth(1);
            System.out.println("Enemy encountered! Health: " + hero.getHealth());
        }

        // Move enemy towards the hero
        enemy.moveTowards(hero.getX(), hero.getY());

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
