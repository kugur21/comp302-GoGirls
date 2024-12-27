package org.RokueLike.ui.screen;

import org.RokueLike.utils.FontLoader;
import org.RokueLike.utils.Textures;
import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class HelpScreen extends JPanel {

    private final Font customFont;

    public HelpScreen() {
        customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(66,40,53));

        JLabel header = new JLabel("Game Help", SwingConstants.CENTER);
        header.setFont(customFont.deriveFont(Font.BOLD, 25));
        header.setForeground(Color.WHITE);
        this.add(header, BorderLayout.NORTH);

        // Create the content panel for help text, with a custom background rendering
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Center the content
        contentPanel.setOpaque(false);

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
        gbc.insets = new Insets(10, 0, 10, 0); // Add vertical spacing between lines

        for (String line : helpLines) {
            JLabel helpText = new JLabel(line);
            helpText.setFont(customFont.deriveFont(Font.BOLD, 12));
            helpText.setForeground(Color.WHITE);
            contentPanel.add(helpText, gbc);
            gbc.gridy++; // Move to the next row
        }
        this.add(contentPanel, BorderLayout.CENTER);

        // Create a back button with a custom rendering
        JButton backButton = customJButton();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    // Creates a custom back button.
    private JButton customJButton() {
        JButton backButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(Textures.getSprite("wall_center"), 0, 0, getWidth(), getHeight(), null);
                g.setFont(customFont.deriveFont(Font.BOLD, 20));
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                String text = "BACK";
                int textX = (getWidth() - fm.stringWidth(text)) / 2; // Center horizontally
                int textY = (getHeight() + fm.getAscent()) / 2 - fm.getDescent(); // Center vertically
                g.drawString(text, textX, textY);
            }
        };
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false); // Remove default button background
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> Window.showScreen("LaunchScreen")); // Navigate to launch screen
        return backButton;
    }

    // Renders the background using "floor_plain" tiles.
    private void renderBackground(Graphics2D g, int width, int height) {
        BufferedImage floorSprite = Textures.getSprite("floor_plain");
        if (floorSprite == null) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, width, height);
            return;
        }

        int tilesWide = width / floorSprite.getWidth() + 1;
        int tilesHigh = height / floorSprite.getHeight() + 1;

        // Draw tiles to fill the entire background
        for (int y = 0; y < tilesHigh; y++) {
            for (int x = 0; x < tilesWide; x++) {
                g.drawImage(floorSprite, x * floorSprite.getWidth(), y * floorSprite.getHeight(), null);
            }
        }
    }

}