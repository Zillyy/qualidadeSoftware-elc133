package it.diamonds.playfield;


import it.diamonds.engine.Point;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Sprite;


public class GameOverBox extends Sprite
{

    private static String texturePath = "gfx/common/gameover";


    public GameOverBox(AbstractEngine engine, Point origin)
    {
        super(origin.getX(), origin.getY(), engine.createImage(texturePath));

        hide();
    }

}
