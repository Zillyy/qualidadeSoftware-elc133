package it.diamonds.handlers;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.InputReactor;


public final class RotateCounterclockwiseCommandHandler extends
    AbstractEventHandler
{
    private DroppablesPair gemsPair;


    public RotateCounterclockwiseCommandHandler(DroppablesPair gemsPair,
        long normalDelay, long fastDelay)
    {
        this.gemsPair = gemsPair;

        setFastRepeatDelay(fastDelay);
        setNormalRepeatDelay(normalDelay);
    }


    public void executeWhenPressed(InputReactor inputReactor)
    {
        if(gemsPair.canReactToInput())
        {
            gemsPair.rotateCounterclockwise();
        }
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        ;
    }
}
