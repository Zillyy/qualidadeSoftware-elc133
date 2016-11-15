package it.diamonds.engine.video;


import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.modifiers.DrawModifier;
import it.diamonds.grid.Cell;


public class Sprite implements DrawableInterface
{
    private int brightOffset = 0;

    private Image texture;

    private Point position = new Point(0.0f, 0.0f);

    private Rectangle textureArea;

    private boolean hidden = false;

    private DrawModifier drawModifier;


    public Sprite(float posX, float posY, Rectangle textureArea, Image texture)
    {
        this.texture = texture;
        this.textureArea = textureArea;

        setPosition(posX, posY);
    }


    public Sprite(float posX, float posY, Image texture)
    {
        this(posX, posY, new Rectangle(0, 0, texture.getWidth() - 1,
            texture.getHeight() - 1), texture);
    }


    public static Sprite createForTesting(AbstractEngine engine)
    {
        return new Sprite(100, 200, engine.createImage("diamond"));
    }


    public static Sprite createDifferentForTesting(AbstractEngine engine)
    {
        return new Sprite(100, 200, engine.createImage("emerald"));
    }


    public void setPosition(float posX, float posY)
    {
        position.setX(posX);
        position.setY(posY);
    }


    public void draw(AbstractEngine engine)
    {
        if(hidden)
        {
            return;
        }

        if(drawModifier != null)
        {
            drawModifier.updateModifierState();
            drawModifier.draw(engine);
        }
        else
        {
            engine.drawImage(position, getTextureArea().getWidth(),
                getTextureArea().getHeight(), texture, textureArea);
        }
    }


    public Image getTexture()
    {
        return texture;
    }


    public Rectangle getTextureArea()
    {
        return textureArea;
    }


    public Point getPosition()
    {
        return position;
    }


    public void translate(float dx, float dy)
    {
        position.setX(position.getX() + dx);
        position.setY(position.getY() + dy);
    }


    public void setOrigin(int left, int top)
    {
        textureArea.translateTo(left + brightOffset, top);
    }


    public void hide()
    {
        hidden = true;
    }


    public void show()
    {
        hidden = false;
    }


    public boolean isHidden()
    {
        return hidden;
    }


    public void setTexture(Image texture)
    {
        this.texture = texture;
    }


    public void setDrawModifier(DrawModifier modifier)
    {
        drawModifier = modifier;
    }


    public DrawModifier getDrawModifier()
    {
        return drawModifier;
    }


    public void removeDrawModifier()
    {
        drawModifier = null;
    }


    public void useNormalImage()
    {
        brightOffset = 0;
    }


    // TODO: i brighter sprites non sono piu' usati
    public void useBrighterImage()
    {
        brightOffset = Cell.SIZE;
    }


    public boolean isBrighter()
    {
        return brightOffset != 0;
    }


    protected void setBrightOffset(int brightOffset)
    {
        this.brightOffset = brightOffset;
    }

}
