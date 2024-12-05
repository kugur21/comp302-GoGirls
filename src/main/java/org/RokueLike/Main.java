package org.RokueLike;

import org.RokueLike.ui.GameScreen;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Rokuelike Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 500);
        window.setResizable(false);

        GameScreen gameScreen = new GameScreen();
        window.add(gameScreen);
        window.setVisible(true);
    }
}
