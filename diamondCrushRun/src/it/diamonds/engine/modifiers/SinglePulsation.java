package it.diamonds.engine.modifiers;


import it.diamonds.engine.video.Sprite;


public class SinglePulsation extends Pulsation
{
    public SinglePulsation(Sprite sprite, int pulsationLength,
        float sizeMultiplier)
    {
        super(sprite, pulsationLength, sizeMultiplier);
    }


    public boolean ended()
    {
        return (getCurrentAngle() / Math.PI) > 1;
    }
}
