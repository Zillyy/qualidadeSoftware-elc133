package it.diamonds.tests.engine;


import it.diamonds.engine.video.Sprite;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public abstract class AbstractLayerTestCase extends TestCase
{
    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    protected MockEngine engine;

    protected Sprite sprite;

    protected Sprite otherSprite;


    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work

    public void setUp()
    {
        engine = new MockEngine(800, 600);
        sprite = Sprite.createForTesting(engine);
        otherSprite = Sprite.createDifferentForTesting(engine);
    }


    protected void spriteOrderTestHelper()
    {
        assertEquals(1, engine.getImageDrawOrder(sprite.getTexture()));
        assertEquals(2, engine.getImageDrawOrder(otherSprite.getTexture()));
    }

}
