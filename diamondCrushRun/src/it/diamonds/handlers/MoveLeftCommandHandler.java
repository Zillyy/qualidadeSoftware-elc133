package it.diamonds.handlers;


import static it.diamonds.droppable.pair.Direction.GO_LEFT;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.EventHandler;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;


public final class MoveLeftCommandHandler extends AbstractEventHandler
{
    private DroppablesPair gemsPair;


    public MoveLeftCommandHandler(DroppablesPair gemsPair)
    {
        this.gemsPair = gemsPair;
    }


    public void handleRepetition(InputReactor inputReactor)
    {
        if(!inputReactor.getEventHandler(Code.RIGHT).isPressed())
        {
            super.handleRepetition(inputReactor);
        }
    }


    public void executeWhenPressed(InputReactor inputReactor)
    {
        EventHandler rightHandler = inputReactor.getEventHandler(Code.RIGHT);

        if(rightHandler.isRepeated(inputReactor.getLastInputTimeStamp())
            || !gemsPair.canReactToInput())
        {
            return;
        }

        gemsPair.move(GO_LEFT);
    }


    public void executeWhenReleased(InputReactor inputReactor)
    {
        ;
    }
}
