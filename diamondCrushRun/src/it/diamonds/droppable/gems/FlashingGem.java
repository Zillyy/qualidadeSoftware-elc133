package it.diamonds.droppable.gems;


import static it.diamonds.droppable.DroppableColor.NO_COLOR;
import static it.diamonds.droppable.DroppableType.FLASHING_GEM;
import it.diamonds.droppable.AbstractSingleDroppable;
import it.diamonds.engine.video.AbstractEngine;


public final class FlashingGem extends AbstractSingleDroppable
{
    private static final int FIRST_FRAME_DELAY = 0;


    private FlashingGem(AbstractEngine engine, int animationDelay)
    {
        super(engine, FLASHING_GEM, NO_COLOR, animationDelay);
        setNumberOfFramesInTexture(8);
    }


    public static FlashingGem create(AbstractEngine engine)
    {
        return new FlashingGem(engine, FIRST_FRAME_DELAY);
    }


    public static FlashingGem create(AbstractEngine engine, int animationDelay)
    {
        return new FlashingGem(engine, animationDelay);
    }
}
