package org.RokueLike.domain.entity.item;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.hall.HallGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrowManager {
    private final List<Arrow> arrows = new ArrayList<>();
    private final HallGrid hallGrid;

    public ArrowManager(HallGrid hallGrid) {
        this.hallGrid = hallGrid;
    }

    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
    }
    public void updateArrows(Hero hero) {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move(); // Oku hareket ettir

            // Çarpışma kontrolü
            if (arrow.getX() == hero.getPositionX() && arrow.getY() == hero.getPositionY()) {
                System.out.println("[Arrow]: Hero hit by arrow at X=" + arrow.getX() + " Y=" + arrow.getY());
                hero.decreaseLife(); // Kahramanın canını azalt
                arrow.deactivate(); // Oku devre dışı bırak
            }

            // Oku listeden çıkar
            if (!arrow.isActive()) {
                iterator.remove();
            }
        }
    }




    public List<Arrow> getArrows() {
        return arrows;
    }
}
