package it.diamonds.droppable.gems;


import it.diamonds.droppable.AbstractSingleDroppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.engine.video.AbstractEngine;


public final class Stone extends AbstractSingleDroppable
{
    public static final int ANIMATION_FRAME_DELAY = 100;

    private static final int FIRST_FRAME_DELAY = 0;

    private long lastUpdateTimeStamp;

    private boolean timeStampIsValid = false;


    private Stone(AbstractEngine engine, DroppableColor color)
    {
        super(engine, DroppableType.STONE, color, FIRST_FRAME_DELAY);
        setNumberOfFramesInTexture(8);
    }


    public static Stone create(AbstractEngine engine, DroppableColor color)
    {
        return new Stone(engine, color);
    }


    public void update(long timer)
    {
        if(getCurrentFrame() < 5)
        {
            return;
        }

        if(!timeStampIsValid)
        {
            lastUpdateTimeStamp = timer;
            timeStampIsValid = true;
            return;
        }

        if(timer < lastUpdateTimeStamp + ANIMATION_FRAME_DELAY)
        {
            return;
        }
        lastUpdateTimeStamp = timer;

        if(getCurrentFrame() < 7)
        {
            setCurrentFrame(getCurrentFrame() + 1);
        }
    }

}
