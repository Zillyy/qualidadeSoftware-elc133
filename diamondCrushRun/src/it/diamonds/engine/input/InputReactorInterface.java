package it.diamonds.engine.input;


import it.diamonds.engine.input.Event.Code;


public interface InputReactorInterface
{
    void addHandler(Code code, AbstractEventHandler handler);


    EventHandler getEventHandler(Code code);


    long getNormalRepeatDelay();


    long getFastRepeatDelay();


    long getLastInputTimeStamp();


    void reactToInput(long timer);


    void emptyQueue();

}
