package it.diamonds.tests.engine;


import it.diamonds.engine.Environment;

import java.io.IOException;

import junit.framework.TestCase;


public abstract class AbstractEnvironmentTestCase extends TestCase
{
    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    protected Environment environment;


    public void setUp() throws IOException
    {
        environment = Environment.createForTesting(800, 600, "");
    }

    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work
}
