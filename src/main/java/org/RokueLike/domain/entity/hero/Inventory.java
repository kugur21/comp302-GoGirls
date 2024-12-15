package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<String> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean hasEnchantment(EnchantmentType enchantment) {
        // TODO: Implement this method.
        return false;
    }

    /**
     * Adds an item to the inventory sequentially.
     * @param itemName The name of the item to add.
     */
    public void addItem(String itemName) {
        // TODO: Implement this method.
    }

    /**
     * Removes the item at the specified index from the inventory.
     * @param enchantment@return The removed item name.
     */
    public String removeItem(EnchantmentType enchantment) {
        // TODO: Implement this method.
        return null;
    }

    /**
     * Gets the item at the specified index without removing it.
     * @param index The index of the item.
     * @return The item name.
     */
    public String getItem(int index) {
        // TODO: Implement this method.
        return null;
    }

    /**
     * Gets all items in the inventory.
     * @return A list of all items.
     */
    public List<String> getAllItems() {
        // TODO: Implement this method.
        return null;
    }

    /**
     * Clears all items from the inventory.
     */
    public void clearInventory() {
        // TODO: Implement this method.
    }

    /**
     * Gets the total number of items in the inventory.
     * @return The number of items.
     */
    public int getItemCount() {
        // TODO: Implement this method.
        return 0;
    }
}