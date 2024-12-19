package org.RokueLike.ui.screen;

import org.RokueLike.ui.Textures;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class HelpScreen extends JPanel {

    public HelpScreen() {
        // Set a custom layout
        this.setLayout(new BorderLayout());

        // Set background with custom rendering (to fill with floor_plain sprite)
        this.setBackground(Color.BLACK);

        // Create the header
        JLabel header = new JLabel("Game Help", SwingConstants.CENTER);
        header.setFont(new Font("Pixelated", Font.BOLD, 36)); // Use the pixelized font
        header.setForeground(Color.WHITE);
        this.add(header, BorderLayout.NORTH);

        // Create the content panel with help text
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Allow the background to show through

        String[] helpLines = {
                "Welcome to the game! Here are some helpful tips:",
                "- Use arrow keys to move your hero around the map.",
                "- Right-click objects near the hero to interact with them.",
                "- Left-click on enchantments to collect them.",
                "- Complete Build Mode to enter Play Mode and find the runes.",
                "- Collect items and enchantments to survive and succeed.",
                "- Use the UI to track progress and stats like time and lives."
        };
        for (String line : helpLines) {
            JLabel helpText = new JLabel(line);
            helpText.setFont(new Font("Pixelated", Font.BOLD, 18)); // Use the pixelized font
            helpText.setForeground(Color.WHITE);
            helpText.setAlignmentX(CENTER_ALIGNMENT); // Center-align text
            contentPanel.add(helpText);
        }
        this.add(contentPanel, BorderLayout.CENTER);

        // Create the back button
        JButton backButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Draw wall_center sprite as the button background
                g.drawImage(Textures.getSprite("wall_center"), 0, 0, getWidth(), getHeight(), null);

                // Draw the "BACK" label over the button
                g.setFont(new Font("Pixelated", Font.BOLD, 20)); // Use the pixelized font
                g.setColor(Color.WHITE); // Text color
                FontMetrics fm = g.getFontMetrics();
                String text = "BACK";
                int textX = (getWidth() - fm.stringWidth(text)) / 2; // Center text horizontally
                int textY = (getHeight() + fm.getAscent()) / 2 - fm.getDescent(); // Center text vertically
                g.drawString(text, textX, textY);
            }
        };
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false); // Remove default button background
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> Window.showScreen("LaunchScreen"));

        // Add the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Allow background to show through
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Render the floor_plain sprite as the background
    private void renderBackground(Graphics2D g, int width, int height) {
        int tilesWide = width / 16;
        int tilesHigh = height / 16;

        for (int y = 0; y <= tilesHigh; y++) {
            for (int x = 0; x <= tilesWide; x++) {
                g.drawImage(Textures.getSprite("floor_plain"), x * 16,
                        y * 16, 16, 16, null);
            }
        }
    }
}
