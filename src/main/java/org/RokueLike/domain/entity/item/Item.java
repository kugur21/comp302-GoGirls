package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.hall.GridCell;

public abstract class Item extends GridCell {

    private ItemType type;

    public Item(String name, int x, int y, ItemType type) {
        super(name, x, y);
        this.type = type;
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
