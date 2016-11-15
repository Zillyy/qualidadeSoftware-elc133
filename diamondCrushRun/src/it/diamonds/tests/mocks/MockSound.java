package it.diamonds.tests.mocks;


import it.diamonds.engine.audio.Sound;


public final class MockSound implements Sound
{
    private String name;

    private boolean wasPlayed;


    private MockSound(String fileName)
    {
        this.name = fileName;
    }


    public static Sound create(String fileName)
    {
        return new MockSound(fileName);
    }


    public Object getName()
    {
        return name;
    }


    public void play()
    {
        wasPlayed = true;
    }


    // I HATE CHECKSTYLE
    public boolean wasPlayed()
    {
        return wasPlayed;
    }


    public void reset()
    {
        wasPlayed = false;
    }


    // I HATE CHECKSTYLE
    public void freeMemory()
    {
        wasPlayed = false;
    }

}
