package org.RokueLike.ui.screen;

import org.RokueLike.utils.FontLoader;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JPanel {

    public GameOverScreen(String title) {
        setLayout(new BorderLayout());
        setBackground(new Color(66, 40, 53)); // Background color #422835

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 18f)); // Custom font and size
        titleLabel.setForeground(Color.WHITE); // White text color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = getJPanel();
        buttonPanel.setOpaque(false); // Allow background to show through
        add(buttonPanel, BorderLayout.CENTER);

        // Add padding around the screen
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    private static JPanel getJPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false); // Allow background to show through

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f)); // Custom font for button
        restartButton.setForeground(Color.BLACK);
        restartButton.setBackground(new Color(50, 25, 40));// White text
        restartButton.addActionListener(e -> {
            Window.addScreen(new LaunchScreen(), "LaunchScreen", true);
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f)); // Custom font for button
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(50, 25, 40));// White text
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }
}
