package org.RokueLike.domain.entity.item;

import org.RokueLike.utils.Direction;

public class Arrow {
    private int x, y;
    private final Direction direction;
    private boolean active;
    private final int range; // Okun maksimum menzili
    private int distanceTraveled; // Okun şimdiye kadar kat ettiği mesafe

    public Arrow(int startX, int startY, Direction direction, int range) {
        this.x = startX;
        this.y = startY;
        this.direction = direction;
        this.active = true;
        this.range = range; // Maksimum menzil atanıyor
        this.distanceTraveled = 0; // Başlangıçta 0 mesafe
    }

    public void move() {
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
        distanceTraveled++; // Her hareket edişte mesafe artar
        if (distanceTraveled >= range) { // Menzile ulaşıldığında
            deactivate(); // Oku devre dışı bırak
        }
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public Direction getDirection() {
        return direction;
    }

}
