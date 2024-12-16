package org.RokueLike;

import org.RokueLike.ui.Window;


public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("[Main]: Starting...");
            Window.create();
            System.out.println("[Main]: Started!");
        } catch(Exception e) {
            System.err.println("\n[Main]: Uncaught exception in initialization!\n");
            System.exit(-1);
        }
    }

    /*
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
    */
}
