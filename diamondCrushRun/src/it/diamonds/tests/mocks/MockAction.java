package it.diamonds.tests.mocks;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;
import it.diamonds.grid.action.AbstractAction;


public class MockAction extends AbstractAction
{
    private int mockField = 0;

    private boolean applied = false;


    public Grid getGrid()
    {
        return super.getGrid();
    }


    public DroppableList getGridElements()
    {
        return super.getGridElements();
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
        applied = true;
    }


    public void applyOn(Droppable gem)
    {
        gem.getFallingObject().drop();
        applied = true;
    }


    public boolean beenApplied()
    {
        return applied;
    }


    public boolean isPassedOnGem(Droppable gem)
    {
        return !gem.getFallingObject().isFalling();
    }


    public void setMockField(int value)
    {
        mockField = value;
    }


    public int getMockField()
    {
        return mockField;
    }
}
