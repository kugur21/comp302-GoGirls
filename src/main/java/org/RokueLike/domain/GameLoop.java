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
            // Check if the game is paused; skip the loop if true
            if (GameManager.isPaused()) {
                return;
            }

            // Update hero's remaining time
            TimeManager.updateRemainingTime();

            // Monster spawn logic
            TimeManager.incrementMonsterSpawnTimer();
            if (TimeManager.isMonsterSpawnTimerReady()) {
                GameManager.handleMonsterSpawn(); // Spawns monsters
                TimeManager.resetMonsterSpawnTimer();
            }

            // Wizard behavior logic
            if (GameManager.hasWizardsInCurrentHall()) {
                TimeManager.incrementWizardTimer();
                if (TimeManager.isWizardTimerReady()) {
                    GameManager.handleWizardBehavior(); // Wizards teleport runes
                    TimeManager.resetWizardTimer();
                }
            }

            // Enchantment spawn logic
            TimeManager.incrementEnchantmentSpawnTimer();
            if (TimeManager.isEnchantmentSpawnTimerReady()) {
                GameManager.handleEnchantmentSpawn(); // Spawn enchantments
                TimeManager.resetEnchantmentSpawnTimer();
                TimeManager.resetEnchantmentDurationTimer();
            }

            // Enchantment expiration logic
            if (GameManager.getCurrentHall().getCurrentEnchantment() != null) {
                TimeManager.incrementEnchantmentDurationTimer();
                if (TimeManager.isEnchantmentDurationTimerReady()) {
                    GameManager.handleEnchantmentExpiration(); // Handle expiration
                    TimeManager.resetEnchantmentDurationTimer();
                }
            }

            // Reveal enchantment logic
            if (GameManager.isRevealActive()) {
                TimeManager.incrementRevealTimer();
                if (TimeManager.isRevealTimerReady()) {
                    GameManager.setRevealActive(false); // Deactivate reveal
                    TimeManager.resetRevealTimer();
                }
            }

            // Cloak of Protection logic
            if (GameManager.isCloakActive()) {
                TimeManager.incrementCloakTimer();
                if (TimeManager.isCloakTimerReady()) {
                    GameManager.setCloakActive(false); // Deactivate clock
                    TimeManager.resetCloakTimer();
                }
            }

            // Update lured monsters (if any)
            GameManager.handleLureBehaviour();

            // Update archer arrows (if any)
            GameManager.handleArcherArrows();

            // Handle hero-monster interactions
            GameManager.handleMonsterHit();

            // Hero movement logic
            if (Keyboard.isKeyPressed("UP")) {
                GameManager.handleMovement(0, -1); // Move up
            } else if (Keyboard.isKeyPressed("DOWN")) {
                GameManager.handleMovement(0, 1); // Move down
            } else if (Keyboard.isKeyPressed("LEFT")) {
                GameManager.handleMovement(-1, 0); // Move left
                GameManager.getHero().setFacing(Direction.LEFT);
            } else if (Keyboard.isKeyPressed("RIGHT")) {
                GameManager.handleMovement(1, 0); // Move right
                GameManager.getHero().setFacing(Direction.RIGHT);
            }
            // Monster movement logic
            TimeManager.incrementMonsterMovementTimer();
            if (TimeManager.isMonsterMovementReady()) {
                GameManager.handleMonsterMovement(); // Move monsters
                TimeManager.resetMonsterMovementTimer();
            }

            // Keyboard input for using enchantments
            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.handleEnchantmentUse(EnchantmentType.REVEAL); // Activate Reveal
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.handleEnchantmentUse(EnchantmentType.CLOAK_OF_PROTECTION); // Activate Cloak of Protection
            }
            if (Keyboard.isKeyPressed("USE_LURING")) {
                GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM); // Activate Luring Gem
            }

            // Luring gem directional logic
            if (GameManager.isLureActive()) {
                if (Keyboard.isKeyPressed("LURE_UP")) {
                    GameManager.handleFighterLuring(Direction.UP);
                    GameManager.setLureActive(false); // Deactivate luring
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.handleFighterLuring(Direction.DOWN);
                    GameManager.setLureActive(false);
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.handleFighterLuring(Direction.LEFT);
                    GameManager.setLureActive(false);
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.handleFighterLuring(Direction.RIGHT);
                    GameManager.setLureActive(false);
                }
            }

        } catch (Exception e) {
            System.out.println("[GameLoop] Error in GameLoop");
            System.exit(-1);
        }

    }
}