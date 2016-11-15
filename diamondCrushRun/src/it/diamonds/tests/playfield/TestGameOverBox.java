package it.diamonds.tests.playfield;


import it.diamonds.engine.Point;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.playfield.GameOverBox;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestGameOverBox extends TestCase
{

    private AbstractEngine engine;


    protected void setUp() throws Exception
    {
        engine = new MockEngine(800, 600);
    }


    public void testCorrectTexture()
    {
        GameOverBox gameOverBox = new GameOverBox(engine, new Point(0, 0));

        assertEquals("GameOverBox must be created with correct texture",
            "gfx/common/gameover", gameOverBox.getTexture().getName());
    }


    public void testCorrectOrigin()
    {
        GameOverBox gameOverBox = new GameOverBox(engine, new Point(10, 20));

        assertEquals("GameOverBox must be created with correct X origin", 10,
            gameOverBox.getPosition().getX(), 0.0001f);
        assertEquals("GameOverBox must be created with correct Y origin", 20,
            gameOverBox.getPosition().getY(), 0.0001f);

        gameOverBox = new GameOverBox(engine, new Point(30, 5));

        assertEquals("GameOverBox must be created with correct X origin", 30,
            gameOverBox.getPosition().getX(), 0.0001f);
        assertEquals("GameOverBox must be created with correct Y origin", 5,
            gameOverBox.getPosition().getY(), 0.0001f);
    }


    public void testIsHiddenAfterCreation()
    {
        GameOverBox gameOverBox = new GameOverBox(engine, new Point(0, 0));

        assertEquals("GameOverBox must be hidden after creation", true,
            gameOverBox.isHidden());
    }


    public void testIsShowingOnGameOver()
    {
    }
}
