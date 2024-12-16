package org.RokueLike.ui.screen;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.render.BuildModeRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BuildModeScreen extends JPanel{

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

        // Buttons
        JButton manualButton = createButton("Manual Placement", 150, e -> enterManualPlacement());
        this.add(manualButton);

        JButton randomButton = createButton("Random Assignment", 250, e -> assignObjectsRandomly());
        this.add(randomButton);

        JButton proceedButton = createButton("Proceed to Play Mode", 350, e -> proceedToPlayMode());
        this.add(proceedButton);
    }

    private JButton createButton(String text, int yPosition, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBounds((Window.WIDTH - 200) / 2, yPosition, 200, 50);
        button.addActionListener(action);
        return button;
    }

    private void enterManualPlacement() {
        System.out.println("[BuildModeScreen]: Entering Manual Placement Mode.");
        // Manual placement logic - Input handling for x, y coordinates will be implemented here.
        // For now, simulate input:
        BuildManager.placeObjectManually("earth", 7, 7); // Example
        repaint();
    }

    private void assignObjectsRandomly() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly.");
        BuildManager.placeObjectRandomly("earth", 6);
        BuildManager.placeObjectRandomly("air", 9);
        BuildManager.placeObjectRandomly("water", 13);
        BuildManager.placeObjectRandomly("fire", 17);
        repaint();
    }

    private void proceedToPlayMode() {
        System.out.println("[BuildModeScreen]: Proceeding to Play Mode.");
        Window.addScreen(new PlayModeScreen(), "PlayModeScreen", true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.renderBuildMode((Graphics2D) g);
    }

}