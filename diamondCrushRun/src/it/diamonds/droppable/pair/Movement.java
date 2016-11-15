package it.diamonds.droppable.pair;


public class Movement
{
    private Direction direction;

    private Position newPosition;


    public Movement(Direction direction, Position newPosition)
    {
        this.direction = direction;
        this.newPosition = newPosition;
    }


    public Direction direction()
    {
        return direction;
    }


    public Position newPosition()
    {
        return newPosition;
    }
}
