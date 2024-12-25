package org.RokueLike.domain.entity.hero;

import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final List<EnchantmentType> items;
    private EnchantmentType activeEnchantment = null;

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

    //Fixed a bug that occurs during using an enchantment.
    public void removeItem(EnchantmentType enchantment) {
        if (items.contains(enchantment)) {
            items.remove(enchantment);
            setActiveEnchantment(enchantment);
            System.out.println("Removed one " + enchantment.getName() + " from inventory.");
        } else {
            System.out.println("No " + enchantment.getName() + " found in inventory.");
        }
    }

    public List<EnchantmentType> getItems() {
        return items;
    }

    public EnchantmentType getActiveEnchantment() {
        return activeEnchantment;
    }

    public void setActiveEnchantment(EnchantmentType activeEnchantment) {
        this.activeEnchantment = activeEnchantment;
    }

}