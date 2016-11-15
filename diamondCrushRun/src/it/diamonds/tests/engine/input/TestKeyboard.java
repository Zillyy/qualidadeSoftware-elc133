package it.diamonds.tests.engine.input;


import it.diamonds.tests.mocks.MockKeyboard;
import junit.framework.TestCase;


public class TestKeyboard extends TestCase
{

    private MockKeyboard keyboard;


    public void setUp()
    {
        keyboard = MockKeyboard.create();
    }


    /*
     * TODO: questo test e' nel case sbagliato (testa una funzionalita' di Environment,
     * non di Keyboard)
     */
    public void testShutDown()
    {
        assertTrue(keyboard.isCreated());
        keyboard.shutDown();
        assertFalse(keyboard.isCreated());
    }

}
