package it.diamonds.engine.video;


public class Frame
{
    private int duration;

    private int x;

    private int y;


    public Frame(int x, int y, int duration)
    {
        this.x = x;
        this.y = y;
        this.duration = duration;
    }


    public int getLength()
    {
        return duration;
    }


    public int getX()
    {
        return x;
    }


    public int getY()
    {
        return y;
    }
}
