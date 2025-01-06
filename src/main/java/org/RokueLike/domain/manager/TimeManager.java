package org.RokueLike.domain.manager;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

import static org.RokueLike.utils.Constants.*;

public class TimeManager {

    private static int monsterSpawnTimer = 0;
    private static int monsterMovementTimer = 0;
    private static int enchantmentSpawnTimer = 0;
    private static int enchantmentDurationTimer = 0;
    private static int revealTimer = 0;
    private static int cloakTimer = 0;
    private static int frameCounter = 0;

    // Remaining Time Clock
    public static void updateRemainingTime() {
        frameCounter++;
        if (frameCounter >= (1000 / GAME_DELAY)) {
            frameCounter = 0;
            if (GameManager.getHero().getRemainingTime() > 0) {
                GameManager.getHero().decrementRemainingTime();
            } else {
                String message = "Game Over! Time is Over";
                GameManager.reset();
                Window.addScreen(new GameOverScreen(message), "GameOverScreen", true);
            }
        }
    }

    // Monster Spawn Clock
    public static void incrementMonsterSpawnTimer() {
        monsterSpawnTimer++;
    }
    public static boolean isMonsterSpawnTimerReady() {
        return monsterSpawnTimer >= (MONSTER_SPAWN / GAME_DELAY);
    }
    public static void resetMonsterSpawnTimer() {
        monsterSpawnTimer = 0;
    }

    // Monster Movement Clock
    public static void incrementMonsterMovementTimer() {
        monsterMovementTimer++;
    }
    public static boolean isMonsterMovementReady() {
        return monsterMovementTimer >= (MONSTER_MOVEMENT_DELAY / GAME_DELAY);
    }
    public static void resetMonsterMovementTimer() {
        monsterMovementTimer = 0;
    }

    // Enchantment Spawn Clock
    public static void incrementEnchantmentSpawnTimer() {
        enchantmentSpawnTimer++;
    }
    public static boolean isEnchantmentSpawnTimerReady() {
        return enchantmentSpawnTimer >= (ENCHANTMENT_SPAWN / GAME_DELAY);
    }
    public static void resetEnchantmentSpawnTimer() {
        enchantmentSpawnTimer = 0;
    }

    // Enchantment Duration Clock
    public static void incrementEnchantmentDurationTimer() {
        enchantmentDurationTimer++;
    }
    public static boolean isEnchantmentDurationTimerReady() {
        return enchantmentDurationTimer >= (ENCHANTMENT_DURATION / GAME_DELAY);
    }
    public static void resetEnchantmentDurationTimer() {
        enchantmentDurationTimer = 0;
    }

    // Reveal Enchantment Clock
    public static void incrementRevealTimer() {
        revealTimer++;
    }
    public static boolean isRevealTimerReady() {
        return revealTimer >= (REVEAL_ENCHANTMENT_DURATION / GAME_DELAY);
    }
    public static void resetRevealTimer() {
        revealTimer = 0;
    }
    public static int remainingRevealTimer() {
        if (!GameManager.isRevealActive()) {
            return 0;
        }
        int elapsedTime = revealTimer * GAME_DELAY;
        int remainingTime = (REVEAL_ENCHANTMENT_DURATION - elapsedTime) / 1000;
        return Math.max(remainingTime, 0);
    }

    // Cloak of Protection Cloak
    public static void incrementCloakTimer() {
        cloakTimer++;
    }
    public static boolean isCloakTimerReady() {
        return  cloakTimer >= (CLOAK_ENCHANTMENT_DURATION / GAME_DELAY);
    }
    public static void resetCloakTimer() {
        cloakTimer = 0;
    }
    public static int remainingCloakTimer() {
        if (!GameManager.isCloakActive()) {
            return 0;
        }
        int elapsedTime = cloakTimer * GAME_DELAY;
        int remainingTime = (CLOAK_ENCHANTMENT_DURATION - elapsedTime) / 1000;
        return Math.max(remainingTime, 0);
    }

    // Resets the spawn and behaviour timers
    public static void hallReset() {
        monsterSpawnTimer = 0;
        enchantmentSpawnTimer = 0;
        enchantmentDurationTimer = 0;

        revealTimer = 0;
        cloakTimer = 0;
    }

    // Resets all the components
    public static void gameReset() {
        hallReset();
        monsterMovementTimer = 0;
        frameCounter = 0;
    }

}