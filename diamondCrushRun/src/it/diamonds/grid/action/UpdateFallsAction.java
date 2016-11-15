package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.grid.Grid;


/* TODO: questa classe è testata troppo indirettamente: creare un test case apposito. */

public class UpdateFallsAction extends AbstractAction
{

    private DroppablesPair pair;


    public UpdateFallsAction(DroppablesPair pair)
    {
        this.pair = pair;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        if(pair != null)
        {
            if(gem == pair.getPivot() || gem == pair.getSlave())
            {
                return;
            }
        }

        gem.getMovingDownObject().moveDown(getGrid());
    }
}
