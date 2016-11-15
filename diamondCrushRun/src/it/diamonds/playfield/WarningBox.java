package it.diamonds.playfield;


import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Number;
import it.diamonds.engine.video.Sprite;


public final class WarningBox extends Sprite
{
    private static String texturePath = "gfx/layout/warning";

    private Number incomingStone;


    private WarningBox(AbstractEngine engine, float x, float y)
    {
        super(x, y, new Rectangle(0, 0, 171, 58),
            engine.createImage(texturePath));
        incomingStone = Number.create16x24(engine, getCrushNumberX(),
            getCrushNumberY());

        hide();
    }


    public static WarningBox createForPlayerOne(AbstractEngine engine)
    {
        return new WarningBox(engine, 120, 492);
    }


    public static WarningBox createForPlayerTwo(AbstractEngine engine)
    {
        return new WarningBox(engine, 521, 492);
    }


    public void draw(AbstractEngine engine)
    {
        drawWarningBoxBackground(engine);
        drawIcomingStone(engine);
    }


    private void drawWarningBoxBackground(AbstractEngine engine)
    {
        super.draw(engine);
    }


    private void drawIcomingStone(AbstractEngine engine)
    {
        if(!isHidden())
        {
            incomingStone.draw(engine);
        }
    }


    public int getCounter()
    {
        return incomingStone.getValue();
    }


    public void setCounter(int gemsCounter)
    {
        incomingStone.setValue(gemsCounter);
    }


    public int getCrushNumberX()
    {
        return (int)getPosition().getX() - 54;
    }


    public int getCrushNumberY()
    {
        return (int)getPosition().getY() + 24;
    }
}
