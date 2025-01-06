package org.RokueLike.domain.model.entity.hero;

import org.RokueLike.domain.model.item.Enchantment.EnchantmentType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final List<EnchantmentType> enchantments; // List of enchantments in the inventory

    public Inventory() {
        this.enchantments = new ArrayList<>();
    }

    // Checks if a specific enchantment is in the inventory.
    public boolean hasEnchantment(EnchantmentType enchantment) {
        return enchantments.contains(enchantment);
    }

    // Adds an enchantment to the inventory.
    public void addItem(EnchantmentType enchantment) {
        enchantments.add(enchantment);
    }

    // Removes an enchantment from the inventory.
    public void removeItem(EnchantmentType enchantment) {
        enchantments.remove(enchantment);
    }

    public List<EnchantmentType> getEnchantments() {
        return enchantments;
    }
}