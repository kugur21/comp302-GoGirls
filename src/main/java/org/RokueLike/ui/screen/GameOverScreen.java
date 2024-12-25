package org.RokueLike.ui.screen;

import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScreen extends JPanel {

    public GameOverScreen() {
        this.setLayout(null);
        this.setBackground(Color.BLACK);

        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverLabel.setBounds(0, 200, Window.WIDTH, 100);
        this.add(gameOverLabel);

        JButton restartButton = createButton("Restart", 350, e -> restartGame());
        JButton exitButton = createButton("Exit", 450, e -> System.exit(0));

        this.add(restartButton);
        this.add(exitButton);
    }

    private JButton createButton(String text, int yPosition, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds((Window.WIDTH - 200) / 2, yPosition, 200, 50);
        button.addActionListener(action);
        return button;
    }

    private void restartGame() {
        Window.showScreen("LaunchScreen");
    }
}
