package org.RokueLike;

import javax.swing.*;
import org.RokueLike.ui.GameScreen;

public class Main {
    public static void main(String[] args) {
        // Create the game window
        JFrame window = new JFrame("Rokuelike Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        window.setUndecorated(true); // Remove window borders for full screen

        // Add the game screen
        GameScreen gameScreen = new GameScreen();
        window.add(gameScreen);

        // Set the window to be visible
        window.setVisible(true);
    }
}
