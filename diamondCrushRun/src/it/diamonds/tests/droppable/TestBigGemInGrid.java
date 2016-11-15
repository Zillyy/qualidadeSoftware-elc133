package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.interfaces.ExtensibleObject;
import it.diamonds.engine.Point;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.grid.Cell;
import it.diamonds.grid.GridController;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;
import java.util.ConcurrentModificationException;


public class TestBigGemInGrid extends AbstractGridTestCase
{
    public void testFind2x2Block()
    {
        assertEquals("Grid must not have any BigGem", 0,
            getNumberOfExtensibleObject());

        insert2x2BlockOfGems(EMERALD, 12, 3);
        grid.updateBigGems();

        assertEquals("Grid must have one BigGem", 1,
            getNumberOfExtensibleObject());
    }


    public void testRemoveBigGem()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();
        Droppable bigGem = grid.getDroppableAt(12, 3);
        grid.removeDroppableFromGrid(bigGem);

        assertEquals("Grid must not have any BigGem", 0,
            getNumberOfExtensibleObject());
    }


    public void testIsCellInABigGem()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        assertTrue("Cell 12, 3 must be in a BigGem",
            grid.isCellInAExtensibleObject(12, 3));
    }


    public void testCreateTwoBigGems()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);
        grid.updateBigGems();

        assertEquals("Grid must have one BigGem", 1,
            getNumberOfExtensibleObject());

        insert2x2BlockOfGems(EMERALD, 12, 6);
        grid.updateBigGems();

        assertEquals("Grid must have two BigGem", 2,
            getNumberOfExtensibleObject());
    }


    public void testSameBigGemMustNotBeCreatedTwice()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();
        grid.updateBigGems();

        assertEquals("Grid must have one BigGem", 1,
            getNumberOfExtensibleObject());
    }


    public void testGetBigGemAt()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        assertNotNull("Grid must return a BigGem object", bigGem);
        assertNull("There must not be any BigGem at 0, 0", grid.getDroppableAt(
            0, 0));
    }


    public void testBigGemDimensions()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);
        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        assertEquals(new Cell(3, 12, 4, 13), bigGem.getCell());
    }


    public void testBigGemContainsGem()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);
        Droppable gem = grid.getDroppableAt(12, 3);

        assertTrue("Emerald at 12,3 must be in the BigGem",
            bigGem.getCell().containsSingleCell(gem.getCell().getTopRow(),
                gem.getCell().getLeftColumn()));
    }


    public void testBigGemExtendsUp()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(EMERALD), 11, 4);

        grid.updateBigGems();

        assertTrue(
            "BigGem must be extended on the top side. Gem at 11,3 wasn't added",
            bigGem.getCell().containsSingleCell(11, 3));
        assertTrue(
            "BigGem must be extended on the top side. Gem at 11,4 wasn't added",
            bigGem.getCell().containsSingleCell(11, 4));
    }


    public void testBigGemExtendsDown()
    {
        insert2x2BlockOfGems(EMERALD, 11, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 13, 3);
        insertAndUpdate(createGem(EMERALD), 13, 4);

        grid.updateBigGems();

        assertTrue(
            "BigGem must be extended on the bottom side. Gem at 13,3 wasn't added",
            bigGem.getCell().containsSingleCell(13, 3));
        assertTrue(
            "BigGem must be extended on the bottom side. Gem at 13,4 wasn't added",
            bigGem.getCell().containsSingleCell(13, 4));
    }


    public void testBigGemExtendsRight()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 5);

        grid.updateBigGems();

        assertTrue(
            "BigGem must be extended on the right side. Gem at 13,5 wasn't added",
            bigGem.getCell().containsSingleCell(13, 5));
        assertTrue(
            "BigGem must be extended on the right side. Gem at 12,5 wasn't added",
            bigGem.getCell().containsSingleCell(12, 5));
    }


    public void testBigGemExtendsOnLeft()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(EMERALD), 12, 2);

        grid.updateBigGems();

        assertTrue(
            "BigGem must be extended on the left side. Gem at 13,2 wasn't added",
            bigGem.getCell().containsSingleCell(13, 3));
        assertTrue(
            "BigGem must be extended on the left side. Gem at 12,2 wasn't added",
            bigGem.getCell().containsSingleCell(12, 2));
    }


    public void testBigGemDoesntExtendsUp()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(DIAMOND), 11, 4);

        grid.updateBigGems();

        assertFalse(
            "BigGem must be not extended on the top side. Gem at 11,3 was added",
            bigGem.getCell().containsSingleCell(11, 3));

        assertFalse(
            "BigGem must be not extended on the top side. Gem at 11,4 was added",
            bigGem.getCell().containsSingleCell(11, 4));
    }


    public void testBigGemDoesntExtendsRight()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(DIAMOND), 12, 5);

        grid.updateBigGems();

        assertFalse(
            "BigGem must be not extended on the right side. Gem at 13,5 was added",
            bigGem.getCell().containsSingleCell(13, 5));
        assertFalse(
            "BigGem must be not extended on the right side. Gem at 12,5 was added",
            bigGem.getCell().containsSingleCell(12, 5));
    }


    public void testBigGemDoesntExtendsOnLeft()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);

        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(DIAMOND), 12, 2);

        grid.updateBigGems();

        assertFalse(
            "BigGem must be not extended on the left side. Gem at 13,2 was added",
            bigGem.getCell().containsSingleCell(13, 2));
        assertFalse(
            "BigGem must be not extended on the left side. Gem at 12,2 was added",
            bigGem.getCell().containsSingleCell(12, 2));
    }


    public void testCantExtendIfOnTheLeftBorder()
    {
        insert2x2BlockOfGems(EMERALD, 12, 0);
        grid.updateBigGems();

        assertEquals("BigGem cant extend on the left border", 1,
            getNumberOfExtensibleObject());
    }


    public void testCantExtendIfOnTheRightBorder()
    {
        insert2x2BlockOfGems(EMERALD, 12, 6);
        grid.updateBigGems();

        assertEquals("BigGem cant extend on the right border", 1,
            getNumberOfExtensibleObject());
    }


    public void testCantExtendIfOnTheTopBorder()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);
        insert2x2BlockOfGems(DIAMOND, 10, 4);
        insert2x2BlockOfGems(EMERALD, 8, 4);
        insert2x2BlockOfGems(DIAMOND, 6, 4);
        insert2x2BlockOfGems(EMERALD, 4, 4);
        insert2x2BlockOfGems(DIAMOND, 2, 4);
        insert2x2BlockOfGems(EMERALD, 0, 4);

        grid.updateBigGems();

        assertEquals("BigGem cant extend on the right border", 7,
            getNumberOfExtensibleObject());
    }


    public void test2x3BlockIsDetected()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);

        insertAndUpdate(createGem(EMERALD), 11, 4);
        insertAndUpdate(createGem(EMERALD), 11, 5);

        grid.updateBigGems();

        assertEquals("2x3 Gems block forms one BigGem", 1,
            getNumberOfExtensibleObject());
    }


    public void test2x4BlockIsDetected()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);

        insertAndUpdate(createGem(EMERALD), 11, 4);
        insertAndUpdate(createGem(EMERALD), 11, 5);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 4);

        insertAndUpdate(createGem(EMERALD), 10, 4);
        insertAndUpdate(createGem(EMERALD), 10, 5);

        grid.updateBigGems();

        assertEquals(new Cell(4, 10, 5, 13), bigGem.getCell());
    }


    public void testTwo2x2VerticalBlocksAreMerged()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);
        insert2x2BlockOfGems(EMERALD, 10, 4);

        grid.updateBigGems();

        assertEquals("Two 2x2 blocks were not merged", 1,
            getNumberOfExtensibleObject());
        assertEquals(new Cell(4, 10, 5, 13),
            grid.getDroppableAt(12, 4).getCell());
    }


    public void testTwo2x2HorizontalBlocksAreMerged()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);
        insert2x2BlockOfGems(EMERALD, 12, 2);

        grid.updateBigGems();

        assertEquals("Two 2x2 blocks were not merged", 1,
            getNumberOfExtensibleObject());
        assertEquals(new Cell(2, 12, 5, 13),
            grid.getDroppableAt(12, 4).getCell());
    }


    public void test3x3BlockIsDetected()
    {
        insert3x3BlockOfGems();

        grid.updateBigGems();

        assertEquals("3x3 Gems block forms one BigGem", 1,
            getNumberOfExtensibleObject());
        assertEquals(new Cell(4, 11, 6, 13),
            grid.getDroppableAt(12, 4).getCell());
    }


    public void test5x3BlockIsDetectedAfterInsertion()
    {
        DroppableColor gemColor = EMERALD;

        insert2x3BlockOfGems(gemColor, 11, 2);

        insert2x3BlockOfGems(gemColor, 11, 5);

        insertAndUpdate(createGem(gemColor), 13, 4);
        insertAndUpdate(createGem(gemColor), 12, 4);

        grid.updateBigGems();

        assertEquals("There must be two BigGem", 2,
            getNumberOfExtensibleObject());

        insertAndUpdate(createGem(gemColor), 11, 4);

        grid.updateBigGems();

        assertEquals("There must be one BigGem", 1,
            getNumberOfExtensibleObject());
        assertEquals(new Cell(2, 11, 6, 13),
            grid.getDroppableAt(11, 4).getCell());
    }


    public void testBigGemDoesNotContainGem()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        insertAndUpdate(createGem(EMERALD), 11, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 3);
        Droppable gem = grid.getDroppableAt(11, 4);

        assertFalse("Emerald at 11,4 must be not in the BigGem",
            bigGem.getCell().containsSingleCell(gem.getCell().getTopRow(),
                gem.getCell().getLeftColumn()));
    }


    private void insert3x3BlockOfGems()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(EMERALD), 12, 6);

        insertAndUpdate(createGem(EMERALD), 11, 4);
        insertAndUpdate(createGem(EMERALD), 11, 5);
        insertAndUpdate(createGem(EMERALD), 11, 6);
    }


    public void testGridIsUpdatedWithBigGems() throws IOException
    {
        int randomSequence[] = { 99, 0, 99, 0 };

        InputReactor inputReactor = new InputReactor(Input.create(
            environment.getKeyboard(), environment.getTimer()),
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        GridController gridController = GridController.create(environment,
            inputReactor, new MockRandomGenerator(randomSequence), new Point(0,
                0));

        grid = gridController.getGrid();

        gridController.update(environment.getTimer().getTime());

        assertEquals("Grid must not have any BigGem", 0,
            getNumberOfExtensibleObject());

        insert2x2BlockOfGems(EMERALD, 12, 2);

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate") + 1);

        while(droppedGemCanMoveDown(grid))
        {
            gridController.update(environment.getTimer().getTime());
        }

        gridController.update(environment.getTimer().getTime());

        assertEquals("Grid must have one BigGem", 1,
            getNumberOfExtensibleObject());
    }


    public void testBigGemNotStealingFromOthers()
    {
        insert2x2BlockOfGems(EMERALD, 12, 3);

        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createGem(EMERALD), 11, 2);
        insertAndUpdate(createGem(EMERALD), 11, 3);

        grid.updateBigGems();

        assertEquals("Grid must have one BigGem", 1,
            getNumberOfExtensibleObject());
    }


    public void testNoBigGemWithChests()
    {
        insertAndUpdate(createChest(EMERALD), 13, 5);
        insertAndUpdate(createChest(EMERALD), 13, 6);
        insertAndUpdate(createChest(EMERALD), 12, 5);
        insertAndUpdate(createChest(EMERALD), 12, 6);

        try
        {
            grid.updateBigGems();
        }
        catch(RuntimeException exc)
        {
            fail("Grid is trying to make a BigGem with Chests");
        }
    }


    public void testNotThrowingConcurrentModificationException()
    {
        try
        {
            insert2x2BlockOfGems(EMERALD, 12, 3);
            insert2x2BlockOfGems(EMERALD, 12, 5);
            insert2x2BlockOfGems(EMERALD, 10, 3);
            insert2x2BlockOfGems(EMERALD, 10, 5);
            grid.updateBigGems();
        }
        catch(ConcurrentModificationException exc)
        {
            fail("ConcurrentModificationException thrown");
        }
    }


    public void testBigGemCanMoveDown()
    {
        insert2x2BlockOfGems(EMERALD, 4, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(4, 4);

        assertTrue("BigGem can move down in 4,4",
            bigGem.getMovingDownObject().canMoveDown(grid));
    }


    public void testBigGemCantMoveDownOnBottom()
    {
        insert2x2BlockOfGems(EMERALD, 12, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(12, 4);

        assertFalse("BigGem must be not able to move down in 12,4",
            bigGem.getMovingDownObject().canMoveDown(grid));
    }


    public void testBigGemCantMoveDownWithObstacles()
    {
        insert2x2BlockOfGems(EMERALD, 11, 4);
        insertAndUpdate(createGem(EMERALD), 13, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(11, 4);

        assertFalse("BigGem must be not able to move down",
            bigGem.getMovingDownObject().canMoveDown(grid));
    }


    public void testBigGemMoveDownABit()
    {
        Droppable gem = createGem(EMERALD);
        grid.insertDroppable(gem, 12, 3);
        gem.getFallingObject().drop();
        grid.updateDroppable(gem);

        Droppable gem1 = createGem(EMERALD);
        grid.insertDroppable(gem1, 12, 4);
        gem1.getFallingObject().drop();
        grid.updateDroppable(gem1);

        Droppable gem2 = createGem(EMERALD);
        grid.insertDroppable(gem2, 11, 3);
        gem2.getFallingObject().drop();
        grid.updateDroppable(gem2);

        Droppable gem3 = createGem(EMERALD);
        grid.insertDroppable(gem3, 11, 4);
        gem3.getFallingObject().drop();
        grid.updateDroppable(gem3);

        float gemY = gem2.getSprite().getPosition().getY();

        grid.updateBigGems();
        Droppable bigGem = grid.getDroppableAt(12, 3);

        bigGem.getMovingDownObject().moveDown(grid);

        assertEquals("top must be increased", gemY + grid.getActualGravity(),
            bigGem.getSprite().getPosition().getY(), 0.001f);
    }


    public void testBigGemMoveDown()
    {
        insert2x2BlockOfGems(EMERALD, 11, 3);

        grid.updateBigGems();
        Droppable bigGem = grid.getDroppableAt(12, 3);

        bigGem.getMovingDownObject().moveDown(grid);
        bigGem.getMovingDownObject().moveDown(grid);
        bigGem.getMovingDownObject().moveDown(grid);
        bigGem.getMovingDownObject().moveDown(grid);

        assertNotSame("Big Gem must be here", null, grid.getDroppableAt(13, 3));
    }


    public void testMergeThreeBigGemUp()
    {
        insertBigGem(EMERALD, 11, 3, 12, 6);

        grid.updateBigGems();

        insertBigGem(EMERALD, 8, 5, 10, 6);

        grid.updateBigGems();

        insertBigGem(EMERALD, 8, 3, 10, 4);

        grid.updateBigGems();

        assertSame("there isn't only one bigGem", grid.getDroppableAt(11, 3),
            grid.getDroppableAt(8, 3));
        assertSame("there isn't only one bigGem", grid.getDroppableAt(11, 3),
            grid.getDroppableAt(8, 5));
    }


    public void testMergeThreeBigGemRight()
    {
        insertBigGem(EMERALD, 8, 3, 12, 4);

        grid.updateBigGems();

        insertBigGem(EMERALD, 10, 5, 12, 6);

        grid.updateBigGems();

        insertBigGem(EMERALD, 8, 5, 9, 6);

        grid.updateBigGems();

        assertSame("there isn't only one bigGem", grid.getDroppableAt(8, 3),
            grid.getDroppableAt(10, 5));
        assertSame("there isn't only one bigGem", grid.getDroppableAt(8, 3),
            grid.getDroppableAt(8, 5));
    }


    public void testGetIncludedGemsReset()
    {
        insertBigGem(EMERALD, 12, 3, 13, 4);
        grid.updateBigGems();

        ExtensibleObject extensibleObject = grid.getDroppableAt(13, 3).getExtensibleObject();

        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(EMERALD), 11, 4);

        grid.updateBigGems();

        assertEquals(2, extensibleObject.getIncludedGems().size());

    }


    public void testRemoveGemsAfterBigGemCreation()
    {
        insertBigGem(EMERALD, 12, 3, 13, 4);

        assertEquals(4, grid.getNumberOfDroppables());

        grid.updateBigGems();

        assertEquals(1, grid.getNumberOfDroppables());
    }


    public void testRemoveGemsAfterBigGemRowExtension()
    {
        insertBigGem(EMERALD, 12, 3, 13, 4);
        grid.updateBigGems();

        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(EMERALD), 11, 4);

        assertEquals(3, grid.getNumberOfDroppables());

        grid.updateBigGems();

        assertEquals(1, grid.getNumberOfDroppables());
    }


    public void testRemoveGemsAfterBigGemColumnExtension()
    {
        insertBigGem(EMERALD, 12, 3, 13, 4);
        grid.updateBigGems();

        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(EMERALD), 12, 2);

        assertEquals(3, grid.getNumberOfDroppables());

        grid.updateBigGems();

        assertEquals(1, grid.getNumberOfDroppables());
    }


    public void testSixPerSixBug()
    {
        makeAllGemsFall();
        insert2x2BlockOfGems(EMERALD, 12, 0);
        grid.updateBigGems();
        insertAndUpdate(createGem(EMERALD), 11, 0);

        Droppable emerald = createGem(EMERALD);
        Droppable slave = createGem(DIAMOND);
        controller.getGemsPair().setPivot(emerald);
        controller.getGemsPair().setSlave(slave);

        emerald.getFallingObject().drop();

        grid.insertDroppable(emerald, 11, 1);
        grid.insertDroppable(slave, 13, 2);

        assertNotNull(grid.getDroppableAt(12, 0));
        grid.updateBigGems();
        assertNotNull(grid.getDroppableAt(12, 0));

    }
}
