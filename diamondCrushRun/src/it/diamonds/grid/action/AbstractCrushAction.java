package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public abstract class AbstractCrushAction extends AbstractAction
{

    private transient Grid grid;

    private boolean addAtScore;

    private int crushedGemsCounter;


    public AbstractCrushAction(Grid grid)
    {
        this.grid = grid;
    }


    protected void crushDroppables(DroppableList crushableGems)
    {
        for(Droppable gemToCrush : crushableGems)
        {
            if(addAtScore)
            {
                grid.getScoreCalculator().addScoreForGem(gemToCrush);
            }

            if(gemToCrush.getGridObject().getType().isGem())
            {
                int area = gemToCrush.getCell().getHeight()
                    * gemToCrush.getCell().getWidth();
                crushedGemsCounter += area;

                if(area != 1)
                {
                    crushedGemsCounter += area;
                }
            }

            removeAndClean(gemToCrush);
        }

    }


    protected Grid getGrid()
    {
        return grid;
    }


    public int getCrushedGemsCounter()
    {
        return crushedGemsCounter;
    }


    private void removeAndClean(Droppable gem)
    {
        grid.doAction(new RemoveDroppableAction(gem));
    }


    protected void setAddAtScore(boolean addAtScore)
    {
        this.addAtScore = addAtScore;
    }


    public void reset()
    {
        crushedGemsCounter = 0;
    }

}
