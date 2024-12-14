package org.RokueLike.domain;

import org.RokueLike.domain.utils.Direction;
import org.RokueLike.ui.Keyboard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            GameManager.genericLoop();

            if (!Keyboard.isLuringMode()) {
                if (Keyboard.isKeyPressed("UP")) {
                    GameManager.handlePlayerMovement(0, 1);
                } else if (Keyboard.isKeyPressed("DOWN")) {
                    GameManager.handlePlayerMovement(0, -1);
                } else if (Keyboard.isKeyPressed("LEFT")) {
                    GameManager.handlePlayerMovement(-1, 0);
                    GameManager.getHero().setFacing(Direction.LEFT);
                } else if (Keyboard.isKeyPressed("RIGHT")) {
                    GameManager.handlePlayerMovement(1, 0);
                    GameManager.getHero().setFacing(Direction.RIGHT);
                }
            } else {
                if (Keyboard.isKeyPressed("LURE_UP")) {
                    GameManager.useEnchantment("LURE", Direction.UP);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.useEnchantment("LURE", Direction.DOWN);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.useEnchantment("LURE", Direction.LEFT);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.useEnchantment("LURE", Direction.RIGHT);
                    Keyboard.deactivateLuringMode();
                }
            }

            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.useEnchantment("REVEAL");
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.useEnchantment("REVEAL");
            }

        } catch (Exception e) {
            System.out.println("Error in GameLoop");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
