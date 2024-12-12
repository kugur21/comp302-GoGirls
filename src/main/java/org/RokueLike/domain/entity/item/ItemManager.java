package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.entity.hero.Hero;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private Enchantment currentEnchantment;
    private List<Object> objects;

    public ItemManager() {
        this.currentEnchantment = null;
        this.objects = new ArrayList<>();
    }

    /**
     * Adds an object to the manager.
     * @param object The object to add.
     */
    public void addObject(Object object) {
        objects.add(object);
    }

    /**
     * Removes an object from the manager.
     * @param object The object to remove.
     */
    public void removeObject(Object object) {
        objects.remove(object);
    }

    /**
     * Retrieves all objects managed by this manager.
     * @return A list of objects.
     */
    public List<Object> getObjects() {
        return objects;
    }

    /**
     * Handles interaction with an object.
     * @param object The object being interacted with.
     * @param hero The hero interacting with the object.
     */
    public void interactWithObject(Object object, Hero hero) {

    }

    /**
     * Spawns a new enchantment in the game.
     * @param enchantment The enchantment to spawn.
     */
    public void spawnEnchantment(Enchantment enchantment) {
        this.currentEnchantment = enchantment;
    }

    /**
     * Removes the current enchantment.
     */
    public void removeEnchantment() {
        this.currentEnchantment = null;
    }

    /**
     * Retrieves the current enchantment.
     * @return The current enchantment, or null if none exists.
     */
    public Enchantment getCurrentEnchantment() {
        return currentEnchantment;
    }

}
