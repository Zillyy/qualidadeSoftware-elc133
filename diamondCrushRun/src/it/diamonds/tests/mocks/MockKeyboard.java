package it.diamonds.tests.mocks;


import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.InputDeviceInterface;
import it.diamonds.engine.input.InputListenerInterface;

import java.util.LinkedList;


public final class MockKeyboard implements InputDeviceInterface
{
    private LinkedList<InputListenerInterface> listeners;

    private boolean created = false;

    private boolean updated = false;


    private MockKeyboard()
    {
        listeners = new LinkedList<InputListenerInterface>();
        created = true;
    }


    public static MockKeyboard create()
    {
        return new MockKeyboard();
    }


    public boolean isCreated()
    {
        return created;
    }


    public void shutDown()
    {
        created = false;
    }


    public void update()
    {
        updated = true;
    }


    public boolean updated()
    {
        return updated;
    }


    public void addListener(InputListenerInterface listener)
    {
        // I HATE CHECKSTYLE!!!
        listeners.add(listener);
    }


    public void notify(Event event)
    {
        // I HATE CHECKSTYLE!!!
        for(InputListenerInterface listener : listeners)
        {
            listener.notify(event);
        }
    }

}
