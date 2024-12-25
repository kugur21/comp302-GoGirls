package org.RokueLike.ui.screen;

import org.RokueLike.ui.Textures;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

public class HelpScreen extends JPanel {

    private Font customFont;

    public HelpScreen() {
        // Load the custom font
        loadCustomFont();

        // Set a custom layout
        this.setLayout(new BorderLayout());

        // Set background with custom rendering (to fill with floor_plain sprite)
        this.setBackground(new Color(66,40,53));

        // Create the header
        JLabel header = new JLabel("Game Help", SwingConstants.CENTER);
        header.setFont(customFont.deriveFont(Font.BOLD, 25)); // Use the custom font
        header.setForeground(Color.WHITE);
        this.add(header, BorderLayout.NORTH);

        // Create a custom panel for centered text with spacing
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        contentPanel.setOpaque(false); // Allow the background to show through

        // Add help text lines with vertical spacing
        String[] helpLines = {
                "Welcome to the game! Here are some helpful tips:",
                "- Use arrow keys to move your hero around the map.",
                "- Right-click objects near the hero to interact with them.",
                "- Left-click on enchantments to collect them.",
                "- Complete Build Mode to enter Play Mode and find the runes.",
                "- Collect items and enchantments to survive and succeed.",
                "- Use the UI to track progress and stats like time and lives.",
                "- Press P for Cloak of Protection activation.",
                "- Press R for Reveal enchantment activation.",
                "- Press B for Luring Gem activation. Then select a direction:",
                "- Press A for left, W for up, S for down and D for right direction.",
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Add vertical spacing (10px)

        for (String line : helpLines) {
            JLabel helpText = new JLabel(line);
            helpText.setFont(customFont.deriveFont(Font.BOLD, 12)); // Use the custom font
            helpText.setForeground(Color.WHITE);
            contentPanel.add(helpText, gbc);
            gbc.gridy++; // Move to the next row
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
                g.setFont(customFont.deriveFont(Font.BOLD, 20)); // Use the custom font
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

    /**
     * Loads the custom font from the resources folder.
     */
    private void loadCustomFont() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/PressStart2P-Regular.ttf");
            if (is == null) {
                throw new RuntimeException("Font file not found in resources/fonts.");
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            System.out.println("[HelpScreen]: Custom font loaded successfully.");
        } catch (Exception e) {
            System.err.println("[HelpScreen]: Failed to load custom font. Using default font.");
            customFont = new Font("Arial", Font.PLAIN, 12); // Fallback font
        }
    }

    /**
     * Renders the background with "floor_plain" tiles.
     */
    private void renderBackground(Graphics2D g, int width, int height) {
        BufferedImage floorSprite = Textures.getSprite("floor_plain");
        if (floorSprite == null) {
            g.setColor(Color.DARK_GRAY); // Fallback color
            g.fillRect(0, 0, width, height);
            return;
        }

        int tilesWide = width / floorSprite.getWidth() + 1;
        int tilesHigh = height / floorSprite.getHeight() + 1;

        for (int y = 0; y < tilesHigh; y++) {
            for (int x = 0; x < tilesWide; x++) {
                g.drawImage(floorSprite, x * floorSprite.getWidth(), y * floorSprite.getHeight(), null);
            }
        }
    }
}