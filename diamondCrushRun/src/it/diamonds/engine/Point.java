package it.diamonds.engine;


public final class Point
{
    private float x;

    private float y;


    public Point(float x, float y)
    {
        this.x = x;
        this.y = y;
    }


    public void setX(float x)
    {
        this.x = x;
    }


    public void setY(float y)
    {
        this.y = y;
    }


    public float getX()
    {
        return this.x;
    }


    public float getY()
    {
        return this.y;
    }


    public boolean equals(Point otherPoint)
    {
        return (x == otherPoint.x) && (y == otherPoint.y);
    }


    @Override
    public boolean equals(Object otherPoint)
    {
        return equals((Point)otherPoint);
    }
}
