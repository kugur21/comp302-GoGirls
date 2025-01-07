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

// MonsterManager and HallGrid modified
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

    @Test
    public void testSpawnMonsterNoSafeLocation() {
        when(mockHallGrid.findRandomSafeCell()).thenReturn(null);

        monsterManager.spawnMonster();

        verify(mockHallGrid, never()).addMonster(any(Monster.class));
    }

    @Test
    public void testSuccessfulMonsterSpawn() {
        when(mockHallGrid.findRandomSafeCell()).thenReturn(new int[]{2, 3});

        monsterManager.spawnMonster();

        verify(mockHallGrid).addMonster(any(Monster.class));
    }

    @Test
    public void testWizardMonsterBehavior() {
        monsterManager = spy(new MonsterManager(new ArrayList<>(), mockHallGrid, mockHero, true));

        when(mockHallGrid.findRandomSafeCell()).thenReturn(new int[]{4, 5});

        Monster wizard = new Monster(Monster.MonsterType.WIZARD, 4, 5);
        doReturn(wizard).when(monsterManager).generateRandomMonster(4, 5);

        monsterManager.spawnMonster();

        assertNotNull(wizard.getBehaviour());
    }

}