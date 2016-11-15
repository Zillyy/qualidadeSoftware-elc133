package it.diamonds.droppable.pair;


import static it.diamonds.droppable.pair.Direction.GO_DOWN;
import static it.diamonds.droppable.pair.Direction.GO_LEFT;
import static it.diamonds.droppable.pair.Direction.GO_RIGHT;
import static it.diamonds.droppable.pair.Direction.GO_UP;
import static it.diamonds.droppable.pair.Position.BOTTOM;
import static it.diamonds.droppable.pair.Position.LEFT;
import static it.diamonds.droppable.pair.Position.RIGHT;
import static it.diamonds.droppable.pair.Position.TOP;

import java.util.HashMap;


public final class SlaveRotation
{
    public static final SlaveRotation MIRROR = mirror();

    public static final SlaveRotation CLOCKWISE = clockwise();

    public static final SlaveRotation COUNTERCLOCKWISE = counterclockwise();

    private HashMap<Position, Movement> transitionMap = new HashMap<Position, Movement>();


    private static SlaveRotation mirror()
    {
        SlaveRotation mirror = new SlaveRotation();

        mirror.addMovement(TOP, GO_DOWN, BOTTOM);
        mirror.addMovement(BOTTOM, GO_UP, TOP);
        mirror.addMovement(LEFT, GO_RIGHT, RIGHT);
        mirror.addMovement(RIGHT, GO_LEFT, LEFT);

        return mirror;
    }


    private static SlaveRotation clockwise()
    {
        SlaveRotation clockwise = new SlaveRotation();

        clockwise.addMovement(TOP, GO_RIGHT, RIGHT);
        clockwise.addMovement(RIGHT, GO_DOWN, BOTTOM);
        clockwise.addMovement(BOTTOM, GO_LEFT, LEFT);
        clockwise.addMovement(LEFT, GO_UP, TOP);

        return clockwise;
    }


    private static SlaveRotation counterclockwise()
    {
        SlaveRotation counterclockwise = new SlaveRotation();

        counterclockwise.addMovement(TOP, GO_LEFT, LEFT);
        counterclockwise.addMovement(RIGHT, GO_UP, TOP);
        counterclockwise.addMovement(BOTTOM, GO_RIGHT, RIGHT);
        counterclockwise.addMovement(LEFT, GO_DOWN, BOTTOM);

        return counterclockwise;
    }


    void addMovement(Position start, Direction direction, Position newPosition)
    {
        transitionMap.put(start, new Movement(direction, newPosition));
    }


    public Movement getMovement(Position start)
    {
        return transitionMap.get(start);
    }
}
