package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.input.Keyboard;
import org.RokueLike.ui.input.Mouse;
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

        // Add key and mouse listener for movement and interaction
        this.addKeyListener(new Keyboard());
        this.addMouseListener(new Mouse());
        System.out.println("[PlayModeScreen]: Initialized.");
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
