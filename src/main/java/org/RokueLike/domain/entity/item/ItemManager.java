package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.Direction;

import java.util.Random;

public class ItemManager {

    private HallGrid hallGrid;
    private Hero hero;
    private MonsterManager monsterManager;

    public ItemManager(HallGrid hallGrid, Hero hero, MonsterManager monsterManager) {
        this.hallGrid = hallGrid;
        this.hero = hero;
        this.monsterManager = monsterManager;
    }

    public void spawnEnchantment() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Enchantment enchantment = generateRandomEnchantment(location[0], location[1]);
        hallGrid.addEnchantment(enchantment);
    }

    public void disappearEnchantment() {
        if (hallGrid.getCurrentEnchantment() != null) {
            hallGrid.removeEnchantment();
        }
    }

    public String collectEnchantment() {
        Enchantment currentEnchantment = hallGrid.getCurrentEnchantment();

        if (currentEnchantment.getEnchantmentType() == Enchantment.EnchantmentType.EXTRA_TIME) {
            hero.addRemainingTime(5);
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

    public String useEnchantment(Enchantment.EnchantmentType enchantment, Direction direction) {
        return switch (enchantment) {
            case REVEAL -> applyReveal();
            case CLOAK_OF_PROTECTION -> applyCloakOfProtection();
            case LURING_GEM -> applyLuringGem(direction);
            default -> "Invalid enchantment type.";
        };
    }

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

    public String applyCloakOfProtection() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION)) {
            hero.useEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);
            GameManager.setCloakActive(true);
            return "Cloak of Protection enchantment applied. Archer Monsters can't attack you.";
        } else {
            return "No Cloak of Protection enchantment available in hero's inventory.";
        }
    }


    public String applyLuringGem(Direction direction) {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.LURING_GEM)) {
            hero.useEnchantment(Enchantment.EnchantmentType.LURING_GEM);
            monsterManager.processLuringGem(direction);
            return "Luring Fighter Monsters in the direction " + direction.name();
        } else {
            return "No Luring Gem enchantment available in hero's inventory.";
        }
    }

    public String interactWithObject(Object object) {
        if (object.containsRune()) {
            object.removeContainedRune();
            hallGrid.openDoor();
            // TODO: Play a sound indicating the door is open
            return "Congratulations! You found the rune, door is unlocked!";
        }
        return null;
    }

    public Enchantment generateRandomEnchantment(int x, int y) {
        Enchantment.EnchantmentType[] enchantmentTypes = Enchantment.EnchantmentType.values();
        Enchantment.EnchantmentType randomType = enchantmentTypes[new Random().nextInt(enchantmentTypes.length)];
        return new Enchantment(randomType, x, y);
    }

}