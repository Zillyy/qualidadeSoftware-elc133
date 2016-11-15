package it.diamonds.tests.droppable;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockTimer;
import junit.framework.TestCase;


public class TestGem extends TestCase
{

    private Droppable createGem()
    {
        return Gem.createForTesting(new MockEngine(800, 600));
    }


    public void testGemIsDrawnCorrectly()
    {
        MockEngine engine = new MockEngine(800, 600);

        Droppable gem = createGem();

        gem.getSprite().useNormalImage();

        gem.getSprite().draw(engine);

        assertEquals("gem must be drawn with unbrightened texture(bad left)",
            engine.getImageRect().left(), 0);

        assertEquals("gem must be drawn with unbrightened texture(bad right)",
            engine.getImageRect().right(), 31);

        gem.getSprite().useBrighterImage();

        gem.getAnimatedObject().update(MockTimer.create().getTime());
        gem.getSprite().draw(engine);

        assertEquals("gem must be drawn with brightened texture(bad left)",
            engine.getImageRect().left(), 32);

        assertEquals("gem must be drawn with brightened texture(bad right)",
            engine.getImageRect().right(), 63);
    }


    public void testGemViewSize()
    {
        MockEngine engine = new MockEngine(800, 600);

        Droppable gem = createGem();
        gem.getSprite().draw(engine);
        assertEquals(
            "Height of the texture engine differente of height of gem(init)",
            engine.getImageRect().getHeight(),
            (int)gem.getSprite().getTextureArea().getHeight());
        assertEquals(
            "Width of the texture engine differente of width of gem(init)",
            engine.getImageRect().getWidth(),
            (int)gem.getSprite().getTextureArea().getWidth());
    }


    public void testSetAndGetCurrentFrame()
    {
        Droppable gem = createGem();
        gem.getAnimatedObject().createAnimationSequence(0);

        gem.getAnimatedObject().setCurrentFrame(1);
        assertEquals(1, gem.getAnimatedObject().getCurrentFrame());

        gem.getAnimatedObject().setCurrentFrame(2);
        assertEquals(2, gem.getAnimatedObject().getCurrentFrame());
    }
}
