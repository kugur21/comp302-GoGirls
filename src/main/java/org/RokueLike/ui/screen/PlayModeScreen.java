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
        GameManager.init();  // Initialize game manager logic
        this.renderer = new PlayModeRenderer();

        this.setFocusable(true);
        this.setLayout(null);

        // Add key listener for player movements
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handlePlayerInput(e.getKeyCode());
                repaint();
            }
        });
    }

    /**
     * Handles player input for movement or actions.
     *
     * @param keyCode The key pressed by the player.
     */

    private void handlePlayerInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> GameManager.movePlayer(0, -1);
            case KeyEvent.VK_DOWN -> GameManager.movePlayer(0, 1);
            case KeyEvent.VK_LEFT -> GameManager.movePlayer(-1, 0);
            case KeyEvent.VK_RIGHT -> GameManager.movePlayer(1, 0);
            case KeyEvent.VK_SPACE -> GameManager.interact();  // Interaction key
            default -> System.out.println("[PlayModeScreen]: Unhandled key pressed.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderPlayMode((Graphics2D) g);
    }

}
