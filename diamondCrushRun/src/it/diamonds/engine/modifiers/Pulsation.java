package it.diamonds.engine.modifiers;


import it.diamonds.engine.Point;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Sprite;


public class Pulsation implements DrawModifier
{
    private Sprite sprite;

    private float currentAngle;

    private float angleStep;

    private int pulsationLength;

    private float sizeMultiplier;


    public Pulsation(Sprite sprite, int pulsationLength, float sizeMultiplier)
    {
        this.sprite = sprite;
        this.pulsationLength = pulsationLength;
        this.sizeMultiplier = sizeMultiplier;

        angleStep = (float)Math.PI / pulsationLength;
    }


    public void updateModifierState()
    {
        currentAngle += angleStep;
    }


    public void draw(AbstractEngine engine)
    {
        float delta = calculateDelta();
        Point position = getDrawingPosition(delta);

        engine.drawImage(position, sprite.getTextureArea().getWidth() + delta,
            sprite.getTextureArea().getHeight() + delta, sprite.getTexture(),
            sprite.getTextureArea());
    }


    public boolean ended()
    {
        return false;
    }


    private float calculateDelta()
    {
        return (float)Math.abs(Math.sin(currentAngle) * sizeMultiplier);
    }


    private Point getDrawingPosition(float delta)
    {
        return new Point(sprite.getPosition().getX() - (delta / 2),
            sprite.getPosition().getY() - (delta / 2));
    }


    public Sprite getSprite()
    {
        return sprite;
    }


    public float getCurrentAngle()
    {
        return currentAngle;
    }


    public float getAngleStep()
    {
        return angleStep;
    }


    public int getPulsationLength()
    {
        return pulsationLength;
    }


    public float getSizeMultiplier()
    {
        return sizeMultiplier;
    }
}
