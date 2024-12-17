package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.input.Keyboard;
import org.RokueLike.ui.input.MousePlay;
import org.RokueLike.ui.render.PlayModeRenderer;

import javax.swing.*;
import java.awt.*;

public class PlayModeScreen extends JPanel {

    private final PlayModeRenderer renderer;

    public PlayModeScreen() {
        GameManager.init();  // Initialize the GameManager logic
        this.renderer = new PlayModeRenderer();

        this.setFocusable(true);
        //this.requestFocusInWindow();
        this.setLayout(null);


        // Add key and mouse listener for movement and interaction
        this.addKeyListener(new Keyboard());
        this.addMouseListener(new MousePlay());
        System.out.println("[PlayModeScreen]: Initialized.");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderPlayMode((Graphics2D) g);
        repaint();
    }

}
