package org.RokueLike.domain;

import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.utils.Direction;
import org.RokueLike.ui.input.Keyboard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLoop implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            if (GameManager.isPaused()) {
                return;
            }
            GameManager.updateRemainingTime();
            GameManager.genericLoop();

            GameManager.getArrowManager().updateArrows(GameManager.getHero());

            GameManager.incrementMonsterSpawnTimer();
            if (GameManager.isMonsterSpawnTimerReady()) {
                GameManager.handleMonsterSpawn();
                GameManager.resetMonsterSpawnTimer();
            }

            if (GameManager.hasWizardsInCurrentHall()) {
                GameManager.incrementWizardTimer();
                if (GameManager.isWizardTimerReady()) {
                    GameManager.handleWizardBehavior();
                    GameManager.resetWizardTimer();
                }
            }

            GameManager.incrementEnchantmentSpawnTimer();
            if (GameManager.isEnchantmentSpawnTimerReady()) {
                GameManager.handleEnchantmentSpawn();
                GameManager.resetEnchantmentSpawnTimer();
            }
            GameManager.handleEnchantmentExpiration();

            if (GameManager.isRevealActive()) {
                GameManager.incrementRevealTimer();
                if (GameManager.isRevealTimerReady()) {
                    GameManager.setRevealActive(false);
                    GameManager.resetRevealTimer();
                }
            }

            if (GameManager.isCloakActive()) {
                GameManager.incrementCloakTimer();
                if (GameManager.isCloakTimerReady()) {
                    GameManager.setCloakActive(false);
                    GameManager.resetCloakTimer();
                }
            }

            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.handleEnchantmentUse(EnchantmentType.REVEAL);
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.handleEnchantmentUse(EnchantmentType.CLOAK_OF_PROTECTION);
            }
            if (Keyboard.isKeyPressed("USE_LURING")) {
                GameManager.setLureActive(true);
            }
            if (GameManager.isLureActive()) {
                if (Keyboard.isKeyPressed("LURE_UP")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.UP);
                    GameManager.setLureActive(false);
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.DOWN);
                    GameManager.setLureActive(false);
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.LEFT);
                    GameManager.setLureActive(false);
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM, Direction.RIGHT);
                    GameManager.setLureActive(false);
                }
            }

            if (Keyboard.isKeyPressed("UP")) {
                GameManager.handleMovement(0, -1);
            } else if (Keyboard.isKeyPressed("DOWN")) {
                GameManager.handleMovement(0, 1);
            } else if (Keyboard.isKeyPressed("LEFT")) {
                GameManager.handleMovement(-1, 0);
                GameManager.getHero().setFacing(Direction.LEFT);
            } else if (Keyboard.isKeyPressed("RIGHT")) {
                GameManager.handleMovement(1, 0);
                GameManager.getHero().setFacing(Direction.RIGHT);
            }
            GameManager.handleLureBehaviour();
            GameManager.incrementMonsterMovementTimer();
            if (GameManager.isMonsterMovementReady()) {
                GameManager.handleMonsterMovement();
                GameManager.resetMonsterMovementTimer();
            }

        } catch (Exception e) {
            System.out.println("[GameLoop] Error in GameLoop");
            System.exit(-1);
        }

    }
}