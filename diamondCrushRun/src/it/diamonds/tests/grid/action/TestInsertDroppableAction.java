package it.diamonds.tests.grid.action;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import it.diamonds.droppable.Droppable;
import it.diamonds.grid.action.InsertDroppableAction;
import it.diamonds.tests.grid.AbstractGridTestCase;


public class TestInsertDroppableAction extends AbstractGridTestCase
{
    public void testNullInsertion()
    {
        try
        {
            grid.doAction(new InsertDroppableAction(null, 0, 0));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("");
    }


    public void testInvalidPosition()
    {
        try
        {
            grid.doAction(new InsertDroppableAction(createGem(TOPAZ), 100, 100));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("");
    }


    public void testInsertion()
    {
        grid.doAction(new InsertDroppableAction(createGem(DIAMOND), 1, 2));
        assertTrue(grid.isDroppableAt(1, 2));
    }


    public void testOtherInsertion()
    {
        grid.doAction(new InsertDroppableAction(createGem(EMERALD), 2, 4));
        assertTrue(grid.isDroppableAt(2, 4));
    }


    public void testInsertionIsCorrect()
    {
        Droppable gem = createGem(TOPAZ);
        grid.doAction(new InsertDroppableAction(gem, 8, 4));
        assertSame(gem, grid.getDroppableAt(8, 4));
    }


    public void testDoubleInsertionSamePosition()
    {
        grid.doAction(new InsertDroppableAction(createGem(DIAMOND), 0, 0));
        try
        {
            grid.doAction(new InsertDroppableAction(createGem(DIAMOND), 0, 0));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("");
    }


    public void testDoubleInsertionSameGem()
    {
        Droppable gem = createGem(EMERALD);
        grid.doAction(new InsertDroppableAction(gem, 2, 1));
        try
        {
            grid.doAction(new InsertDroppableAction(gem, 2, 1));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("");
    }

}
