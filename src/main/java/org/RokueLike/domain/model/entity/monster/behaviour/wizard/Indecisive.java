package org.RokueLike.domain.model.entity.monster.behaviour.wizard;

import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.entity.monster.behaviour.IMonsterBehaviour;

import java.io.Serial;
import java.io.Serializable;
import static org.RokueLike.utils.Constants.*;

public class Indecisive implements IMonsterBehaviour, Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private double timeElapsed = 0;

    public Indecisive() {
    }

    @Override
    public void behaviour(Monster monster) {
        timeElapsed += GAME_DELAY;
        if (timeElapsed >= WIZARD_DISAPPEAR) { // 2 seconds have passed
            monster.markForRemoval(); // Mark the monster for removal
        }
    }
}