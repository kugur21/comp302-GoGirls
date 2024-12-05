package org.RokueLike;

import javax.swing.*;
import org.RokueLike.ui.LaunchScreen;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Create the game window
        JFrame window = new JFrame("Rokuelike Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set custom size close to fullscreen (e.g., 90% of screen size)
        int screenWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9);
        int screenHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.9);
        window.setSize(screenWidth, screenHeight);

        // Center the window on the screen
        window.setLocationRelativeTo(null);

        // Add the launch screen
        LaunchScreen launchScreen = new LaunchScreen(window);
        window.add(launchScreen);

        // Set the window to be visible
        window.setVisible(true);
    }
}
