package org.RokueLike.domain.entity.item;

public class Enchantment extends Item {

    private final EnchantmentType type;
    private int duration;
    private boolean isActive;

    public Enchantment(EnchantmentType type, int x, int y) {
        super(type.getName() + "_enchantment", x, y, ItemType.ENCHANTMENT);
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
        EXTRA_TIME("ExtraTime", 0),
        EXTRA_LIFE("ExtraLife", 0),
        REVEAL("Reveal", 10),
        CLOAK_OF_PROTECTION("CloakOfProtection", 20),
        LURING_GEM("LuringGem", 0);

        private final String name;
        private final int duration;

        EnchantmentType(String name, int duration) {
            this.name = name;
            this.duration = duration;
        }

        public String getName() {
            return name;
        }

        public int getDuration() {
            return duration;
        }
    }
}
