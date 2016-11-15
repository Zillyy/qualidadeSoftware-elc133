package it.diamonds.tests.grid.action;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.grid.action.TryToMoveDroppableAction;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestTryToMoveDroppableAction extends AbstractGridTestCase
{
    private Droppable droppable;


    public void setUp() throws IOException
    {
        super.setUp();
        droppable = createGem(DIAMOND);
    }


    public void testMove()
    {
        grid.insertDroppable(droppable, 3, 4);
        grid.doAction(new TryToMoveDroppableAction(droppable, 5, 2));
        assertTrue(grid.isDroppableAt(5, 2));
    }


    public void testDontMoveBehindLeftWall()
    {
        grid.insertDroppable(droppable, 6, 2);
        grid.doAction(new TryToMoveDroppableAction(droppable, 5, -1));
        assertTrue(grid.isDroppableAt(6, 2));
    }


    public void testDontMoveBehindRightWall()
    {
        grid.insertDroppable(droppable, 8, 5);
        grid.doAction(new TryToMoveDroppableAction(droppable, 7, 8));
        assertTrue(grid.isDroppableAt(8, 5));
    }


    public void testDontMoveToOccupiedCell()
    {
        grid.insertDroppable(droppable, 5, 1);
        grid.insertDroppable(createGem(EMERALD), 4, 6);
        grid.doAction(new TryToMoveDroppableAction(droppable, 4, 6));
        assertTrue(grid.isDroppableAt(5, 1));
    }

}
