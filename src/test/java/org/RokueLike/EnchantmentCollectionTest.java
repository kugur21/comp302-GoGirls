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

}
