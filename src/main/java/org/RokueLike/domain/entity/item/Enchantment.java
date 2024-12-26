
package org.RokueLike.domain.entity.item;

public class Enchantment extends Item {

    private final EnchantmentType type;
    private int duration;
    private boolean isActive;

    public Enchantment(EnchantmentType type, int x, int y) {
        super(type.getName(), x, y, ItemType.ENCHANTMENT);
        this.type = type;
        this.duration = type.getDuration();
        this.isActive = false;
    }

    public EnchantmentType getEnchantmentType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public enum EnchantmentType {
        EXTRA_TIME("extra_time", 0),
        EXTRA_LIFE("extra_life", 0),
        REVEAL("reveal", 10),
        CLOAK_OF_PROTECTION("cloak_of_protection", 20),
        LURING_GEM("luring_gem", 0);

        private final String name;
        private final int duration;

        EnchantmentType(String name, int duration) {
            this.name = name;
            this.duration = duration;
        }

        // Revised for PlayModeRenderer by Sarp
        public String getName() {
            return this.name;
        }

        public int getDuration() {
            return duration;
        }
    }
}
