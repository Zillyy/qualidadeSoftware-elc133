package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public class InsertDroppableAction extends AbstractAction
{

    private transient Droppable droppable;

    private int row;

    private int column;


    public InsertDroppableAction(Droppable droppable, int row, int column)
    {
        if(null == droppable)
        {
            throw new IllegalArgumentException();
        }
        this.droppable = droppable;
        this.row = row;
        this.column = column;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        if(!grid.isValidCell(row, column) || grid.isDroppableAt(row, column)
            || gridElements.contains(droppable))
        {
            throw new IllegalArgumentException();
        }

        droppable.getCell().setRow(row);
        droppable.getCell().setColumn(column);

        /* TODO: testare questa chiamata */
        grid.alignDroppableToCellUpperBound(droppable);

        gridElements.add(droppable);
    }


    protected void applyOn(Droppable gem)
    {
        ;
    }
}
