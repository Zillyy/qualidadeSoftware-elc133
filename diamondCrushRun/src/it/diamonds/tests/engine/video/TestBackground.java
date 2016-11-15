package it.diamonds.tests.engine.video;


import it.diamonds.engine.Environment;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Background;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestBackground extends TestCase
{
    private AbstractEngine engine;

    private Background background;


    public void setUp()
    {
        Environment environment = Environment.createForTesting(800, 600, "");
        engine = environment.getEngine();
        background = new Background(environment, "back000", ".jpg");
    }


    public void testBackgroundPosition()
    {
        assertEquals("X origin must be 0", 0F, background.getX());
        assertEquals("Y origin must be 0", 0F, background.getY());
    }


    public void testBackgroundWidthAndHeight()
    {
        assertEquals("Width must be equal to the width of the window", 1024,
            background.getWidth());

        assertEquals("Height must be equal to the height of the window", 1024,
            background.getHeight());

    }


    public void testBackgroundSpriteNotNull()
    {
        assertNotNull(background.getSprite());
    }


    public void testDraw()
    {
        background.draw(engine);
        assertEquals(1, ((MockEngine)engine).getNumberOfQuadsDrawn());
    }
}
