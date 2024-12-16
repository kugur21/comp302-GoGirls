package org.RokueLike.ui;

import org.RokueLike.ui.screen.BuildModeScreen;
import org.RokueLike.ui.screen.HelpScreen;
import org.RokueLike.ui.screen.LaunchScreen;

import javax.swing.*;
import java.awt.*;

public class Window {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;

    private static JFrame window;
    private static JPanel mainPanel;
    private static CardLayout cardLayout;

    public static void create() {

        window = new JFrame("RoKue-Like Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(WIDTH, HEIGHT);
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

    public static void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
        System.out.println("[Window]: Switched to screen - " + screenName);
    }

    public static void addScreen(JPanel screen, String screenName, boolean show) {
        mainPanel.add(screen, screenName);
        if (show) {
            showScreen(screenName);
        }
    }

    public static JFrame getWindow() {
        return window;
    }

    public static JPanel getMainPanel() {
        return mainPanel;
    }

}