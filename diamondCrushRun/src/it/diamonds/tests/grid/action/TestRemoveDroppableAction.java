package it.diamonds.tests.grid.action;


import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.grid.action.RemoveDroppableAction;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestRemoveDroppableAction extends AbstractGridTestCase
{
    private Droppable droppable;

    private RemoveDroppableAction action;


    public void setUp() throws IOException
    {
        super.setUp();
        droppable = createGem(EMERALD);
        grid.insertDroppable(droppable, 4, 2);
        action = new RemoveDroppableAction(droppable);
    }


    public void testRemoval()
    {
        grid.doAction(action);
        assertFalse(grid.isDroppableAt(4, 2));
    }


    public void testNullRemoval()
    {
        try
        {
            grid.doAction(new RemoveDroppableAction(null));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("actions tries to remove null droppable");
    }


    public void testInvalidRemoval()
    {
        try
        {
            grid.doAction(new RemoveDroppableAction(createGem(EMERALD)));
        }
        catch(IllegalArgumentException e)
        {
            return;
        }
        fail("action tried to remove non present gem");
    }

}
