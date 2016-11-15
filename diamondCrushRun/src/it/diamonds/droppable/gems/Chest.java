package it.diamonds.droppable.gems;


import it.diamonds.droppable.AbstractSingleDroppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.engine.video.AbstractEngine;


public final class Chest extends AbstractSingleDroppable
{
    private Chest(AbstractEngine engine, DroppableColor color,
        int animationDelay)
    {
        super(engine, DroppableType.CHEST, color, animationDelay);
    }


    public static Chest create(AbstractEngine engine, DroppableColor color,
        int animationDelay)
    {
        return new Chest(engine, color, animationDelay);
    }
}
