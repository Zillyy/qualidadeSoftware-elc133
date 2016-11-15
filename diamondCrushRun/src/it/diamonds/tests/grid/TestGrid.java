package it.diamonds.tests.grid;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.pair.Direction.GO_DOWN;
import static it.diamonds.droppable.pair.Direction.GO_LEFT;
import static it.diamonds.droppable.pair.Direction.GO_RIGHT;
import static it.diamonds.droppable.pair.Direction.GO_UP;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.Rectangle;
import it.diamonds.grid.Cell;
import it.diamonds.tests.mocks.MockAction;
import it.diamonds.tests.mocks.MockEngine;

import java.io.IOException;


public class TestGrid extends AbstractGridTestCase
{
    private static final int GRID_LEFT = 40;

    private static final int GRID_TOP = 40;

    private static final int GRID_RIGHT = 295;

    private static final int GRID_BOTTOM = 487;

    private Rectangle bounds;

    private Droppable gem1;

    private Droppable gem2;

    private int gridColumns;

    private int gridRows;

    private float xStep;

    private float yStep;


    public void setUp() throws IOException
    {
        super.setUp();
        gridColumns = 8;
        gridRows = 14;

        bounds = new Rectangle(GRID_LEFT, GRID_TOP, GRID_RIGHT, GRID_BOTTOM);

        xStep = (float)bounds.getWidth() / gridColumns;
        yStep = (float)bounds.getHeight() / gridRows;

        gem1 = Gem.createForTesting(environment.getEngine());
        gem2 = Gem.createForTesting(environment.getEngine());
    }


    public void testInsertion()
    {
        assertFalse("there's a Gem at (0,0) before insertion",
            grid.isDroppableAt(1, 1));
        grid.insertDroppable(gem1, 1, 1);
        assertTrue("insertion failed", grid.isDroppableAt(1, 1));
    }


    public void testTwoInsertions()
    {
        grid.insertDroppable(gem1, 1, 2);
        assertFalse("there's a Gem at (0,1) before insertion",
            grid.isDroppableAt(3, 0));
        grid.insertDroppable(gem2, 3, 0);
        assertTrue("insertion failed at (0,1) ", grid.isDroppableAt(3, 0));
    }


    public void testRedundantInsertion()
    {
        try
        {
            grid.insertDroppable(gem1, 2, 0);
            grid.insertDroppable(gem2, 2, 0);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }

        fail("Double insertion at same position should not be allowed");
    }


    public void testRetrieval()
    {
        grid.insertDroppable(gem1, 1, 0);
        assertSame("Gem retrieval failed", gem1, grid.getDroppableAt(1, 0));
    }


    public void testDrawBackground()
    {
        grid.draw(environment.getEngine());
        assertEquals(1,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testDrawTwoGems()
    {
        grid.insertDroppable(gem1, 3, 1);
        grid.insertDroppable(gem2, 2, 1);

        grid.draw(environment.getEngine());

        assertEquals(3,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testPositions()
    {
        grid.insertDroppable(gem1, 4, 2);
        assertEquals(bounds.left() + xStep * 2,
            gem1.getSprite().getPosition().getX());
        assertEquals(bounds.top() + yStep * 4,
            gem1.getSprite().getPosition().getY());

        Droppable otherGem = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(otherGem, 3, 5);
        assertEquals(bounds.left() + xStep * 5,
            otherGem.getSprite().getPosition().getX());
        assertEquals(bounds.top() + yStep * 3,
            otherGem.getSprite().getPosition().getY());
    }


    public void testUnderflow()
    {
        try
        {
            grid.insertDroppable(gem1, -1, -1);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }

        fail("wrong parameters not rejected");
    }


    public void testOverflow()
    {
        try
        {
            grid.insertDroppable(gem1, 100, 100); // overflow
        }
        catch(IllegalArgumentException e)
        {
            return;
        }

        fail("wrong parameters not rejected");
    }


    public void testFindGemCell()
    {
        grid.insertDroppable(gem1, 1, 2);

        assertEquals(1, gem1.getCell().getTopRow());
        assertEquals(2, gem1.getCell().getLeftColumn());
    }


    public void testNullInsertion()
    {
        try
        {
            grid.insertDroppable(null, 6, 5);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("null gem inserted");
    }


    public void testSameInsertion()
    {
        grid.insertDroppable(gem1, 4, 3);
        try
        {
            grid.insertDroppable(gem1, 4, 4);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("same gem inserted at different positions");
    }


    public void testBottomBound()
    {
        grid.insertDroppable(gem1, gridRows - 1, 1);
        grid.draw(environment.getEngine());

        assertFalse(
            "Gem moved beyond bottom bound!",
            gem1.getSprite().getPosition().getY()
                + gem1.getSprite().getTextureArea().getHeight() - bounds.top() > bounds.getHeight());
    }


    public void testGemIsMovingWhenLowerCellIsEmpty()
    {
        grid.insertDroppable(gem1, 4, 3);

        grid.setGravity(1.0f);

        float oldPosition = gem1.getSprite().getPosition().getY();

        grid.updateDroppable(gem1);

        assertEquals(
            "Gem is not moving when cell under current position is empty",
            oldPosition + 1.0f, gem1.getSprite().getPosition().getY(), 0.0001f);
    }


    public void testGemIsStoppedWhenLowerCellIsFull()
    {
        grid.insertDroppable(gem1, 5, 2);
        grid.insertDroppable(gem2, 6, 2);

        grid.setGravity(1.0f);

        float oldPosition = gem1.getSprite().getPosition().getY();

        grid.updateDroppable(gem1);

        assertEquals(
            "Gem is not moving when cell under current position is empty",
            oldPosition, gem1.getSprite().getPosition().getY(), 0.0001f);
    }


    public void testNotMovingWithZeroGravity()
    {
        grid.insertDroppable(gem1, 4, 5);
        grid.setGravity(0.0f);

        grid.updateDroppable(gem1);

        assertEquals(4, gem1.getCell().getTopRow());
        assertEquals(5, gem1.getCell().getLeftColumn());
    }


    public void testGemPositionAfterCollision()
    {
        grid.insertDroppable(gem2, 6, 2);
        grid.insertDroppable(gem1, 4, 2);

        grid.setGravity(yStep - 1);

        grid.updateDroppable(gem1);
        grid.updateDroppable(gem1);

        assertEquals("Gem is not in the correct position after update",
            gem2.getSprite().getPosition().getY() - yStep,
            gem1.getSprite().getPosition().getY());
    }


    public void testGemCanMoveLeft()
    {
        grid.insertDroppable(gem1, 3, 3);
        assertTrue("gem1 can move left", grid.droppableCanMove(gem1, GO_LEFT));

        grid.insertDroppable(gem2, 3, 2);
        assertFalse("gem1 cannot move left", grid.droppableCanMove(gem1,
            GO_LEFT));
    }


    public void testGemCanMoveRight()
    {
        grid.insertDroppable(gem1, 3, 2);
        assertTrue("gem1 can move right", grid.droppableCanMove(gem1, GO_RIGHT));

        grid.insertDroppable(gem2, 3, 3);
        assertFalse("gem1 cannot move right", grid.droppableCanMove(gem1,
            GO_RIGHT));
    }


    public void testIsValidCell()
    {
        assertTrue("0,0 must be valid cell", grid.isValidCell(0, 0));
        assertTrue("0,7 must be valid cell", grid.isValidCell(0, 7));
        assertTrue("13,0 must be valid cell", grid.isValidCell(13, 0));

        assertFalse("-1,0 must not be valid cell", grid.isValidCell(-1, 0));
        assertFalse("0,-1 must not be valid cell", grid.isValidCell(0, -1));
        assertFalse("0,8 must not be valid cell", grid.isValidCell(0, 8));
        assertFalse("14,0 must not be valid cell", grid.isValidCell(14, 0));
    }


    public void testIsColumnFull()
    {
        grid.insertDroppable(gem1, 0, 4);
        gem1.getFallingObject().drop();

        assertTrue("column 4 is not full", grid.isColumnFull(4));
    }


    public void testIsColumnNotFull()
    {
        grid.insertDroppable(gem1, 0, 4);

        assertFalse("column 4 is full", grid.isColumnFull(4));

        grid.setGravity(Cell.SIZE);
        grid.updateDroppable(gem1);

        assertFalse("column 4 is full", grid.isColumnFull(4));
    }


    public void testMoveWithNullGem()
    {
        try
        {
            grid.moveDroppable(null, GO_DOWN);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("exception not raised");
    }


    public void testMoveDown()
    {
        grid.insertDroppable(gem1, 2, 4);

        grid.moveDroppable(gem1, GO_DOWN);
        assertTrue("Gem didn't move down", grid.isDroppableAt(3, 4));
    }


    public void testAddGemToANotEmptyCell()
    {
        try
        {
            grid.insertDroppable(gem1, 5, 5);
            grid.insertDroppable(gem2, 5, 5);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("exception not reised");
    }


    public void testMoveDownWithCollision()
    {
        grid.insertDroppable(gem1, gridRows - 1, 4);

        grid.moveDroppable(gem1, GO_DOWN);
        assertTrue("Gem must remain to the bottom", grid.isDroppableAt(
            gridRows - 1, 4));
    }


    public void testMoveUp()
    {
        grid.insertDroppable(gem1, 2, 4);

        grid.moveDroppable(gem1, GO_UP);
        assertTrue("Gem didn't move up", grid.isDroppableAt(1, 4));
    }


    public void testMoveUpWithCollision()
    {
        grid.insertDroppable(gem1, 0, 4);

        grid.moveDroppable(gem1, GO_UP);
        assertTrue("Gem must remain to the top", grid.isDroppableAt(0, 4));
    }


    public void testStrongerGravity()
    {
        grid.setStrongerGravity();
        assertEquals(4.0, grid.getActualGravity(), 0.001f);
    }


    public void testStrongestGravity()
    {
        grid.setStrongestGravity();
        assertEquals(10.0, grid.getActualGravity(), 0.001f);
    }


    public void testNormalGravity()
    {
        grid.setStrongerGravity();
        grid.setNormalGravity();
        assertEquals(0.5, grid.getActualGravity(), 0.001f);
    }


    public void testCanMoveWithNullGem()
    {
        assertFalse("gemCanMov should return false with null gem",
            grid.droppableCanMove(null, GO_DOWN));

    }


    public void testGetRows()
    {
        assertEquals("Grid must return right number of rows",
            environment.getConfig().getInteger("rows"), grid.getNumberOfRows());
    }


    public void testGetColumns()
    {
        assertEquals("Grid must return right number of columns",
            environment.getConfig().getInteger("columns"),
            grid.getNumberOfColumns());
    }


    public void testDroppedGemDoesntMoves()
    {
        grid.insertDroppable(gem1, 13, 4);
        grid.insertDroppable(gem2, 12, 4);

        grid.updateDroppable(gem1);
        grid.updateDroppable(gem2);

        grid.removeDroppableFromGrid(gem1);

        grid.updateDroppable(gem2);

        assertEquals("stopped gem must not move", 12,
            gem2.getCell().getTopRow());
    }


    public void testDroppedGemCanMove()
    {
        grid.insertDroppable(gem1, 13, 4);
        grid.insertDroppable(gem2, 12, 4);

        grid.updateDroppable(gem1);
        grid.updateDroppable(gem2);

        grid.removeDroppableFromGrid(gem1);

        assertTrue("stopped gem must move", droppedGemCanMoveDown(grid));
    }


    public void testDroppedGemCantMove()
    {
        grid.insertDroppable(gem1, 13, 4);
        grid.insertDroppable(gem2, 12, 4);

        grid.updateDroppable(gem1);
        grid.updateDroppable(gem2);

        assertFalse("stopped gem must not move", droppedGemCanMoveDown(grid));
    }


    public void testDroppedBigGemCanMove()
    {
        grid.insertDroppable(gem1, 13, 4);
        grid.insertDroppable(gem2, 13, 3);

        grid.updateDroppable(gem1);
        grid.updateDroppable(gem2);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 4);
        insertAndUpdate(createGem(DIAMOND), 11, 3);
        insertAndUpdate(createGem(DIAMOND), 11, 4);

        grid.removeDroppableFromGrid(gem1);
        grid.removeDroppableFromGrid(gem2);

        grid.updateBigGems();

        assertTrue("stopped gem must move", droppedGemCanMoveDown(grid));
    }


    public void testDroppedBigGemCantMove()
    {
        insertAndUpdate(createGem(RUBY), 13, 3);
        grid.insertDroppable(gem1, 13, 4);

        grid.updateDroppable(gem1);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 4);
        insertAndUpdate(createGem(DIAMOND), 11, 3);
        insertAndUpdate(createGem(DIAMOND), 11, 4);

        grid.removeDroppableFromGrid(gem1);

        grid.updateBigGems();

        assertFalse("stopped gem must not move", droppedGemCanMoveDown(grid));
    }


    public void testChainCounterAfterCreation()
    {
        assertEquals("chainCounter Must be zero after the grid creation", 0,
            grid.getChainCounter());
    }


    public void testBigGemCrushedIncrementChainCounter()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 2);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateBigGems();

        grid.updateCrushes();

        assertTrue("chainCounter can not be zero after a successful crush",
            grid.getChainCounter() != 0);
    }


    public void testGemCrushIncrementChainCounter()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 2);

        grid.updateCrushes();

        assertTrue("chainCounter can not be zero after a successful crush",
            grid.getChainCounter() != 0);
    }


    public void testNoGemCrush()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 2);

        grid.updateCrushes();

        assertEquals("chainCounter must be zero when no gem are crushed", 0,
            grid.getChainCounter());
        assertEquals("crushedGemsCounter must be zero when no gem are crushed",
            0, grid.getChainCounter());
    }


    public void testCrushedGemsAfterCreation()
    {
        assertEquals("crushedGemsCounter must be zero after grid creation", 0,
            grid.getCrushedGemsCounter());
    }


    public void testGemCrushIncrementCrushedGemsCounter()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 2);

        grid.updateCrushes();

        assertTrue(
            "crushedGemsCounter can not be zero after a successful crush",
            grid.getCrushedGemsCounter() != 0);
    }


    public void testCrushedGemsDoNotIncludeChest()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 2);

        grid.updateCrushes();

        assertEquals(
            "crushedGemsCounter must be 1 because chest are not counted", 1,
            grid.getCrushedGemsCounter());
    }


    public void testCrushedGemsDoNotIncludeFlash()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createFlashingGem(), 13, 2);

        grid.updateCrushes();

        assertEquals(
            "crushedGemsCounter must be 0 because flashgem and gem crushed by flash are not counted",
            0, grid.getCrushedGemsCounter());
    }


    public void testBigGemCrushedDoubleTheGems()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 2);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateBigGems();

        grid.updateCrushes();

        assertEquals(
            "crushedGemsCounter must be 8 because biggems size is moltiplied by two",
            8, grid.getCrushedGemsCounter());
    }


    public void testColumnHeightIsZero()
    {
        assertEquals("Column height must be 0", 0, grid.getHeightOfColumn(0));
    }


    public void testColumnHeightIsOne()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        assertEquals("Column height must be 1", 1, grid.getHeightOfColumn(2));
    }


    public void testColumnHeightWithHoles()
    {
        Droppable floatingGem = createGem(DIAMOND);
        floatingGem.getFallingObject().drop();
        insertAndUpdate(floatingGem, 4, 2);

        assertEquals("Column height must be 10", 10, grid.getHeightOfColumn(2));
    }


    public void testColumnHeightWithFallingGems()
    {
        Droppable floatingGem = createGem(DIAMOND);
        insertAndUpdate(floatingGem, 4, 2);

        assertEquals("Column height must be 10", 0, grid.getHeightOfColumn(2));
    }


    public void testCrushCounterIsReset()
    {
        insert2x2BlockOfGems(DIAMOND, 12, 2);
        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateCrushes();
        assertEquals(4, grid.getCrushedGemsCounter());
        grid.resetCrushedGemsCounter();
        assertEquals(0, grid.getCrushedGemsCounter());
    }


    public void testGetRowUpperBound()
    {
        for(int i = 0; i < grid.getNumberOfRows(); i++)
        {
            assertEquals(i * yStep + bounds.top(), grid.getRowUpperBound(i));
        }
    }


    public void incrementChainCounter()
    {
        grid.resetChainCounter();
        assertEquals(0, grid.getChainCounter());

        grid.incrementChainCounter();
        assertEquals(1, grid.getChainCounter());
    }


    public void testApplyAction()
    {
        MockAction action = new MockAction();
        grid.doAction(action);
        assertTrue(action.beenApplied());
    }


    public void testDontCrushFallingGemByChest()
    {
        insertAndUpdate(createGem(RUBY), 13, 0);
        insertAndUpdate(createGem(RUBY), 12, 0);
        insertAndUpdate(createGem(RUBY), 11, 0);
        insertAndUpdate(createChest(DIAMOND), 10, 0);

        Droppable gem = createGem(DIAMOND);

        grid.insertDroppable(gem, 10, 1);

        grid.updateCrushes();

        assertEquals("chainCounter must be zero", 0, grid.getChainCounter());
    }


    public void testDontCrushFallingChest()
    {
        insertAndUpdate(createGem(RUBY), 13, 0);
        insertAndUpdate(createGem(RUBY), 12, 0);
        insertAndUpdate(createGem(RUBY), 11, 0);
        insertAndUpdate(createGem(DIAMOND), 10, 0);

        Droppable gem = createChest(DIAMOND);

        grid.insertDroppable(gem, 10, 1);

        grid.updateCrushes();

        assertEquals("chainCounter must be zero", 0, grid.getChainCounter());
    }


    public void testDontCrushFallingFlash()
    {
        insertAndUpdate(createGem(RUBY), 13, 0);
        insertAndUpdate(createGem(RUBY), 12, 0);
        insertAndUpdate(createGem(RUBY), 11, 0);
        insertAndUpdate(createGem(DIAMOND), 10, 0);

        Droppable gem = createFlashingGem();

        grid.insertDroppable(gem, 10, 1);

        grid.updateCrushes();

        assertNotNull("flash must no be cancelled", grid.getDroppableAt(10, 1));
    }

}
