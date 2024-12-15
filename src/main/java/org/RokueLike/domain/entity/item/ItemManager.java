package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.Direction;

import java.util.Random;

public class ItemManager {

    private HallGrid hallGrid;

    public ItemManager(HallGrid hallGrid) {
        this.hallGrid = hallGrid;}

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
        // TODO: Implement this method.
    }

    public void applyCloakOfProtection() {
        // TODO: Implement this method.
    }

    public void applyLuringGem(Direction direction) {
        // TODO: Implement this method.
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

}
