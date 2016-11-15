package it.diamonds.tests.mocks;


import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.EventHandler;
import it.diamonds.engine.input.InputReactorInterface;
import it.diamonds.engine.input.Event.Code;


public class MockInputReactor implements InputReactorInterface
{
    private boolean reacted = false;


    public void addHandler(Code code, AbstractEventHandler handler)
    {
        ;
    }


    public EventHandler getEventHandler(Code code)
    {
        return null;
    }


    public long getNormalRepeatDelay()
    {
        return 0;
    }


    public long getFastRepeatDelay()
    {
        return 0;
    }


    public long getLastInputTimeStamp()
    {
        return 0;
    }


    public void reactToInput(long timer)
    {
        reacted = true;
    }


    public boolean hasReacted()
    {
        return reacted;
    }


    public void emptyQueue()
    {
        ;
    }

}
