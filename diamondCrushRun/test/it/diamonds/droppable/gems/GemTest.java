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
    
    @Before
    public void setUp() {
        engine = new MockEngine(800, 600);
        gem = Gem.createForTesting(engine);
    }
    
    /**
     * Testa se a gema é desenhada corretamente.
     */
    @Test
    public void testNormalGemIsDrawnCorrectly() {
        System.out.println("* GemTest: - testNormalGemIsDrawnCorrectly()");
        
        gem.getSprite().useNormalImage();

        gem.getSprite().draw(engine);
        
        assertEquals("Gema deve ser desenhada com textura sem brilho (bad left)",
            engine.getImageRect().left(), 0);

        assertEquals("Gema deve ser desenhada com textura sem brilho (bad right)",
            engine.getImageRect().right(), 31);
    }

    /**
     * Testa o view size da gema.
     */
    @Test
    public void testGemViewSize() {
        System.out.println("* GemTest: - testGemViewSize()");
        
        gem.getSprite().draw(engine);
        
        assertEquals(
            "Altura da textura da engine diferente da altura da gema (inicial)",
            engine.getImageRect().getHeight(),
            (int)gem.getSprite().getTextureArea().getHeight());
        assertEquals(
            "Largura da textura da engine diferente da largura da gema (inicial)",
            engine.getImageRect().getWidth(),
            (int)gem.getSprite().getTextureArea().getWidth());
    }
    
}
