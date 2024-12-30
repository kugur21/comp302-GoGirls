package org.RokueLike.domain.loop;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.manager.TimeManager;
import org.RokueLike.utils.Direction;
import org.RokueLike.ui.input.Keyboard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.RokueLike.utils.Constants.*;

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

            // Wizard behavior logic
            if (GameManager.hasWizardsInCurrentHall()) {
                GameManager.updateWizardBehaviour();
                GameManager.handleWizardBehavior();
            }

            // Fighter and Archer interaction logic
            GameManager.handleMonsterHit();

            // Update Archer arrows (if any)
            GameManager.handleArcherArrows();

            // Update lured Fighters (if any)
            GameManager.handleLureBehaviour();

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
            if (Keyboard.isKeyPressed("USE_LURING")) {
                GameManager.handleEnchantmentUse(EnchantmentType.LURING_GEM); // Activate Luring Gem
            }
            if (Keyboard.isKeyPressed("USE_REVEAL")) {
                GameManager.handleEnchantmentUse(EnchantmentType.REVEAL); // Activate Reveal
            }
            if (Keyboard.isKeyPressed("USE_PROTECTION")) {
                GameManager.handleEnchantmentUse(EnchantmentType.CLOAK_OF_PROTECTION); // Activate Cloak of Protection
            }

            // Luring Gem logic
            if (GameManager.isLureActive()) {
                if (Keyboard.isKeyPressed("LURE_UP")) {
                    GameManager.handleFighterLuring(Direction.UP);
                    GameManager.setLureActive(false); // Deactivate luring
                } else if (Keyboard.isKeyPressed("LURE_DOWN")) {
                    GameManager.handleFighterLuring(Direction.DOWN);
                    GameManager.setLureActive(false); // Deactivate luring
                } else if (Keyboard.isKeyPressed("LURE_LEFT")) {
                    GameManager.handleFighterLuring(Direction.LEFT);
                    GameManager.setLureActive(false); // Deactivate luring
                } else if (Keyboard.isKeyPressed("LURE_RIGHT")) {
                    GameManager.handleFighterLuring(Direction.RIGHT);
                    GameManager.setLureActive(false); // Deactivate luring
                }
            }

            // Reveal logic
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
        } catch (Exception e) {
            System.out.println("[GameLoop] Error in GameLoop");
            System.exit(-1);
        }
    }

}