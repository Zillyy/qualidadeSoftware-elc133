package it.diamonds.droppable.pair;


public final class Direction
{
    public static final Direction GO_UP = new Direction(0, -1);

    public static final Direction GO_RIGHT = new Direction(1, 0);

    public static final Direction GO_DOWN = new Direction(0, 1);

    public static final Direction GO_LEFT = new Direction(-1, 0);

    private final int deltaX;

    private final int deltaY;


    private Direction(int deltaX, int deltaY)
    {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }


    public int deltaX()
    {
        return deltaX;
    }


    public int deltaY()
    {
        return deltaY;
    }
}
