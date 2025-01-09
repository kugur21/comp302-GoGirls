package org.RokueLike;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.RokueLike.domain.manager.MonsterManager;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.entity.monster.Monster;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/// Tests for spawnMonster method in MonsterManager
public class MonsterSpawnTest {
    private MonsterManager monsterManager;

    @Mock
    private HallGrid mockHallGrid;
    @Mock
    private Hero mockHero;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        monsterManager = new MonsterManager(new ArrayList<>(), mockHallGrid, mockHero, true);
    }

    @Test
    public void testSpawnMonsterWhenSpawningDisabled() {
        monsterManager = new MonsterManager(new ArrayList<>(), mockHallGrid, mockHero, false);

        monsterManager.spawnMonster();

        verify(mockHallGrid, never()).addMonster(any(Monster.class));
    }
}
