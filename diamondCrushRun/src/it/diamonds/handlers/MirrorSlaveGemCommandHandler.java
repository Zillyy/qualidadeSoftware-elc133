package it.diamonds.handlers;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.InputReactor;


public class MirrorSlaveGemCommandHandler extends AbstractEventHandler
{
    private DroppablesPair gemsPair;


    public MirrorSlaveGemCommandHandler(DroppablesPair gemsPair,
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
            gemsPair.mirrorSlave();
        }
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        ;
    }
}
