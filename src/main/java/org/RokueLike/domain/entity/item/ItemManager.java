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

    public void spawnEnchantment() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            System.out.println("No safe location available to spawn enchantment.");
            return;
        }
        Enchantment enchantment = generateRandomEnchantment(location[0], location[1]);
        hallGrid.addEnchantment(enchantment);
        System.out.println("Enchantment spawned: " + enchantment.getEnchantmentType().getName() +
                " at (" + location[0] + ", " + location[1] + ")");
    }

    public void disappearEnchantment() {
        if (hallGrid.getCurrentEnchantment() != null) {
            hallGrid.removeEnchantment();
            System.out.println("Enchantment removed from grid.");
        }
    }

    public void useEnchantment(Enchantment.EnchantmentType enchantment, Direction direction) {
        switch (enchantment) {
            case LURING_GEM:
                applyLuringGem(direction);
                break;
            case CLOAK_OF_PROTECTION:
                applyCloakOfProtection();
                break;
            case REVEAL:
                applyReveal();
                break;
            default:
                System.out.println("Invalid enchantment type.");
        }
    }

    public void applyReveal() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)) {
            hero.useEnchantment(Enchantment.EnchantmentType.REVEAL);
            int[][] runeRegion = hallGrid.findRuneRegion(4);
            highlightRegion(runeRegion);
            System.out.println("Reveal applied. Highlighting region for 10 seconds.");
            Timer revealTimer = new Timer(10000, e -> removeHighlight(runeRegion));
            revealTimer.setRepeats(false);
            revealTimer.start();
            System.out.println("Reveal enchantment applied.");
        } else {
            System.out.println("Hero does not have a Reveal Enchantment.");
        }
    }

    public void applyCloakOfProtection() {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION)) {
            hero.useEnchantment(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);
            monsterManager.processCloakOfProtection(20);
        } else {
            System.out.println("No Cloak of Protection enchantment available in hero's inventory.");
        }
    }

    public void applyLuringGem(Direction direction) {
        if (hero.hasEnchantment(Enchantment.EnchantmentType.LURING_GEM)) {
            hero.useEnchantment(Enchantment.EnchantmentType.LURING_GEM);
            monsterManager.processLuringGem(direction);
        } else {
            System.out.println("No Luring Gem enchantment available in hero's inventory.");
        }
    }

    /**
     * Handles interaction with an object.
     * @param object The object being interacted with.
     * @param hero The hero interacting with the object.
     */
    public void interactWithObject(Object object, Hero hero) {
        // TODO: Implement this method.
    }

    public Enchantment getCurrentEnchantment() {
        return hallGrid.getCurrentEnchantment();
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
