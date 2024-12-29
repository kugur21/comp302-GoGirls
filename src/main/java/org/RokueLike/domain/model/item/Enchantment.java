
package org.RokueLike.domain.model.item;

import org.RokueLike.domain.hall.GridCell;

public class Enchantment extends GridCell {

    private final EnchantmentType type; // Type of the enchantment

    public Enchantment(EnchantmentType type, int x, int y) {
        super(type.getName(), x, y);
        this.type = type;
    }

    public EnchantmentType getEnchantmentType() {
        return type;
    }

    public enum EnchantmentType {
        EXTRA_TIME("extra_time"), // Adds extra time for the hero
        EXTRA_LIFE("extra_life"), // Adds extra life for the hero
        REVEAL("reveal"), // Reveals hidden rune region
        CLOAK_OF_PROTECTION("cloak_of_protection"), // Provides protection from archer attacks
        LURING_GEM("luring_gem"); // Lures the fighter monsters in a specified direction

        private final String name;

        EnchantmentType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

}