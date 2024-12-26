package org.RokueLike.domain.entity.item;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

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
                hero.decrementLives();
                if (!hero.isAlive()) {
                    String message;
                    if (hero.getLives() > 0) {
                        message = "You ran out of time. Game Over!";
                    } else {
                        message = "You ran out of lives. Game Over!";
                    }
                    GameManager.reset();
                    Window.addScreen(new GameOverScreen(message), "GameOverScreen", true);
                }
                arrow.deactivate(); // Oku devre dışı bırak
                GameManager.handleHeroSpawn();
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
