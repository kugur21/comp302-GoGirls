package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.Direction;

import javax.swing.*;
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

    public String spawnEnchantment() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return "No safe location available to spawn enchantment.";
        }
        Enchantment enchantment = generateRandomEnchantment(location[0], location[1]);
        hallGrid.addEnchantment(enchantment);
        return "Enchantment spawned: " + enchantment.getEnchantmentType().getName() +
                " at (" + location[0] + ", " + location[1] + ")";
    }

    public String disappearEnchantment() {
        if (hallGrid.getCurrentEnchantment() != null) {
            hallGrid.removeEnchantment();
            return "Enchantment removed from grid.";
        }
        return "No enchantment to remove.";
    }

    public String collectEnchantment() {
        Enchantment currentEnchantment = hallGrid.getCurrentEnchantment();

        if (currentEnchantment.getEnchantmentType() == Enchantment.EnchantmentType.EXTRA_TIME) {
            hero.addRemainingTime(5);
            hallGrid.removeEnchantment();
            return "Collected Extra Time enchantment!";
        } else if (currentEnchantment.getEnchantmentType() == Enchantment.EnchantmentType.EXTRA_LIFE) {
            hero.incrementLives();
            hallGrid.removeEnchantment();
            return "Collected Extra Life enchantment!";
        } else {
            hero.addToInventory(currentEnchantment.getEnchantmentType());
            hallGrid.removeEnchantment();
            return "Collected " + currentEnchantment.getEnchantmentType().getName() + " enchantment!";
        }
    }

    public String useEnchantment(Enchantment.EnchantmentType enchantment, Direction direction) {
        switch (enchantment) {
            case LURING_GEM:
                return applyLuringGem(direction);
            case CLOAK_OF_PROTECTION:
                return applyCloakOfProtection();
            case REVEAL:
                return applyReveal();
            default:
                return "Invalid enchantment type.";
        }
    }

    public String applyReveal() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)) {
            hero.useEnchantment(Enchantment.EnchantmentType.REVEAL);
            int[][] runeRegion = hallGrid.findRuneRegion(4);
            highlightRegion(runeRegion);
            System.out.println("Reveal applied. Highlighting region for 10 seconds.");
            Timer revealTimer = new Timer(10000, e -> removeHighlight(runeRegion));
            revealTimer.setRepeats(false);
            revealTimer.start();
            return "Reveal enchantment applied.";
        } else {
            return "Hero does not have a Reveal Enchantment.";
        }
    }

    public String applyCloakOfProtection() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION)) {
            hero.useEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);

            // Back-end eklentisi UI için
            hero.activateCloak(20);

            monsterManager.processCloakOfProtection(20);
            return "Cloak of Protection enchantment applied.";
        } else {
            return "No Cloak of Protection enchantment available in hero's inventory.";
        }
    }


    public String applyLuringGem(Direction direction) {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.LURING_GEM)) {
            hero.useEnchantment(Enchantment.EnchantmentType.LURING_GEM);
            monsterManager.processLuringGem(direction);
            return "Luring Gem enchantment applied.";
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
        } else {
            return "The object is empty. Keep looking!";
        }
    }

    public Enchantment generateRandomEnchantment(int x, int y) {
        Enchantment.EnchantmentType[] enchantmentTypes = Enchantment.EnchantmentType.values();
        Enchantment.EnchantmentType randomType = enchantmentTypes[new Random().nextInt(enchantmentTypes.length)];
        return new Enchantment(randomType, x, y);
    }

    private void highlightRegion(int[][] runeRegion) {
        // TODO: Implement highlight logic in UI (e.g., a grid overlay).
    }

    private void removeHighlight(int[][] runeRegion) {
        // TODO: Remove the highlight after 10 seconds.
    }
}