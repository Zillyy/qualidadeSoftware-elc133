package it.diamonds.engine.video;


import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;

import java.util.ArrayList;


public final class Number implements DrawableInterface
{
    public static final int DIGIT_WIDTH = 16;

    public static final int DIGIT_HEIGHT = 24;

    public static final int DIGIT_OVERLAY = 5;

    private ArrayList<Sprite> digits;

    private int value;

    private Point origin;


    private Number(AbstractEngine engine, String textureName, Point origin)
    {
        createDigits(engine.createImage(textureName));
        this.origin = origin;
    }


    private void createDigits(Image texture)
    {
        digits = new ArrayList<Sprite>();

        for(int i = 0; i < 10; i++)
        {
            int x = (i % 5) * DIGIT_WIDTH;
            int y = (i / 5) * DIGIT_HEIGHT;
            digits.add(new Sprite(0, 0, new Rectangle(x, y,
                x + DIGIT_WIDTH - 1, y + DIGIT_HEIGHT - 1), texture));
        }
    }


    public static Number create16x24(AbstractEngine engine, float x, float y)
    {
        return new Number(engine, "gfx/common/score_16x24", new Point(x, y));
    }


    public Sprite getDigitSprite(int index)
    {
        return digits.get(index);
    }


    public int getValue()
    {
        return value;
    }


    public void setValue(int value)
    {
        this.value = value;
    }


    private void drawDigit(AbstractEngine engine, int digit, float x, float y)
    {
        Sprite digitSprite = digits.get(digit);

        digitSprite.setPosition(x, y);
        digitSprite.draw(engine);
    }


    private int extractDigit(int position)
    {
        int pow = (int)Math.pow(10, position);
        return (value % (pow * 10)) / pow;
    }


    public void draw(AbstractEngine engine)
    {
        boolean processingLeadingZeroes = true;

        for(int position = 0; position < 8; ++position)
        {
            int curDigit = extractDigit(7 - position);

            if(0 == curDigit && processingLeadingZeroes && position < 7)
            {
                continue;
            }

            processingLeadingZeroes = false;

            float x = origin.getX() + (DIGIT_WIDTH - DIGIT_OVERLAY) * position;
            float y = origin.getY();

            drawDigit(engine, curDigit, x, y);
        }
    }

}
