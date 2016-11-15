package it.diamonds.handlers;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.grid.Grid;


public class DropCommandHandler extends AbstractEventHandler
{
    private Grid grid;

    private DroppablesPair gemsPair;


    public DropCommandHandler(Grid grid, DroppablesPair gemsPair)
    {
        this.grid = grid;
        this.gemsPair = gemsPair;
    }


    public void executeWhenPressed(InputReactor inputReactor)
    {
        if(gemsPair.canReactToInput())
        {
            grid.setStrongerGravity();
        }
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        if(gemsPair.canReactToInput())
        {
            grid.setNormalGravity();
        }
    }
}
