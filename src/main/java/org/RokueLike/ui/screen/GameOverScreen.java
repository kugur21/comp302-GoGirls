package org.RokueLike.ui.screen;

import org.RokueLike.utils.FontLoader;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JPanel {

    public GameOverScreen(String title) {
        setLayout(new BorderLayout());
        setBackground(new Color(66, 40, 53));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 18f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = getJPanel();
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.CENTER);

        // Add padding around the screen
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    // Creates a button panel.
    private static JPanel getJPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f));
        restartButton.setForeground(Color.BLACK);
        restartButton.setBackground(new Color(50, 25, 40));
        restartButton.addActionListener(e -> {
            Window.addScreen(new LaunchScreen(), "LaunchScreen", true);
        }); // Switch to launch screen

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f));
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(50, 25, 40));
        exitButton.addActionListener(e -> {
            System.exit(0);
        }); // Exit application

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }

}