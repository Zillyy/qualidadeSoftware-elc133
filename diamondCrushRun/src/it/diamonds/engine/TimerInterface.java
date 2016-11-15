package it.diamonds.engine;


public interface TimerInterface
{

    long getTime();


    void advance(long timeOffset);

}
