package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableType.CHEST;
import static it.diamonds.droppable.DroppableType.FLASHING_GEM;
import static it.diamonds.droppable.DroppableType.GEM;
import static it.diamonds.droppable.DroppableType.STONE;
import junit.framework.TestCase;


public class TestGemType extends TestCase
{
    public void testGemType()
    {
        assertFalse(GEM.isChest());
        assertTrue(GEM.isGem());
        assertFalse(GEM.isFlashingGem());
        assertFalse(GEM.isStone());
    }


    public void testChestsType()
    {
        assertTrue(CHEST.isChest());
        assertFalse(CHEST.isGem());
        assertFalse(CHEST.isFlashingGem());
        assertFalse(CHEST.isStone());
    }


    public void testStoneType()
    {
        assertTrue(STONE.isStone());
        assertFalse(STONE.isGem());
        assertFalse(STONE.isChest());
        assertFalse(STONE.isFlashingGem());
    }


    public void testFlashType()
    {
        assertTrue(FLASHING_GEM.isFlashingGem());
        assertFalse(FLASHING_GEM.isChest());
        assertFalse(FLASHING_GEM.isGem());
        assertFalse(FLASHING_GEM.isStone());
    }

    /*
     * public void testGemAndChestTypeEquals() { assertSame("The type must be the same
     * type (DIAMOND)", DIAMOND.getBaseType(), DIAMOND_CHEST.getBaseType());
     * assertSame("The type must be the same type (EMERALD)", EMERALD.getBaseType(),
     * EMERALD_CHEST.getBaseType()); assertSame("The type must be the same type (RUBY)",
     * RUBY.getBaseType(), RUBY_CHEST.getBaseType()); assertSame("The type must be the
     * same type (SAPPHIRE)", SAPPHIRE.getBaseType(), SAPPHIRE_CHEST.getBaseType());
     * assertSame("The type must be the same type (TOPAZ)", TOPAZ.getBaseType(),
     * TOPAZ_CHEST.getBaseType()); } public void testGemAndChestTypeNotEquals() {
     * assertNotSame("The type must be different", DIAMOND.getBaseType(),
     * EMERALD_CHEST.getBaseType()); } public void testFlashTypeEquals() {
     * assertNotNull("The type must be different", FLASHING_GEM.getBaseType());
     * assertSame("The type must be different", FLASHING_GEM.getBaseType(),
     * FLASHING_GEM.getBaseType()); }
     */
}
