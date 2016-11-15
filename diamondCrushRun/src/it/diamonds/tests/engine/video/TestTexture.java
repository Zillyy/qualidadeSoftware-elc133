package it.diamonds.tests.engine.video;


import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.DisplayException;
import it.diamonds.engine.video.Image;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestTexture extends TestCase
{

    private AbstractEngine engine;


    protected void setUp()
    {
        engine = new MockEngine(800, 600);
    }


    public void testLoadDiamondTexture()
    {
        Image texture = engine.createImage("diamond");

        assertTrue("texture has not been loaded", texture.isLoaded());
    }


    public void testSize()
    {
        Image texture = engine.createImage("diamond");

        assertEquals("texture width is wrong", 64, texture.getWidth());
        assertEquals("texture height is wrong", 64, texture.getHeight());
    }


    public void testLoadFailed()
    {
        try
        {
            engine.createImage("this_texture_doesnt_exist");
        }
        catch(RuntimeException e)
        {
            return;
        }

        fail("TextureNotFoundException not thrown");
    }


    public void testMultipleTextureCreation()
    {
        Image texture = engine.createImage("diamond");
        Image texture2 = engine.createImage("grid-background");
        Image texture3 = engine.createImage("diamond");
        Image texture4 = engine.createImage("grid-background");

        assertSame("textures must be the same", texture, texture3);
        assertSame("textures must be the same", texture2, texture4);
    }


    public void testCleanUpTexture()
    {
        Image texture = engine.createImage("diamond");
        texture.cleanup();

        assertFalse("texture has not been cleaned", texture.isLoaded());
    }


    public void testLoadNotTwoMultipleTexture()
    {
        try
        {
            @SuppressWarnings("unused")
            Image texture = engine.createImage("textureTest");
        }
        catch(RuntimeException t)
        {
            return;
        }
        fail();
    }


    public void testDisplayExceptionWithMessage()
    {
        try
        {
            throw new DisplayException("con messaggio");
        }
        catch(DisplayException e)
        {
            return;
        }
    }
}
