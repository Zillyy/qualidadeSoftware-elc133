package it.diamonds.droppable.gems;

import it.diamonds.droppable.Droppable;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockTimer;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Zilly
 */
public class GemTest {
    
    private MockEngine engine;
    private Droppable gem;
    
    public GemTest() {
    }
    
    private Droppable createGem() {
        return Gem.createForTesting(new MockEngine(800, 600));
    }
    
    @Before
    public void setUp() {
        engine = new MockEngine(800, 600);
        gem = createGem();
    }
    
    @Test
    public void testGemIsDrawnCorrectly() {
        gem.getSprite().useNormalImage();

        gem.getSprite().draw(engine);

        assertEquals("Gem must be drawn with unbrightened texture(bad left)",
            engine.getImageRect().left(), 0);

        assertEquals("Gem must be drawn with unbrightened texture(bad right)",
            engine.getImageRect().right(), 31);

        gem.getSprite().useBrighterImage();

        gem.getAnimatedObject().update(MockTimer.create().getTime());
        gem.getSprite().draw(engine);

        assertEquals("Gem must be drawn with brightened texture(bad left)",
            engine.getImageRect().left(), 32);

        assertEquals("Gem must be drawn with brightened texture(bad right)",
            engine.getImageRect().right(), 63);
    }

    @Test
    public void testGemViewSize() {
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
    
}
