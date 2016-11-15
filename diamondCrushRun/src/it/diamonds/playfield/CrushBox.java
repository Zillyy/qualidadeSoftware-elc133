package it.diamonds.playfield;


import it.diamonds.engine.Config;
import it.diamonds.engine.Environment;
import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Image;
import it.diamonds.engine.video.Sprite;


// TODO: In questo momento la texture quando pulsa esce dallo schermo
// oltretutto anche la barra sotto si "gonfia". Chiedere a jocchan se
// vuole separare la barra dalle scritte in questo modo spostiamo le
// scritte piu a sinistra e risolviamo il problema.
public final class CrushBox extends Sprite
{
    private static String crushBoxPath = "gfx/common/crush/";

    private static Image crushBoxTexture[];

    private Point origin;

    private int speed;

    private int screenWidth;


    private CrushBox(Point origin, int speed, int screenWidth)
    {
        super(origin.getX(), origin.getY(), new Rectangle(0, 0, 256, 64),
            getTexture(2));

        this.origin = origin;

        this.screenWidth = screenWidth;

        this.speed = speed * (int)Math.signum(origin.getX() - 320);

        hide();
    }


    public static CrushBox create(AbstractEngine engine, Point origin,
        int speed, int screenWidth)
    {
        if(crushBoxTexture == null)
        {
            crushBoxTexture = new Image[] {
                engine.createImage(getTextureName(2)),
                engine.createImage(getTextureName(3)),
                engine.createImage(getTextureName(4)),
                engine.createImage(getTextureName(5)),
                engine.createImage(getTextureName(6)),
                engine.createImage(getTextureName(7)),
                engine.createImage(getTextureName(8)),
                engine.createImage(getTextureName(9)),
                engine.createImage(getTextureName(100)) };
        }
        return new CrushBox(origin, speed, screenWidth);
    }


    public static CrushBox createForPlayerOne(Environment environment)
    {
        Config config = environment.getConfig();
        return create(environment.getEngine(), getOriginForPlayerOne(),
            config.getInteger("crushBoxSpeed"), config.getInteger("width"));
    }


    public static CrushBox createForPlayerTwo(Environment environment)
    {
        Config config = environment.getConfig();
        return create(environment.getEngine(), getOriginForPlayerTwo(),
            config.getInteger("crushBoxSpeed"), config.getInteger("width"));
    }


    private static String getTextureName(int comboNumber)
    {
        if(comboNumber > 9)
        {
            return crushBoxPath + "over";
        }
        else
        {
            return crushBoxPath + "0" + comboNumber;
        }
    }


    private static Image getTexture(int comboNumber)
    {
        if(comboNumber > 9)
        {
            return crushBoxTexture[8];
        }
        else
        {
            return crushBoxTexture[comboNumber - 2];
        }
    }


    public void setCrushCounter(int crushCounter)
    {
        setTexture(getTexture(crushCounter));
    }


    public boolean isOffScreen()
    {
        return (getPosition().getX() + getTextureArea().getWidth() < 0)
            || (getPosition().getX() >= screenWidth);
    }


    public void updatePosition()
    {
        setPosition(getPosition().getX() + speed, getPosition().getY());
    }


    public void show()
    {
        setPosition(origin.getX(), origin.getY());
        super.show();
    }


    public static Point getOriginForPlayerOne()
    {
        return new Point(-50, 192);
    }


    public static Point getOriginForPlayerTwo()
    {
        return new Point(594, 192);
    }

}
