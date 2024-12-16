package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final List<EnchantmentType> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean hasEnchantment(EnchantmentType enchantment) {
        return items.contains(enchantment);
    }

    public void addItem(EnchantmentType enchantment) {
        items.add(enchantment);
        System.out.println("Added " + enchantment.getName() + " to inventory.");
    }

    public void removeItem(EnchantmentType enchantment) {
        if (items.remove(enchantment)) {
            System.out.println("Removed " + enchantment.getName() + " from inventory.");
        } else {
            System.out.println(enchantment.getName() + " not found in inventory.");
        }
    }

    public List<EnchantmentType> getItems() {
        return items;
    }

}