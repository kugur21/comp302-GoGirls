package org.RokueLike;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.manager.ItemManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Enchantment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// MUHAMMET - applyReveal in ItemManager
/// Tests for applyReveal method in ItemManager
public class RevealEnchantmentTest {

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
    public void testApplyRevealWithRuneFound() {
        // Setup: Hero has the Reveal enchantment and the rune is already found
        when(mockHero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)).thenReturn(true);
        doNothing().when(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        when(mockHallGrid.findRuneRegion(4)).thenReturn(null);

        // Act
        String result = itemManager.applyReveal();

        // Assert
        verify(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        assertEquals("Rune already found. You can move to the next hall.", result);
    }
}
