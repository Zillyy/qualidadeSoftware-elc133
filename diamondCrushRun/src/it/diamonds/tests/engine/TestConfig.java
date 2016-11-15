package it.diamonds.tests.engine;


import it.diamonds.engine.Config;
import junit.framework.TestCase;


public class TestConfig extends TestCase
{
    public void testConfigFileNotFound()
    {
        try
        {
            @SuppressWarnings("unused")
            Config config = new Config("data/BooYa", "data/KeysConfig");
        }
        catch(RuntimeException e)
        {
            return;
        }
        fail("exception not raised");
    }


    public void testGetInteger()
    {
        Config config = new Config("data/TestGameConfig", "data/KeysConfig");

        assertEquals(314, config.getInteger("TestInteger"));
    }


    public void testWrongPropertyName()
    {
        Config config = Config.create();

        try
        {
            assertEquals(null,
                config.getInteger("obviously-wrong@property.name"));
        }
        catch(RuntimeException e)
        {
            return;
        }
        fail("exception not raised");
    }


    public void testKeysConfigFileNotFound()
    {
        try
        {
            @SuppressWarnings("unused")
            Config config = new Config("data/GameConfig", "data/BooYa");
        }
        catch(RuntimeException e)
        {
            return;
        }
        fail("exception not raised");
    }


    public void testGetKey()
    {
        Config config = new Config("data/GameConfig", "data/TestKeysConfig");

        assertEquals("KEY_UP", config.getKey("P1.UP"));
    }


    public void testWrongKeyName()
    {
        Config config = Config.create();

        assertEquals(null, config.getKey("obviously-wrong@property.name"));
    }

}
