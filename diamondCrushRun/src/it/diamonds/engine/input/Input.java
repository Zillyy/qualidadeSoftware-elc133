package it.diamonds.engine.input;


import it.diamonds.engine.TimerInterface;
import it.diamonds.engine.input.Event.Code;

import java.util.LinkedList;


public final class Input implements InputListenerInterface
{

    private InputDeviceInterface device;

    private TimerInterface timer;

    private LinkedList<Event> queue;

    private EventMappings eventMappings;


    private Input(InputDeviceInterface device, TimerInterface timer)
    {
        this.timer = timer;
        this.device = device;
        this.device.addListener(this);

        queue = new LinkedList<Event>();
    }


    public static Input create(InputDeviceInterface input, TimerInterface timer)
    {
        return new Input(input, timer);
    }


    public boolean isEmpty()
    {
        return queue.isEmpty();
    }


    public Event extractEvent()
    {
        return queue.remove();
    }


    public void flushEvents()
    {
        while(!queue.isEmpty())
        {
            queue.remove();
        }
    }


    public void notify(Event event)
    {
        Code newCode = eventMappings.translateEvent(event.getCode());
        if(newCode != Code.UNKNOWN)
        {
            queue.add(event.copyAndChange(newCode, timer.getTime()));
        }
    }


    public void setEventMappings(EventMappings mappings)
    {
        this.eventMappings = mappings;
    }

}
