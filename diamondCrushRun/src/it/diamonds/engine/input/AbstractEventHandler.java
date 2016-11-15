package it.diamonds.engine.input;


import it.diamonds.engine.input.Event.State;


public abstract class AbstractEventHandler implements EventHandler
{
    private State eventState = State.RELEASED;

    private long timeStamp = 0;

    private boolean repeated = false;

    private long normalRepeatDelay;

    private long fastRepeatDelay;


    public long getNormalRepeatDelay()
    {
        return normalRepeatDelay;
    }


    public void setNormalRepeatDelay(long repeatDelay)
    {
        normalRepeatDelay = repeatDelay;
    }


    public long getFastRepeatDelay()
    {
        return fastRepeatDelay;
    }


    public void setFastRepeatDelay(long repeatDelay)
    {
        fastRepeatDelay = repeatDelay;
    }


    public long getCurrentRepeatDelay()
    {
        if(repeated)
        {
            return fastRepeatDelay;
        }
        else
        {
            return normalRepeatDelay;
        }
    }


    public void handleEvent(InputReactor inputReactor, Event event)
    {
        update(event);
        execute(inputReactor);
    }


    public void handleRepetition(InputReactor inputReactor)
    {
        if(repeatDelayHasExpired(inputReactor.getLastInputTimeStamp()))
        {
            repeated = true;

            timeStamp = inputReactor.getLastInputTimeStamp();

            execute(inputReactor);
        }
    }


    public boolean isPressed()
    {
        return eventState == State.PRESSED;
    }


    private void update(Event event)
    {
        eventState = event.getState();

        if(isPressed())
        {
            timeStamp = event.getTimestamp();
        }
        else
        {
            timeStamp = 0;
            repeated = false;
        }
    }


    protected abstract void executeWhenPressed(InputReactor inputReactor);


    protected abstract void executeWhenReleased(InputReactor inputReactor);


    private void execute(InputReactor inputReactor)
    {
        if(isPressed())
        {
            executeWhenPressed(inputReactor);
        }
        else
        {
            executeWhenReleased(inputReactor);
        }
    }


    public boolean isRepeated(long timeStamp)
    {
        return repeated || repeatDelayHasExpired(timeStamp);
    }


    private boolean repeatDelayHasExpired(long timeStamp)
    {
        return isPressed()
            && timeStamp > this.timeStamp + getCurrentRepeatDelay();
    }
}
