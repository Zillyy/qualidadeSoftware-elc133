package it.diamonds.grid.action;


import static it.diamonds.droppable.DroppableType.GEM;

import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.droppable.DroppableList;
import it.diamonds.engine.Environment;
import it.diamonds.engine.TimerInterface;
import it.diamonds.grid.Grid;


public class TurnStonesIntoGemsAction extends AbstractAction
{

    private TimerInterface timer;

    private transient DroppableFactory gemFactory;

    private int turningStonesCounter = 0;


    public TurnStonesIntoGemsAction(Environment environment, Grid grid)
    {
        this.timer = environment.getTimer();
        gemFactory = new DroppableFactory(environment);
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        if(!gem.getGridObject().getType().isStone())
        {
            return;
        }

        if(gem.getAnimatedObject().getCurrentFrame() < 5)
        {
            return;
        }
        turningStonesCounter++;

        if(gem.getAnimatedObject().getCurrentFrame() >= 7)
        {
            turningStonesCounter--;
            getGridElements().remove(gem);
            Droppable newGem = gemFactory.create(GEM,
                gem.getGridObject().getColor());
            newGem.getFallingObject().drop();
            getGrid().insertDroppable(newGem, gem.getCell().getTopRow(),
                gem.getCell().getLeftColumn());
        }

        gem.getAnimatedObject().update(timer.getTime());
    }


    public int getNumberOfTurningStones()
    {
        return turningStonesCounter;
    }
}
