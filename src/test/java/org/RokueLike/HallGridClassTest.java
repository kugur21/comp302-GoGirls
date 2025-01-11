package org.RokueLike;

import static org.junit.jupiter.api.Assertions.*;

import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.item.Door;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class HallGridClassTest {

    private HallGrid hallGrid;

    @BeforeEach
    public void setUp() {
        String[][] gridData = {
                {"#", ".", "h"},
                {"o1", ".", "d"},
                {".", "#", "."}
        };
        hallGrid = new HallGrid(gridData, "Test Hall");
    }

    @Test
    public void testInitialization() {
        assertNotNull(hallGrid);
        assertTrue(hallGrid.repOk());
    }

    @Test
    public void testFindRuneRegion() {
        hallGrid.initRune();
        int[][] region = hallGrid.findRuneRegion(3);
        assertNotNull(region);
        assertTrue(region.length > 0);
    }

    @Test
    public void testOpenDoor() {
        hallGrid.openDoor();
        boolean doorOpened = false;
        for (int y = 0; y < hallGrid.getHeight(); y++) {
            for (int x = 0; x < hallGrid.getWidth(); x++) {
                GridCell cell = hallGrid.getCell(x, y);
                if (cell instanceof Door && ((Door) cell).isOpen()) {
                    doorOpened = true;
                }
            }
        }
        assertTrue(doorOpened);
    }

    @Test
    public void testAddMonster() {
        Monster monster = new Monster(Monster.MonsterType.FIGHTER, 2, 2);
        hallGrid.addMonster(monster);
        assertTrue(hallGrid.getMonsters().contains(monster));
        assertTrue(hallGrid.repOk());
    }

}