package org.RokueLike.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchScreen extends JPanel {
    private final JFrame window;
    private final Image backgroundImage;

    public LaunchScreen(JFrame window) {
        this.window = window;

        // Set layout to null for absolute positioning
        this.setLayout(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/images/Rokue-like logo 4.png")).getImage();

        // Create the "Play" button
        JButton playButton = new JButton("PLAY") {
            @Override
            protected void paintComponent(Graphics g) {
                // Rounded corners for the button
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Set corner radius
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        playButton.setFont(loadPixelFont(36)); // Use custom pixel font
        playButton.setBackground(Color.DARK_GRAY); // Set button background to red
        playButton.setForeground(Color.WHITE); // Set button text color to white
        playButton.setOpaque(false); // Let paintComponent handle background
        playButton.setFocusPainted(false); // Remove focus border
        playButton.setBorder(BorderFactory.createEmptyBorder()); // Remove default border

        // Position the button dynamically
        int buttonWidth = 200;
        int buttonHeight = 80;
        int buttonX = (Toolkit.getDefaultToolkit().getScreenSize().width - buttonWidth) / 2 - 75; // Center horizontally
        int buttonY = (Toolkit.getDefaultToolkit().getScreenSize().height - buttonHeight) / 2 + 250; // Slightly below center
        playButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        // Add action listener for the "Play" button
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Add the "Play" button to the panel
        this.add(playButton);

        // Revalidate and repaint to ensure UI updates
        this.revalidate();
        this.repaint();
    }

    private void startGame() {
        // Switch to the game screen
        GameScreen gameScreen = new GameScreen();
        window.getContentPane().removeAll();
        window.add(gameScreen);
        window.revalidate();
        window.repaint();
        gameScreen.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private Font loadPixelFont(float size) {
        try {
            // Load a pixelated game font from resources
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/pixel.ttf"));
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, (int) size); // Fallback font
        }
    }
}
