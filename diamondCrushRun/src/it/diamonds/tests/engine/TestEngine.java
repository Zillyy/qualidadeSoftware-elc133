package it.diamonds.tests.engine;


import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Image;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestEngine extends TestCase
{
    private AbstractEngine engine;


    public void setUp()
    {
        engine = new MockEngine(800, 600);
    }


    /*
     * FIXME setting the MockEngine's shutDown flag value as required by this test
     * causes JUnit to run out of heap space and go to a grinding halt.
     * Fix this and reenable this testcase ASAP
     */
    public void testCreated()
    {
        assertEquals(800, engine.getDisplayWidth());
        assertEquals(600, engine.getDisplayHeight());
        // assertFalse(engine.isWindowClosed());
    }


    public void testShuttedDown()
    {
        engine.shutDown();
        assertTrue(engine.isWindowClosed());
    }


    public void testCreateTexture()
    {
        Image texture = engine.createImage("diamond");
        assertEquals("diamond", texture.getName());
    }


    public void testCreateSameTextureTwice()
    {
        Image texture = engine.createImage("diamond");
        Image sameTexture = engine.createImage("diamond");
        assertSame(sameTexture, texture);
    }


    public void testCreateTwoDifferentTextures()
    {
        Image texture = engine.createImage("diamond");
        Image differentTexture = engine.createImage("gfx/droppables/gems/ruby");
        assertNotSame(differentTexture, texture);
    }

}
