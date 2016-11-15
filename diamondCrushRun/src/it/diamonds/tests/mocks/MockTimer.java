package it.diamonds.tests.mocks;


import it.diamonds.engine.TimerInterface;


public final class MockTimer implements TimerInterface
{
    private long time;


    private MockTimer()
    {
        this.time = 0;
    }


    public static MockTimer create()
    {
        return new MockTimer();
    }


    public long getTime()
    {
        return time;
    }


    public void setTime(long time)
    {
        this.time = time;
    }


    public void advance(long timeOffset)
    {
        this.time += timeOffset;
    }

}
