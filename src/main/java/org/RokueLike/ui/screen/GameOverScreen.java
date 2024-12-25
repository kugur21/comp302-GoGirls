package org.RokueLike.ui.screen;

import org.RokueLike.ui.FontLoader;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScreen extends JPanel {

    public GameOverScreen(String title) {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = getJPanel();

        add(buttonPanel, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    }

    private static JPanel getJPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 18));
        restartButton.addActionListener(e -> {Window.addScreen(new LaunchScreen(), "LaunchScreen", true);});

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        exitButton.addActionListener(e -> {System.exit(0);});

        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }
}
