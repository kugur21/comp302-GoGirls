package org.RokueLike.domain;

import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.utils.Direction;
import org.RokueLike.ui.input.Keyboard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            GameManager.genericLoop();

            GameManager.updateRemainingTime();

            GameManager.incrementMonsterSpawnTimer();
            if (GameManager.isMonsterSpawnTimerReady()) {
                GameManager.handleMonsterSpawn();
                GameManager.resetMonsterSpawnTimer();
            }

            GameManager.incrementEnchantmentSpawnTimer();
            if (GameManager.isEnchantmentSpawnTimerReady()) {
                GameManager.handleEnchantmentSpawn();
                GameManager.resetEnchantmentSpawnTimer();
            }
            GameManager.handleEnchantmentExpiration();

            if (GameManager.hasWizardsInCurrentHall()) {
                GameManager.incrementWizardTimer();
                if (GameManager.isWizardTimerReady()) {
                    GameManager.handleWizardBehavior();
                    GameManager.resetWizardTimer();
                }
            }
            GameManager.handleLureBehaviour();

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
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.UP);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.DOWN);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.LEFT);
                    Keyboard.deactivateLuringMode();
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.RIGHT);
                    Keyboard.deactivateLuringMode();
                }
            }

            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.handleEnchantmentUse(EnchantmentType.REVEAL);
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.handleEnchantmentUse(EnchantmentType.CLOAK_OF_PROTECTION);
            }

        } catch (Exception e) {
            System.out.println("Error in GameLoop");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}