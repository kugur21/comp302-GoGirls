package org.RokueLike.ui.screen;

import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LaunchScreen extends JPanel {

    private final Image backgroundImage;

    public LaunchScreen() {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagesekstra/Rokue-like logo 4.png"))).getImage();
        this.setLayout(null);

        JButton playButton = createStyledButton("PLAY", 350, 550);
        JButton helpButton = createStyledButton("HELP", 150, 550);
        JButton exitButton = createStyledButton("EXIT", 550, 550);

        playButton.addActionListener(e -> startGame());
        helpButton.addActionListener(e -> openHelpScreen());
        exitButton.addActionListener(e -> System.exit(0));

        this.add(playButton);
        this.add(helpButton);
        this.add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Pixelated", Font.BOLD, 36));
        button.setBackground(Color.RED);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBounds(x, y, 200, 80);
        return button;
    }

    private void startGame() {
        Window.addScreen(new BuildModeScreen(), "BuildModeScreen", true);
    }

    private void openHelpScreen() {
        Window.showScreen("HelpScreen");
    }

}