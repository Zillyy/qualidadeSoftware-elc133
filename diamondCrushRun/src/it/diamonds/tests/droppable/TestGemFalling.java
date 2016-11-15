package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import it.diamonds.droppable.Droppable;
import it.diamonds.tests.grid.AbstractGridTestCase;


public class TestGemFalling extends AbstractGridTestCase
{
    public void testGemFalling()
    {
        Droppable gem = createGem(EMERALD);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(gem, 12, 3);

        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateCrushes();

        updateFalls(grid);

        assertEquals("Gem must be in the last row", 13,
            gem.getCell().getTopRow());
    }


    public void testTwoGemFalling()
    {
        Droppable firstGem = createGem(EMERALD);
        Droppable secondGem = createGem(EMERALD);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(firstGem, 12, 3);

        insertAndUpdate(secondGem, 12, 2);

        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateCrushes();

        updateFalls(grid);

        assertEquals("Gem must be in the last row", 13,
            firstGem.getCell().getTopRow());
        assertEquals("Gem must be in the last row", 13,
            secondGem.getCell().getTopRow());
    }


    public void testGemFallingFromHigher()
    {
        Droppable gem = createGem(EMERALD);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 3);

        insertAndUpdate(gem, 11, 3);

        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateCrushes();

        for(int i = 0; i < 80; i++)
        {
            updateFalls(grid);
        }
        assertEquals("Gem must be in the last row", 13,
            gem.getCell().getTopRow());
    }


    public void testTwoVerticalGemFalling()
    {
        Droppable firstGem = createGem(EMERALD);
        Droppable secondGem = createGem(EMERALD);
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(firstGem, 12, 3);

        insertAndUpdate(secondGem, 11, 3);

        insertAndUpdate(createChest(DIAMOND), 13, 4);

        grid.updateCrushes();

        updateFalls(grid);

        assertEquals("Gem must be in the last row", 13,
            firstGem.getCell().getTopRow());
        assertEquals("Gem must be in the last row", 12,
            secondGem.getCell().getTopRow());
    }


    public void testBigGemFalling()
    {

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insert2x2BlockOfGems(EMERALD, 11, 3);

        grid.updateBigGems();
        grid.updateCrushes();

        updateFalls(grid);
        assertNotSame("Big Gem must be here", null, grid.getDroppableAt(13, 3));
    }


    public void testBigGemNotFalling()
    {

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);

        insertAndUpdate(createGem(RUBY), 13, 5);

        insert2x2BlockOfGems(EMERALD, 11, 3);
        insertAndUpdate(createGem(EMERALD), 12, 5);
        insertAndUpdate(createGem(EMERALD), 11, 5);

        insertAndUpdate(createChest(DIAMOND), 13, 2);

        grid.updateBigGems();
        grid.updateCrushes();

        updateFalls(grid);
        assertSame("Big Gem must be not here", null, grid.getDroppableAt(13, 3));
    }


    public void testBigGemFallingFromHigher()
    {

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createGem(RUBY), 12, 3);
        insertAndUpdate(createGem(RUBY), 12, 4);

        insert2x2BlockOfGems(EMERALD, 10, 3);

        insertAndUpdate(createChest(DIAMOND), 13, 2);
        insertAndUpdate(createChest(RUBY), 12, 2);

        grid.updateBigGems();
        grid.updateCrushes();

        for(int i = 0; i < 100; i++)
        {
            updateFalls(grid);
        }
        assertNotSame("Big Gem must be here", null, grid.getDroppableAt(13, 3));
    }


    public void testTwoVerticalBigGemFalling()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insert2x2BlockOfGems(EMERALD, 11, 3);
        insert2x2BlockOfGems(RUBY, 9, 3);

        grid.updateBigGems();
        grid.updateCrushes();

        updateFalls(grid);

        assertNotSame("Big Gem must be not here", null, grid.getDroppableAt(13,
            3));
        assertNotSame("Big Gem must be not here", null, grid.getDroppableAt(11,
            3));
    }


    public void testBigGemNotFallingForLeftObstacle()
    {

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);

        insertAndUpdate(createGem(RUBY), 13, 2);

        insert2x2BlockOfGems(EMERALD, 11, 3);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createGem(EMERALD), 11, 2);

        insertAndUpdate(createChest(DIAMOND), 13, 5);

        grid.updateBigGems();
        grid.updateCrushes();

        updateFalls(grid);
        assertSame("Big Gem must be not here", null, grid.getDroppableAt(13, 3));
    }


    public void testGemFallingFromVeryHigher()
    {
        Droppable gem1 = createGem(EMERALD);
        Droppable gem2 = createGem(RUBY);

        insertAndUpdate(gem1, 1, 4);
        insertAndUpdate(gem2, 0, 4);

        for(int i = 0; i < 800; i++)
        {
            updateFalls(grid);
        }
        assertSame("Gem must be here", gem1, grid.getDroppableAt(13, 4));
        assertSame("Gem must be here", gem2, grid.getDroppableAt(12, 4));
    }


    public void testUpdateOnGridController()
    {
        grid = controller.getGrid();

        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createChest(DIAMOND), 13, 3);
        insertAndUpdate(createGem(EMERALD), 12, 2);

        dropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertTrue("This gem must be an EMERALD",
            grid.getDroppableAt(13, 2).getGridObject().getColor().equals(
                EMERALD));
    }


    public void testUpdateCrushesAfterUpdateFalls()
    {
        grid = controller.getGrid();

        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createChest(DIAMOND), 13, 3);
        insertAndUpdate(createChest(EMERALD), 13, 1);
        insertAndUpdate(createGem(EMERALD), 12, 2);

        dropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertEquals("grid must contain 2 gems", 2,
            grid.getNumberOfDroppables());
    }


    public void testGemFallingWithGravityWithDroppedGem()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 3);

        insertAndUpdate(createGem(RUBY), 13, 6);
        insertAndUpdate(createGem(RUBY), 12, 6);
        insertAndUpdate(createGem(RUBY), 11, 6);

        grid.updateCrushes();
        updateFalls(grid);

        assertTrue("Gem mst be moved", droppedGemCanMoveDown(grid));
    }


    public void testBigGemFallingWithGravityWithDroppedGem()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 3);

        insert2x2BlockOfGems(RUBY, 12, 6);

        grid.updateBigGems();
        grid.updateCrushes();
        updateFalls(grid);

        assertTrue("Gem mst be moved", droppedGemCanMoveDown(grid));
    }


    public void testBigGemNotFallingFromHigher()
    {

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);

        insertAndUpdate(createGem(RUBY), 12, 3);
        insertAndUpdate(createGem(RUBY), 12, 4);
        insertAndUpdate(createGem(RUBY), 13, 5);
        insertAndUpdate(createGem(RUBY), 12, 5);

        insert2x2BlockOfGems(EMERALD, 10, 4);

        insertAndUpdate(createChest(RUBY), 13, 6);

        grid.updateBigGems();
        grid.updateCrushes();

        for(int i = 0; i < 800; i++)
        {
            updateFalls(grid);
        }
        assertSame("Big Gem must be not here", null, grid.getDroppableAt(13, 5));
    }


    private void dropGemsPair()
    {
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
    }
}
