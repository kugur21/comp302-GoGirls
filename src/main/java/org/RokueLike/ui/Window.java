package org.RokueLike.ui;

import org.RokueLike.ui.screen.HelpScreen;
import org.RokueLike.ui.screen.LaunchScreen;

import javax.swing.*;
import java.awt.*;

import static org.RokueLike.utils.Constants.*;

public class Window {

    private static JFrame window; // Main application window
    private static JPanel mainPanel; // Container for different screens
    private static CardLayout cardLayout; // Layout manager to switch screens

    // Creates the main game window with initial settings.
    public static void create() {
        window = new JFrame("RoKue-Like Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(BUILD_WINDOW_WIDTH, BUILD_WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LaunchScreen(), "LaunchScreen");
        mainPanel.add(new HelpScreen(), "HelpScreen");
        showScreen("LaunchScreen");

        window.add(mainPanel);
        window.setVisible(true);

        System.out.println("[Window]: Game window created successfully.");
    }

    // Displays the specified screen by name.
    public static void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
        System.out.println("[Window]: Switched to screen - " + screenName);

        // Ensure the displayed screen has focus
        Component currentScreen = mainPanel.getComponent(0);
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible()) {
                currentScreen = comp;
                break;
            }
        }
        if (currentScreen instanceof JPanel) {
            currentScreen.requestFocusInWindow();
            System.out.println("[Window]: Focus requested for " + screenName);
        }
    }

    // Adds a new screen to the main panel, shows based on preference.
    public static void addScreen(JPanel screen, String screenName, boolean show) {
        mainPanel.add(screen, screenName);
        if (show) {
            showScreen(screenName);
        }
    }

    public static JFrame getWindow() {
        return window;
    }

}