package it.diamonds.engine.audio;


public interface Sound
{

    Object getName();


    void play();


    boolean wasPlayed();


    void reset();


    void freeMemory();

}
