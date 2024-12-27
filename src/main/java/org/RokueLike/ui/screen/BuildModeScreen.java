package org.RokueLike.ui.screen;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.input.MouseBuild;
import org.RokueLike.ui.render.BuildModeRenderer;
import org.RokueLike.utils.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static org.RokueLike.utils.Constants.*;

public class BuildModeScreen extends JPanel {

    private final BuildModeRenderer renderer; // Renderer for build mode visuals
    private final Font customFont;

    // Constructs the BuildModeScreen, initializes layout, and sets up UI elements.
    public BuildModeScreen() {
        // Initialize the BuildManager for build mode logic
        BuildManager.init();

        this.customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 10f);
        this.renderer = new BuildModeRenderer();
        this.setLayout(null);

        JButton randomButton = createButton("Random Assignment", (int) (BUILD_WINDOW_HEIGHT * 0.75), e -> assignObjectsRandomly());
        this.add(randomButton);

        JButton proceedButton = createButton("Play", (int) (BUILD_WINDOW_HEIGHT * 0.75) + 50, e -> proceedToPlayMode());
        this.add(proceedButton);

        this.addMouseListener(new MouseBuild());
    }

    // Creates a styled button with an action listener.
    private JButton createButton(String text, int yPosition, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(customFont);
        button.setBounds((BUILD_WINDOW_WIDTH * 3/4), yPosition, 200, 50);
        button.addActionListener(action);
        return button;
    }

    // Assigns objects randomly for the build mode.
    private void assignObjectsRandomly() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly.");
        BuildManager.placeObjectRandomly("earth");
        BuildManager.placeObjectRandomly("air");
        BuildManager.placeObjectRandomly("water");
        BuildManager.placeObjectRandomly("fire");
        repaint(); // Refresh the screen to reflect changes
    }

    // Finalizes object placement and transitions to play mode.
    private void proceedToPlayMode() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly to satisfy object limit.");
        BuildManager.placeObjectRandomly("earth");
        BuildManager.placeObjectRandomly("air");
        BuildManager.placeObjectRandomly("water");
        BuildManager.placeObjectRandomly("fire");
        System.out.println("[BuildModeScreen]: Proceeding to Play Mode.");
        Window.addScreen(new PlayModeScreen(), "PlayModeScreen", true); // Starts the game by adding a PlayModeScreen
    }

    // Renders the build mode visuals using the BuildModeRenderer.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderBuildMode((Graphics2D) g); // Render custom graphics for build mode
        repaint(); // Continuously repaint for dynamic updates
    }
}
