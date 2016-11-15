package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public class TryToMoveDroppableAction extends AbstractAction
{

    private transient Droppable droppable;

    private int row;

    private int column;


    public TryToMoveDroppableAction(Droppable droppable, int row, int column)
    {
        this.droppable = droppable;
        this.row = row;
        this.column = column;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        if(grid.droppableCanBePutAt(row, column))
        {
            droppable.getMoveableObject().moveToCell(row, column);
        }
    }


    protected void applyOn(Droppable gem)
    {
        ;
    }
}
