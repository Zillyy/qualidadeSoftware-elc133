package it.diamonds.grid.query;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.grid.Grid;


/* TODO: questa classe  testata troppo indirettamente: creare un test case apposito. */

public class CanMoveDownQuery extends AbstractQuery
{
    private DroppablesPair pair;

    private boolean result;


    public CanMoveDownQuery(DroppablesPair pair)
    {
        this.pair = pair;
        result = false;
    }


    public boolean getResult()
    {
        return result;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
        // I HATE CHECKSTYLE!!!
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

        result |= gem.getMovingDownObject().canMoveDown(getGrid());
    }
}
