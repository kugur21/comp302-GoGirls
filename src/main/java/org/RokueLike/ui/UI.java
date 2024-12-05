package org.RokueLike.ui;

import java.awt.*;

public class UI {
    public static void drawHealthBar(Graphics g, int x, int y, int health, int maxHealth) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 100, 10);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, (int) ((health / (double) maxHealth) * 100), 10);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, 100, 10);
    }
}
