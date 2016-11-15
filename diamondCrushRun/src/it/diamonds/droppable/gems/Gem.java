package it.diamonds.droppable.gems;


import it.diamonds.droppable.AbstractSingleDroppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.engine.video.AbstractEngine;


public final class Gem extends AbstractSingleDroppable
{
    private static final int FIRST_FRAME_DELAY = 3500;


    private Gem(AbstractEngine engine, DroppableColor color, int animationDelay)
    {
        super(engine, DroppableType.GEM, color, animationDelay);
    }


    public static Gem create(AbstractEngine engine, DroppableColor color,
        int animationDelay)
    {
        return new Gem(engine, color, animationDelay);
    }


    public static Gem createForTesting(AbstractEngine engine)
    {
        return Gem.create(engine, DroppableColor.DIAMOND, FIRST_FRAME_DELAY);
    }


    public int getScore()
    {
        return getColor().getScore();
    }
}
