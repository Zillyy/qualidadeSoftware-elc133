package it.diamonds.handlers;


import static it.diamonds.droppable.pair.Direction.GO_RIGHT;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.EventHandler;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;


public final class MoveRightCommandHandler extends AbstractEventHandler
{
    private DroppablesPair gemsPair;


    public MoveRightCommandHandler(DroppablesPair gemsPair)
    {
        this.gemsPair = gemsPair;
    }


    public void handleRepetition(InputReactor inputReactor)
    {
        if(!inputReactor.getEventHandler(Code.LEFT).isPressed())
        {
            super.handleRepetition(inputReactor);
        }
    }


    public void executeWhenPressed(InputReactor inputReactor)
    {
        EventHandler leftHandler = inputReactor.getEventHandler(Code.LEFT);

        if(leftHandler.isRepeated(inputReactor.getLastInputTimeStamp())
            || !gemsPair.canReactToInput())
        {
            return;
        }

        gemsPair.move(GO_RIGHT);
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        ;
    }
}
