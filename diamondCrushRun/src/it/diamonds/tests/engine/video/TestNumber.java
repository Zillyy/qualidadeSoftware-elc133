package it.diamonds.tests.engine.video;


import static it.diamonds.engine.video.Number.DIGIT_HEIGHT;
import static it.diamonds.engine.video.Number.DIGIT_OVERLAY;
import static it.diamonds.engine.video.Number.DIGIT_WIDTH;
import it.diamonds.engine.video.Number;
import it.diamonds.tests.engine.AbstractEnvironmentTestCase;
import it.diamonds.tests.mocks.MockEngine;

import java.io.IOException;


public class TestNumber extends AbstractEnvironmentTestCase
{
    private MockEngine engine;

    private Number number;


    public void setUp() throws IOException
    {
        super.setUp();
        engine = (MockEngine)environment.getEngine();
        number = Number.create16x24(engine, 0.0f, 0.0f);
    }


    public void testOneValue()
    {
        number.setValue(1234);
        assertEquals(1234, number.getValue());
    }


    public void testAnotherValue()
    {
        number.setValue(4321);
        assertEquals(4321, number.getValue());
    }


    public void testDrawOneDigit()
    {
        number.setValue(8);
        number.draw(engine);
        assertEquals(1, engine.getNumberOfQuadsDrawn());
    }


    public void testDrawTwoDigits()
    {
        number.setValue(56);
        number.draw(engine);
        assertEquals(2, engine.getNumberOfQuadsDrawn());
    }


    public void testTrailingZeroes()
    {
        number.setValue(12300);
        number.draw(engine);
        assertEquals(5, engine.getNumberOfQuadsDrawn());
    }


    public void testMiddleZeroes()
    {
        number.setValue(71104);
        number.draw(engine);
        assertEquals(5, engine.getNumberOfQuadsDrawn());
    }


    public void testZero()
    {
        number.setValue(0);
        number.draw(engine);
        assertEquals(1, engine.getNumberOfQuadsDrawn());
    }


    public void testSpriteSize()
    {
        number.setValue(3);
        number.draw(engine);
        assertEquals((float)DIGIT_WIDTH, engine.getQuadWidth());
        assertEquals((float)DIGIT_HEIGHT, engine.getQuadHeight());
    }


    public void testSpriteTopCoord()
    {
        number.setValue(2);
        number.draw(engine);
        assertEquals(0, (int)engine.getQuadPosition().getY());
    }


    public void testFirstSpriteLeftCoord()
    {
        number.setValue(2);
        number.draw(engine);
        assertEquals((DIGIT_WIDTH - DIGIT_OVERLAY) * 7,
            (int)engine.getQuadPosition().getX());
    }


    public void testSecondSpriteLeftCoord()
    {
        number.setValue(20);
        number.draw(engine);
        assertEquals((DIGIT_WIDTH - DIGIT_OVERLAY) * 6,
            (int)engine.getQuadPosition(1).getX());
    }


    public void testSpriteTopCoordWithOrigin()
    {
        number = Number.create16x24(engine, 64, 35);
        number.setValue(9);
        number.draw(engine);
        assertEquals(35, (int)engine.getQuadPosition().getY());
    }


    public void testFirstSpriteLeftCoordWithOrigin()
    {
        number = Number.create16x24(engine, 64, 35);
        number.setValue(9);
        number.draw(engine);
        assertEquals(64 + (DIGIT_WIDTH - DIGIT_OVERLAY) * 7,
            (int)engine.getQuadPosition().getX());
    }


    public void testSecondSpriteLeftCoordWithOrigin()
    {
        number = Number.create16x24(engine, 64, 35);
        number.setValue(90);
        number.draw(engine);
        assertEquals(64 + (DIGIT_WIDTH - DIGIT_OVERLAY) * 6,
            (int)engine.getQuadPosition(1).getX());
    }

}
