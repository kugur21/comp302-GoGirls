package org.RokueLike.domain.entity.hero;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<String> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory sequentially.
     * @param itemName The name of the item to add.
     */
    public void addItem(String itemName) {
        // Implementation will go here.
    }

    /**
     * Removes the item at the specified index from the inventory.
     * @param index The index of the item to remove.
     * @return The removed item name.
     */
    public String removeItem(int index) {
        // Implementation will go here.
        return null;
    }

    /**
     * Gets the item at the specified index without removing it.
     * @param index The index of the item.
     * @return The item name.
     */
    public String getItem(int index) {
        // Implementation will go here.
        return null;
    }

    /**
     * Gets all items in the inventory.
     * @return A list of all items.
     */
    public List<String> getAllItems() {
        // Implementation will go here.
        return null;
    }

    /**
     * Clears all items from the inventory.
     */
    public void clearInventory() {
        // Implementation will go here.
    }

    /**
     * Gets the total number of items in the inventory.
     * @return The number of items.
     */
    public int getItemCount() {
        // Implementation will go here.
        return 0;
    }
}