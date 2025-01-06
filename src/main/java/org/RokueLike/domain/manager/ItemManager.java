package org.RokueLike.domain.manager;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Enchantment;
import org.RokueLike.domain.model.item.Object;
import org.RokueLike.domain.hall.HallGrid;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

import static org.RokueLike.utils.Constants.*;

public class ItemManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final HallGrid hallGrid; // The current hall grid
    private final Hero hero; // The hero instance
    private final boolean spawn; // Whether to spawn enchantments or not

    public ItemManager(HallGrid hallGrid, Hero hero, MonsterManager monsterManager, boolean spawn) {
        this.hallGrid = hallGrid;
        this.hero = hero;
        this.spawn = spawn;
    }

    // Spawns a random enchantment at a safe location in the hall.
    public void spawnEnchantment() {
        if (!spawn) {
            return;
        }
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Enchantment enchantment = generateRandomEnchantment(location[0], location[1]);
        hallGrid.addEnchantment(enchantment);
    }

    // Removes the current enchantment from the hall.
    public void disappearEnchantment() {
        if (hallGrid.getCurrentEnchantment() != null) {
            hallGrid.removeEnchantment();
        }
    }

    // Collects the current enchantment and applies its effect to the hero.
    public String collectEnchantment() {
        Enchantment currentEnchantment = hallGrid.getCurrentEnchantment();

        if (currentEnchantment.getEnchantmentType() == Enchantment.EnchantmentType.EXTRA_TIME) {
            hero.addRemainingTime(EXTRA_TIME);
            hallGrid.removeEnchantment();
            return "Collected Extra Time! Gained 5 extra seconds.";
        } else if (currentEnchantment.getEnchantmentType() == Enchantment.EnchantmentType.EXTRA_LIFE) {
            hero.incrementLives();
            hallGrid.removeEnchantment();
            return "Collected Extra Life! Gained an extra life.";
        } else {
            hero.addToInventory(currentEnchantment.getEnchantmentType());
            hallGrid.removeEnchantment();
            return switch (currentEnchantment.getEnchantmentType()) {
                case CLOAK_OF_PROTECTION -> "Collected Cloak of Protection!";
                case REVEAL -> "Collected Reveal Enchantment!";
                case LURING_GEM -> "Collected Luring Gem!";
                default -> "Enchantment type not recognized!";
            };
        }
    }

    // Uses a specific enchantment from the hero's inventory.
    public String useEnchantment(Enchantment.EnchantmentType enchantment, MonsterManager monsterManager) {
        return switch (enchantment) {
            case REVEAL -> applyReveal();
            case CLOAK_OF_PROTECTION -> applyCloakOfProtection(monsterManager);
            case LURING_GEM -> applyLuringGem();
            default -> "Invalid enchantment type.";
        };
    }

    // Applies the Reveal enchantment, revealing the rune's region.
    public String applyReveal() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)) {
            hero.useEnchantment(Enchantment.EnchantmentType.REVEAL);
            int[][] runeRegion = hallGrid.findRuneRegion(4);
            if (runeRegion != null) {
                GameManager.setRevealActive(true);
                return "Revealed rune region!";
            }
            return "Rune already found. You can move to the next hall.";
        } else {
            return "Hero does not have a Reveal Enchantment.";
        }
    }

    // Applies the Cloak of Protection enchantment, preventing archer attacks.
    public String applyCloakOfProtection(MonsterManager monsterManager) {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION)) {
            hero.useEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);
            GameManager.setCloakActive(true);
            monsterManager.clearArrows();
            return "Cloak of Protection active. Archers can't attack you.";
        } else {
            return "Hero does not have a Cloak of Protection.";
        }
    }

    // Applies the Luring Gem enchantment, allowing the hero to lure nearby fighter monsters.
    public String applyLuringGem() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.LURING_GEM)) {
            hero.useEnchantment(Enchantment.EnchantmentType.LURING_GEM);
            GameManager.setLureActive(true);
            return "Activating Luring Gem! Choose the direction (A,W,S,D)";
        } else {
            return "Hero does not have a Luring Gem.";
        }
    }

    // Interacts with an object on the grid, unlocking the door if it contains a rune.
    public String interactWithObject(Object object) {
        if (object.containsRune()) {
            object.removeContainedRune();
            hallGrid.openDoor();
            // TODO: Play a sound indicating the door is open
            return "You found the rune! Door is unlocked.";
        }
        return null;
    }

    // Generates a random enchantment at a specific position.
    public Enchantment generateRandomEnchantment(int x, int y) {
        Enchantment.EnchantmentType[] enchantmentTypes = Enchantment.EnchantmentType.values();
        Enchantment.EnchantmentType randomType = enchantmentTypes[new Random().nextInt(enchantmentTypes.length)];
        return new Enchantment(randomType, x, y);
    }
}