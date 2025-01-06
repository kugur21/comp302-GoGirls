package org.RokueLike.domain.model.entity.monster.behaviour.wizard;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.entity.monster.behaviour.IMonsterBehaviour;

import java.io.Serial;
import java.io.Serializable;
public class Closer implements IMonsterBehaviour, Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    public Closer() {
    }

    @Override
    public void behaviour(Monster monster) {
        GameManager.setWizardClosureActive(true);
    }
}