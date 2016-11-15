package it.diamonds.playfield;


import it.diamonds.droppable.DroppableGenerator;
import it.diamonds.engine.Point;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.DrawableInterface;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.Cell;


public class NextGemsPanel implements DrawableInterface
{

    private DroppableGenerator gemQueue;

    private Point origin;


    public NextGemsPanel(DroppableGenerator gemQueue, Point origin)
    {
        this.gemQueue = gemQueue;
        this.origin = origin;
    }


    public void draw(AbstractEngine engine)
    {
        drawUpperGem(engine);
        drawLowerGem(engine);
    }


    private void drawUpperGem(AbstractEngine engine)
    {
        Sprite sprite = gemQueue.getGemAt(1).getSprite();
        sprite.setPosition(origin.getX(), origin.getY());
        sprite.draw(engine);
    }


    private void drawLowerGem(AbstractEngine engine)
    {
        Sprite sprite = gemQueue.getGemAt(0).getSprite();
        sprite.setPosition(origin.getX(), origin.getY() + Cell.SIZE);
        sprite.draw(engine);
    }

}
