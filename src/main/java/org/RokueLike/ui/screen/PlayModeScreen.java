package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.input.Keyboard;
import org.RokueLike.ui.input.MousePlay;
import org.RokueLike.ui.render.PlayModeRenderer;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;

public class PlayModeScreen extends JPanel {

    private final PlayModeRenderer renderer; // Renderer for play mode visuals
    private final Rectangle pauseButtonBounds;
    private final Rectangle exitButtonBounds;
    private final String saveFileName;

    public PlayModeScreen(String fileName) {
        this.saveFileName = fileName;
        // Initialize the GameManager for game mode logic
        GameManager.init(saveFileName);

        this.renderer = new PlayModeRenderer();
        this.setFocusable(true);
        this.setLayout(null);

        // TODO: Not generic, needs fixing
        pauseButtonBounds = new Rectangle(650, 75, 32, 32);
        exitButtonBounds = new Rectangle(690, 75, 32, 32);

        this.addKeyListener(new Keyboard());
        this.addMouseListener(new MousePlay(pauseButtonBounds, exitButtonBounds, this));
        System.out.println("[PlayModeScreen]: Initialized.");
    }

    // Returns to the LaunchScreen and resets the game state.
    public void returnToLaunchScreen() {
        String fileName = "src/main/resources/saves/";
        if (saveFileName.endsWith(".dat")) {
            fileName += saveFileName;
        } else {
            fileName += saveFileName + ".dat";
        }
        GameManager.saveGame(fileName);
        GameManager.reset(false);
        Window.addScreen(new LaunchScreen(), "LaunchScreen", true);
    }

    // Renders the play mode visuals using the PlayModeRenderer.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderPlayMode((Graphics2D) g, exitButtonBounds, pauseButtonBounds);  // Render custom graphics for play mode
        repaint(); // Continuously repaint for dynamic updates
    }

}