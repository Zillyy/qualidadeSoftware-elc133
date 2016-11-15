package it.diamonds.engine.input;


import it.diamonds.engine.input.Event.Code;

import java.util.HashMap;


public class InputReactor implements InputReactorInterface
{
    private long lastInputTimeStamp;

    private long normalRepeatDelay;

    private long fastRepeatDelay;

    private HashMap<Code, EventHandler> eventHandlersMap;

    private Input input;


    public InputReactor(Input input, int normalRepeatDelay, int fastRepeatDelay)
    {
        this.input = input;

        this.normalRepeatDelay = normalRepeatDelay;
        this.fastRepeatDelay = fastRepeatDelay;

        eventHandlersMap = new HashMap<Code, EventHandler>();
    }


    public void addHandler(Code code, AbstractEventHandler handler)
    {
        eventHandlersMap.put(code, handler);
        handler.setNormalRepeatDelay(normalRepeatDelay);
        handler.setFastRepeatDelay(fastRepeatDelay);
    }


    public long getNormalRepeatDelay()
    {
        return normalRepeatDelay;
    }


    public long getFastRepeatDelay()
    {
        return fastRepeatDelay;
    }


    private void handleEvent(Event event)
    {
        Code code = event.getCode();
        if(eventHandlersMap.containsKey(code))
        {
            eventHandlersMap.get(code).handleEvent(this, event);
        }
    }


    public EventHandler getEventHandler(Code code)
    {
        return eventHandlersMap.get(code);
    }


    private void handleEventRepetition()
    {
        for(Code code : eventHandlersMap.keySet())
        {
            EventHandler eventHandler = eventHandlersMap.get(code);

            eventHandler.handleRepetition(this);
        }
    }


    public long getLastInputTimeStamp()
    {
        return lastInputTimeStamp;
    }


    public void reactToInput(long timer)
    {
        lastInputTimeStamp = timer;

        while(!input.isEmpty())
        {
            handleEvent(input.extractEvent());
        }

        handleEventRepetition();
    }


    public void emptyQueue()
    {
        while(!input.isEmpty())
        {
            input.extractEvent();
        }
    }
}
