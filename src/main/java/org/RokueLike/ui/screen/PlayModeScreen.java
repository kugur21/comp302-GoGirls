package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.Textures;
import org.RokueLike.ui.input.Keyboard;
import org.RokueLike.ui.input.MousePlay;
import org.RokueLike.ui.render.PlayModeRenderer;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayModeScreen extends JPanel {

    private final PlayModeRenderer renderer;
    private final Rectangle pauseButtonBounds;
    private final Rectangle exitButtonBounds;

    public PlayModeScreen() {
        GameManager.init();  // Initialize the GameManager logic
        this.renderer = new PlayModeRenderer();

        this.setFocusable(true);
        this.setLayout(null);

        pauseButtonBounds = new Rectangle(650, 75, 32, 32);
        exitButtonBounds = new Rectangle(690, 75, 32, 32);

        this.addKeyListener(new Keyboard());
        this.addMouseListener(new MousePlay(pauseButtonBounds, exitButtonBounds, this));
        System.out.println("[PlayModeScreen]: Initialized.");
    }

    public void returnToLaunchScreen() {
        GameManager.reset();
        Window.addScreen(new LaunchScreen(), "LaunchScreen", true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderPlayMode((Graphics2D) g, exitButtonBounds, pauseButtonBounds);
        repaint();
    }

}
