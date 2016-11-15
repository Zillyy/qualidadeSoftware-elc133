package it.diamonds.droppable;


import it.diamonds.droppable.gems.Chest;
import it.diamonds.droppable.gems.FlashingGem;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.gems.Stone;
import it.diamonds.engine.Environment;
import it.diamonds.engine.audio.Sound;
import it.diamonds.engine.video.AbstractEngine;


public class DroppableFactory
{
    private AbstractEngine engine;

    private Sound sound;

    private int gemAnimationUpdateRate;

    private int gemAnimationDelay;


    public DroppableFactory(Environment environment)
    {
        this.engine = environment.getEngine();
        sound = environment.getAudio().createSound("diamond");
        gemAnimationUpdateRate = environment.getConfig().getInteger(
            "GemAnimationUpdateRate");
        gemAnimationDelay = environment.getConfig().getInteger(
            "GemAnimationDelay");
    }


    public static DroppableFactory createForTesting(Environment environment)
    {
        return new DroppableFactory(environment);
    }


    private Droppable setupGemAnimationAndSound(Droppable newGem)
    {
        newGem.getObjectWithCollisionSound().setCollisionSound(sound);
        newGem.getAnimatedObject().createAnimationSequence(
            gemAnimationUpdateRate);

        return newGem;
    }


    public Droppable create(DroppableType type, DroppableColor color)
    {
        Droppable newDroppable = null;

        if(type.isChest())
        {
            newDroppable = Chest.create(engine, color, gemAnimationDelay);
        }
        else if(type.isFlashingGem())
        {
            newDroppable = FlashingGem.create(engine);
        }
        else if(type.isGem())
        {
            newDroppable = Gem.create(engine, color, gemAnimationDelay);
        }
        else if(type.isStone())
        {
            newDroppable = Stone.create(engine, color);
        }

        return setupGemAnimationAndSound(newDroppable);
    }


    public Droppable createFlashingGem()
    {
        return create(DroppableType.FLASHING_GEM, null);
    }


    public Droppable createStone(DroppableColor color)
    {
        return create(DroppableType.STONE, color);
    }

}
