package it.diamonds.engine;


public final class Timer implements TimerInterface
{

    private long timeStamp;


    private Timer()
    {
        timeStamp = System.nanoTime();
    }


    public static Timer create()
    {
        return new Timer();
    }


    public long getTime()
    {
        return (System.nanoTime() - timeStamp) / 1000000;
    }


    public void advance(long timeOffset)
    {
        try
        {
            Thread.sleep(timeOffset);
        }
        catch(InterruptedException e)
        {
            ;
        }
    }

}
