package it.diamonds.tests.grid.action;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockAction;

import java.io.IOException;


public class TestAbstractIteration extends AbstractGridTestCase
{
    private MockAction mockAction;


    public void setUp() throws IOException
    {
        super.setUp();
        mockAction = new MockAction();
    }


    public void testForEachGemNull()
    {
        try
        {
            grid.doAction(mockAction);
        }
        catch(NullPointerException exception)
        {
            fail("the forEachGem call action for null gem");
        }
    }


    private void fillGrid()
    {
        for(int row = grid.getNumberOfRows() - 1; row >= 0; row--)
        {
            for(int column = grid.getNumberOfColumns() - 1; column >= 0; column--)
            {
                Droppable gem = Gem.create(environment.getEngine(), DIAMOND,
                    3500);
                grid.insertDroppable(gem, row, column);
            }
        }
    }


    private DroppableList importGridElements()
    {
        DroppableList testList = new DroppableList();

        for(int i = grid.getNumberOfRows() - 1; i >= 0; i--)
        {
            for(int k = grid.getNumberOfColumns() - 1; k >= 0; k--)
            {
                testList.add(grid.getDroppableAt(i, k));
            }
        }

        return testList;
    }


    public void testForEachGem()
    {
        fillGrid();

        DroppableList testList = importGridElements();

        for(Droppable gem : testList)
        {
            assertFalse(mockAction.isPassedOnGem(gem));
        }

        grid.doAction(mockAction);

        for(Droppable gem : testList)
        {
            assertTrue(mockAction.isPassedOnGem(gem));
        }
    }


    public void testGetGrid()
    {
        DroppableList gridElements = new DroppableList();
        mockAction.apply(grid, gridElements);
        assertSame(grid, mockAction.getGrid());
    }


    public void testGetGridElements()
    {
        DroppableList gridElements = new DroppableList();
        mockAction.apply(grid, gridElements);
        assertSame(gridElements, mockAction.getGridElements());
    }

}
