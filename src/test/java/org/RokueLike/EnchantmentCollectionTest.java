package org.RokueLike;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.manager.ItemManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Enchantment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.RokueLike.utils.Constants.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


/// Tests for collectEnchantment method in ItemManager
public class EnchantmentCollectionTest {
    // SARP - collectEnchantment in ItemManager
    private ItemManager itemManager;

    @Mock
    private HallGrid mockHallGrid;
    @Mock
    private Hero mockHero;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemManager = new ItemManager(mockHallGrid, mockHero, null, true);
    }

    @Test
    public void testCollectExtraTimeEnchantment() {
        Enchantment extraTime = mock(Enchantment.class);
        when(extraTime.getEnchantmentType()).thenReturn(Enchantment.EnchantmentType.EXTRA_TIME);
        when(mockHallGrid.getCurrentEnchantment()).thenReturn(extraTime);

        String result = itemManager.collectEnchantment();

        verify(mockHero).addRemainingTime(EXTRA_TIME);
        verify(mockHallGrid).removeEnchantment();
        assertEquals("Collected Extra Time! Gained 5 extra seconds.", result);
    }

    @Test
    public void testCollectExtraLifeEnchantment() {
        Enchantment extraLife = mock(Enchantment.class);
        when(extraLife.getEnchantmentType()).thenReturn(Enchantment.EnchantmentType.EXTRA_LIFE);
        when(mockHallGrid.getCurrentEnchantment()).thenReturn(extraLife);

        String result = itemManager.collectEnchantment();

        verify(mockHero).incrementLives();
        verify(mockHallGrid).removeEnchantment();
        assertEquals("Collected Extra Life! Gained an extra life.", result);
    }

    @Test
    public void testCollectCloakOfProtectionEnchantment() {
        Enchantment cloak = mock(Enchantment.class);
        when(cloak.getEnchantmentType()).thenReturn(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);
        when(mockHallGrid.getCurrentEnchantment()).thenReturn(cloak);

        String result = itemManager.collectEnchantment();

        verify(mockHero).addToInventory(Enchantment.EnchantmentType.CLOAK_OF_PROTECTION);
        verify(mockHallGrid).removeEnchantment();
        assertEquals("Collected Cloak of Protection!", result);
    }

    @Test
    public void testCollectRevealEnchantment() {
        Enchantment reveal = mock(Enchantment.class);
        when(reveal.getEnchantmentType()).thenReturn(Enchantment.EnchantmentType.REVEAL);
        when(mockHallGrid.getCurrentEnchantment()).thenReturn(reveal);

        String result = itemManager.collectEnchantment();

        verify(mockHero).addToInventory(Enchantment.EnchantmentType.REVEAL);
        verify(mockHallGrid).removeEnchantment();
        assertEquals("Collected Reveal Enchantment!", result);
    }


}
