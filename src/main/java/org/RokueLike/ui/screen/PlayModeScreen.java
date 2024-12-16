package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.render.PlayModeRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayModeScreen extends JPanel {

    private final PlayModeRenderer renderer;

    public PlayModeScreen() {
        GameManager.init();  // Initialize the GameManager logic
        this.renderer = new PlayModeRenderer();

        this.setFocusable(true);
        this.setLayout(null);

        // Add key listener for movement and interaction
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handlePlayerInput(e.getKeyCode());
                repaint(); // Repaint after every action
            }
        });
    }

    /**
     * Handles player input for movement and interaction.
     *
     * @param keyCode The key pressed.
     */
    private void handlePlayerInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> GameManager.handleMovement(0, -1);
            case KeyEvent.VK_DOWN -> GameManager.handleMovement(0, 1);
            case KeyEvent.VK_LEFT -> GameManager.handleMovement(-1, 0);
            case KeyEvent.VK_RIGHT -> GameManager.handleMovement(1, 0);
            case KeyEvent.VK_SPACE -> GameManager.handleEnchantmentUse(null); // Interact
            default -> System.out.println("[PlayModeScreen]: Unknown key pressed.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderPlayMode((Graphics2D) g);
    }
}
