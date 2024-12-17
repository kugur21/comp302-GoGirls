package org.RokueLike.ui.screen;

import org.RokueLike.ui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class HelpScreen extends JPanel {

    public HelpScreen() {

        this.setLayout(new BorderLayout());

        JLabel header = new JLabel("Game Help", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.PLAIN, 36));
        header.setForeground(Color.WHITE);
        this.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

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
            helpText.setFont(new Font("Arial", Font.PLAIN, 18));
            helpText.setForeground(Color.WHITE);
            contentPanel.add(helpText);
        }
        this.add(contentPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("BACK");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window.showScreen("LaunchScreen");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

}