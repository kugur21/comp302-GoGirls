package org.RokueLike.ui.screen;

import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BuildModeScreen extends JPanel{


    public BuildModeScreen() {
        this.setLayout(null);

        JLabel titleLabel = new JLabel("Build Mode");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, Window.WIDTH, 50);
        this.add(titleLabel);

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
        // Logic for manual placement (e.g., grid visualization and interaction)
        // Implement this in conjunction with your hall grid logic.
    }

    private void assignObjectsRandomly() {
        System.out.println("[BuildModeScreen]: Assigning objects randomly.");
        // Call backend logic to randomly assign objects to the grid.
    }

    private void proceedToPlayMode() {
        System.out.println("[BuildModeScreen]: Proceeding to Play Mode.");
        Window.showScreen("PlayModeScreen");
    }

}