package org.RokueLike.domain.entity.item;

public abstract class Item {

    private String name;
    private ItemType type;

    public Item(String name, ItemType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public enum ItemType {
        ENCHANTMENT,
        RUNE,
        OBJECT,
        DOOR;
    }
}
