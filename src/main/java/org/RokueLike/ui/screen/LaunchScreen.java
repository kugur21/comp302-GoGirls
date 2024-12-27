package org.RokueLike.ui.screen;

import org.RokueLike.ui.Window;
import org.RokueLike.utils.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LaunchScreen extends JPanel {

    private final Image backgroundImage;
    private final Font customFont;

    // Constructs the launch screen, initializes the layout, and adds buttons.
    public LaunchScreen() {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/Rokue-like logo 4.png"))).getImage();
        customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 24f);
        this.setLayout(null);

        JButton playButton = createStyledButton("PLAY", 350, 550);
        JButton helpButton = createStyledButton("HELP", 150, 550);
        JButton exitButton = createStyledButton("EXIT", 550, 550);

        // Starts the game by adding a BuildModeScreen
        playButton.addActionListener(e -> Window.addScreen(new BuildModeScreen(), "BuildModeScreen", true));
        // Opens the HelpScreen by showing the HelpScreen
        helpButton.addActionListener(e -> Window.showScreen("HelpScreen"));
        // Exits the game
        exitButton.addActionListener(e -> System.exit(0));

        this.add(playButton);
        this.add(helpButton);
        this.add(exitButton);
    }

    // Draws the background image on the panel.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // Creates a custom-styled button with specified text and position.
    private JButton createStyledButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(customFont);
        button.setBackground(Color.RED);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBounds(x, y, 200, 80);
        return button;
    }

}