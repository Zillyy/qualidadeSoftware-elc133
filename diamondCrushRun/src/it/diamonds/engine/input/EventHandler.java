package it.diamonds.engine.input;


public interface EventHandler
{
    void handleRepetition(InputReactor inputReactor);


    void handleEvent(InputReactor inputReactor, Event event);


    boolean isPressed();


    boolean isRepeated(long timeStamp);


    void setNormalRepeatDelay(long normalRepeatDelay);


    long getNormalRepeatDelay();


    void setFastRepeatDelay(long fastRepeatDelay);


    long getFastRepeatDelay();


    long getCurrentRepeatDelay();
}
