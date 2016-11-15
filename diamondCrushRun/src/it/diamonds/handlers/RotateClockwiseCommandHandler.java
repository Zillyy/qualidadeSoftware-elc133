package it.diamonds.handlers;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.InputReactor;


public class RotateClockwiseCommandHandler extends AbstractEventHandler
{
    private DroppablesPair gemsPair;


    public RotateClockwiseCommandHandler(DroppablesPair gemsPair,
        long normalDelay, long fastDelay)
    {
        this.gemsPair = gemsPair;

        this.setFastRepeatDelay(fastDelay);
        this.setNormalRepeatDelay(normalDelay);
    }


    public void executeWhenPressed(InputReactor inputReactor)
    {
        if(gemsPair.canReactToInput())
        {
            gemsPair.rotateClockwise();
        }
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        ;
    }
}
