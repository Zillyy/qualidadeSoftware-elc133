package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public class UpdateAnimationsAction extends AbstractAction
{
    private long timer;


    public UpdateAnimationsAction(long timer)
    {
        this.timer = timer;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        if(gem.getAnimatedObject() != null)
        {
            gem.getAnimatedObject().update(timer);
        }
    }
}
