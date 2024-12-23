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
        items.remove(enchantment);
    }

    public List<EnchantmentType> getItems() {
        return items;
    }

}