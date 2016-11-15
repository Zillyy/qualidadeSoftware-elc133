package it.diamonds.grid.iteration;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public abstract class AbstractGridIteration
{
    private Grid grid;

    private DroppableList gridElements;


    protected void forEachDroppable(Grid grid)
    {
        DroppableList alreadyApplied = new DroppableList();

        for(int row = grid.getNumberOfRows() - 1; row >= 0; row--)
        {
            for(int column = 0; column < grid.getNumberOfColumns(); column++)
            {
                Droppable droppable = grid.getDroppableAt(row, column);
                if(droppable != null && !alreadyApplied.contains(droppable))
                {
                    applyOn(droppable);
                    alreadyApplied.add(droppable);
                }
            }
        }
    }


    protected Grid getGrid()
    {
        return grid;
    }


    protected DroppableList getGridElements()
    {
        return gridElements;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        this.grid = grid;
        this.gridElements = gridElements;
    }


    protected abstract void applyOn(Droppable gem);

}
