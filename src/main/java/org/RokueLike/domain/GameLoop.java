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

            if (GameManager.hasWizardsInCurrentHall()) {
                GameManager.incrementWizardTimer();
                if (GameManager.isWizardTimerReady()) {
                    GameManager.handleWizardBehavior();
                    GameManager.resetWizardTimer();
                }
            }

            GameManager.incrementMonsterSpawnTimer();
            if (GameManager.isMonsterSpawnTimerReady()) {
                GameManager.handleMonsterSpawn();
                GameManager.resetMonsterSpawnTimer();
            }


            if (!Keyboard.isLuringMode()) {
                if (Keyboard.isKeyPressed("UP")) {
                    GameManager.handleMovement(0, 1);
                } else if (Keyboard.isKeyPressed("DOWN")) {
                    GameManager.handleMovement(0, -1);
                } else if (Keyboard.isKeyPressed("LEFT")) {
                    GameManager.handleMovement(-1, 0);
                    GameManager.getHero().setFacing(Direction.LEFT);
                } else if (Keyboard.isKeyPressed("RIGHT")) {
                    GameManager.handleMovement(1, 0);
                    GameManager.getHero().setFacing(Direction.RIGHT);
                }
            } else {
                if (Keyboard.isKeyPressed("LURE_UP")) {
                    GameManager.handleEnchantmentUse("LURE", Direction.UP);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.handleEnchantmentUse("LURE", Direction.DOWN);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.handleEnchantmentUse("LURE", Direction.LEFT);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.handleEnchantmentUse("LURE", Direction.RIGHT);
                    Keyboard.deactivateLuringMode();
                }
            }

            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.handleEnchantmentUse("REVEAL");
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.handleEnchantmentUse("REVEAL");
            }

        } catch (Exception e) {
            System.out.println("Error in GameLoop");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
