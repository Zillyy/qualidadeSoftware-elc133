package it.diamonds.tests.droppable;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.droppable.gems.BigGem;
import it.diamonds.droppable.gems.Chest;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.Point;
import it.diamonds.grid.Cell;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestBigGem extends TestCase
{
    private AbstractEngine engine;

    private Droppable diamond;

    private BigGem bigGem;


    public void setUp()
    {
        engine = new MockEngine(800, 600);
        diamond = createDiamond();
        diamond.getCell().setColumn(1);
        diamond.getCell().setRow(1);

        bigGem = new BigGem(1, 1, diamond.getGridObject().getColor(), engine,
            new Point(0, 0));
        bigGem.addGem(diamond);
    }


    private Droppable createDiamond()
    {
        return Gem.createForTesting(engine);
    }


    public void testHasCorrectSprite()
    {
        String texturePath = "gfx/droppables/tiles/";

        String loadedTexture = bigGem.getSprite().getTexture().getName();
        assertEquals(texturePath.concat(bigGem.getColor().getName()),
            loadedTexture);
    }


    public void testSpriteSize()
    {
        assertEquals(Cell.SIZE, bigGem.getSprite().getTextureArea().getHeight());
        assertEquals(Cell.SIZE, bigGem.getSprite().getTextureArea().getWidth());
    }


    public void testGemType()
    {
        assertSame(diamond.getGridObject().getColor(), bigGem.getColor());
    }


    public void testDifferentTexturesLoaded()
    {
        BigGem diamondBigGem = new BigGem(0, 0, DroppableColor.DIAMOND, engine,
            new Point(0, 0));
        BigGem emeraldBigGem = new BigGem(0, 0, DroppableColor.EMERALD, engine,
            new Point(0, 0));

        assertEquals("gemGroup Texture is not correct",
            "gfx/droppables/tiles/diamond",
            diamondBigGem.getSprite().getTexture().getName());
        assertEquals("gemGroup Texture is not correct",
            "gfx/droppables/tiles/emerald",
            emeraldBigGem.getSprite().getTexture().getName());
    }


    public void testMovingDownObjectInterfaceReturnsWell()
    {
        assertEquals(bigGem, bigGem.getMovingDownObject());
    }


    public void testGridObjectInterfaceReturnsWell()
    {
        assertEquals(bigGem, bigGem.getGridObject());
    }


    public void testExtensibleObjectInterfaceReturnsWell()
    {
        assertEquals(bigGem, bigGem.getExtensibleObject());
    }


    public void testMergingObjectInterfaceReturnsWell()
    {
        assertEquals(bigGem, bigGem.getMergingObject());
    }


    public void testMoveableObjectInterfaceReturnsWell()
    {
        assertNull(bigGem.getMoveableObject());
    }


    public void testAnimatedObjectInterfaceReturnsWell()
    {
        assertNull(bigGem.getAnimatedObject());
    }


    public void testObjectWithCollisionSoundInterfaceReturnsWell()
    {
        assertNull(bigGem.getObjectWithCollisionSound());
    }


    public void testFallingObjectInterfaceReturnsWell()
    {
        assertNull(bigGem.getFallingObject());
    }


    public void testCorrectType()
    {
        assertEquals(bigGem.getType(), DroppableType.GEM);
    }


    public void testSameBigGems()
    {
        BigGem diamondBigGem = new BigGem(0, 0, DroppableColor.DIAMOND, engine,
            new Point(0, 0));
        assertTrue(bigGem.isSameOf(diamondBigGem));
    }


    public void testNotSameBigGems()
    {
        BigGem newBigGem = new BigGem(0, 0, DroppableColor.RUBY, engine,
            new Point(0, 0));
        assertFalse(bigGem.isSameOf(newBigGem));
    }


    public void testNotSameWithChestOfSameColor()
    {
        Chest chest = Chest.create(engine, bigGem.getColor(), 3500);
        assertFalse(bigGem.isSameOf(chest));
    }


    public void testGetIncludedGems()
    {
        BigGem newBigGem = new BigGem(0, 0, DroppableColor.DIAMOND, engine,
            new Point(0, 0));

        Droppable gems[] = new Droppable[4];

        for(int i = 0; i < 4; i++)
        {
            gems[i] = createDiamond();
            newBigGem.addGem(gems[i]);
        }

        assertEquals(4, newBigGem.getIncludedGems().size());

        for(int i = 0; i < 4; i++)
        {
            assertTrue(newBigGem.getIncludedGems().contains(gems[i]));
        }
    }
}
