package org.RokueLike.ui.screen;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.input.MouseBuild;
import org.RokueLike.ui.render.BuildModeRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BuildModeScreen extends JPanel {

    private final BuildModeRenderer renderer;

    public BuildModeScreen() {
        BuildManager.init(); // Initialize the BuildManager
        this.renderer = new BuildModeRenderer();
        this.setLayout(null);

        // Title Label
        JLabel titleLabel = new JLabel("Build Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, Window.WIDTH, 50);
        this.add(titleLabel);

        JButton randomButton = createButton("Random Assignment", 550, e -> assignObjectsRandomly());
        this.add(randomButton);

        JButton proceedButton = createButton("Proceed to Play Mode", 600, e -> proceedToPlayMode());
        this.add(proceedButton);

        this.addMouseListener(new MouseBuild());
    }

    private JButton createButton(String text, int yPosition, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBounds((Window.WIDTH + 475 ) / 2, yPosition, 200, 50); // X, Y, WIDTH, HEIGHT
        button.addActionListener(action);
        return button;
    }

    private void assignObjectsRandomly() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly.");
        BuildManager.placeObjectRandomly("earth");
        BuildManager.placeObjectRandomly("air");
        BuildManager.placeObjectRandomly("water");
        BuildManager.placeObjectRandomly("fire");
        repaint();
    }

    private void proceedToPlayMode() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly to satisfy object limit.");
        BuildManager.placeObjectRandomly("earth");
        BuildManager.placeObjectRandomly("air");
        BuildManager.placeObjectRandomly("water");
        BuildManager.placeObjectRandomly("fire");
        System.out.println("[BuildModeScreen]: Proceeding to Play Mode.");
        Window.addScreen(new PlayModeScreen(), "PlayModeScreen", true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderBuildMode((Graphics2D) g);
        repaint();
    }
}
