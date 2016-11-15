package it.diamonds.engine.video;


import it.diamonds.engine.Rectangle;
import it.diamonds.grid.Cell;


public class TiledSprite extends Sprite
{
    private static final Rectangle TEXTURE_AREA = new Rectangle(0, 0,
        Cell.SIZE - 1, Cell.SIZE - 1);

    private Cell dimensions;


    public TiledSprite(float posX, float posY, Image texture, Cell dimensions)
    {
        super(posX, posY, TEXTURE_AREA, texture);
        this.dimensions = dimensions;
    }


    public void draw(AbstractEngine engine)
    {
        float posX = getPosition().getX();
        float posY = getPosition().getY();

        for(int row = 0; row < dimensions.getHeight(); row++)
        {
            for(int column = 0; column < dimensions.getWidth(); column++)
            {
                setOrigin(getLeftOrigin(column), getTopOrigin(row));

                setPosition(posX + column * Cell.SIZE, posY + row * Cell.SIZE);

                super.draw(engine);
            }
        }
        setPosition(posX, posY);
    }


    private int getTopOrigin(int row)
    {
        if(row == 0)
        {
            return 0;
        }

        if(row == dimensions.getHeight() - 1)
        {
            return Cell.SIZE * 2;
        }

        return Cell.SIZE;
    }


    private int getLeftOrigin(int column)
    {
        if(column == 0)
        {
            return 0;
        }

        if(column == dimensions.getWidth() - 1)
        {
            return Cell.SIZE * 2;
        }

        return Cell.SIZE;
    }


    @Override
    public void useBrighterImage()
    {
        setBrightOffset(Cell.SIZE * 3);
    }

}
