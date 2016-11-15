package it.diamonds.tests.engine;


import it.diamonds.engine.Environment;

import junit.framework.TestCase;


public class TestEnvironment extends TestCase
{
    private Environment environment;


    protected void setUp()
    {
        environment = Environment.createForTesting(800, 600, "");
    }


    public void testConfigCreated()
    {
        assertNotNull(environment.getConfig());
    }


    public void testAudioCreated()
    {
        assertNotNull(environment.getAudio());
    }


    public void testEngineCreated()
    {
        assertNotNull(environment.getEngine());
    }


    public void testKeyboardCreated()
    {
        assertNotNull(environment.getKeyboard());
    }


    public void testTimerCreated()
    {
        assertNotNull(environment.getTimer());
    }
}
