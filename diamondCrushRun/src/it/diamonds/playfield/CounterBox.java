package it.diamonds.playfield;


import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Sprite;


public final class CounterBox extends Sprite
{
    private static String texturePath = "gfx/layout/counter";


    private CounterBox(AbstractEngine engine, float x, float y)
    {
        super(x, y, new Rectangle(0, 0, 171, 58),
            engine.createImage(texturePath));
        hide();
    }


    public static CounterBox createForPlayerOne(AbstractEngine engine)
    {
        return new CounterBox(engine, 120, 492);
    }


    public static CounterBox createForPlayerTwo(AbstractEngine engine)
    {
        return new CounterBox(engine, 521, 492);
    }

}
