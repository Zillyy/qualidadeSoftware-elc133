package it.diamonds.tests.engine.input;


import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.tests.mocks.MockKeyboard;
import it.diamonds.tests.mocks.MockTimer;
import junit.framework.TestCase;


public class TestInputDevice extends TestCase
{
    private Input input;

    private EventMappings keyMappings;

    private MockKeyboard keyboard;


    public void setUp()
    {
        keyboard = MockKeyboard.create();
        input = Input.create(keyboard, MockTimer.create());
        keyMappings = EventMappings.create();
        keyMappings.setMapping(Code.KEY_A, Code.KEY_Z);
        input.setEventMappings(keyMappings);
    }


    public void testAddOneInput()
    {
        Event keyEvent = Event.create(Code.KEY_A, State.PRESSED);
        keyboard.notify(keyEvent);

        assertTrue("Uncorrect KeyCode mapping in the input",
            input.extractEvent().is(Code.KEY_Z));
    }


    public void testUpdateCorrectlyHandled()
    {
        assertFalse(keyboard.updated());
        keyboard.update();
        assertTrue(keyboard.updated());
    }
}
