package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public class RemoveDroppableAction extends AbstractAction
{

    private transient Droppable toBeRemoved;


    public RemoveDroppableAction(Droppable toBeRemoved)
    {
        this.toBeRemoved = toBeRemoved;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        if(!gridElements.remove(toBeRemoved))
        {
            throw new IllegalArgumentException();
        }
    }


    protected void applyOn(Droppable gem)
    {
        ;
    }
}
