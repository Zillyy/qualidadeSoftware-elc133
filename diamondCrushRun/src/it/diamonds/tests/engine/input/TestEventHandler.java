package it.diamonds.tests.engine.input;


import it.diamonds.engine.TimerInterface;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputDeviceInterface;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.tests.mocks.MockKeyboard;
import it.diamonds.tests.mocks.MockTimer;
import junit.framework.TestCase;


// FIXME: convertire a MockHandler o qualcosa del genere

final class HandlerForTestEventHandler extends AbstractEventHandler
{

    private boolean executed;

    private boolean pressed;


    public static HandlerForTestEventHandler create()
    {
        return new HandlerForTestEventHandler();
    }


    protected void executeWhenPressed(InputReactor inputReactor)
    {
        // I HATE CHECKSTYLE
        executed = true;
        pressed = true;
    }


    protected void executeWhenReleased(InputReactor inputReactor)
    {
        // I HATE CHECKSTYLE
        executed = true;
        pressed = false;
    }


    public boolean executed()
    {
        // I HATE CHECKSTYLE
        return executed;
    }


    public boolean pressed()
    {
        // I HATE CHECKSTYLE
        return pressed;
    }


    public void resetStatus()
    {

    }

}



public class TestEventHandler extends TestCase
{
    private InputDeviceInterface keyboard;

    private TimerInterface timer;

    private Input input;

    private InputReactor reactor;

    private HandlerForTestEventHandler handler;


    public void setUp()
    {
        keyboard = MockKeyboard.create();
        timer = MockTimer.create();

        handler = HandlerForTestEventHandler.create();
    }


    public void testNormalRepeatDelaySet()
    {
        handler.setNormalRepeatDelay(100);
        assertEquals(100, handler.getNormalRepeatDelay());
    }


    public void testFastRepeatDelaySet()
    {
        handler.setFastRepeatDelay(100);
        assertEquals(100, handler.getFastRepeatDelay());
    }


    public void testPressed()
    {
        setupInput();
        handler.handleEvent(reactor, Event.create(Code.ENTER, State.PRESSED));
        assertTrue(handler.isPressed());
    }


    public void testReleased()
    {
        setupInput();
        handler.handleEvent(reactor, Event.create(Code.ENTER, State.PRESSED));
        handler.handleEvent(reactor, Event.create(Code.ENTER, State.RELEASED));
        assertFalse(handler.isPressed());
    }


    private void setupInput()
    {
        input = Input.create(keyboard, timer);
        input.setEventMappings(EventMappings.createForTesting());
        reactor = new InputReactor(input, 200, 100);
        reactor.addHandler(Code.ENTER, handler);
    }


    public void testRepeatitionOnTimeDelay()
    {
        setupInput();
        input.notify(Event.create(Code.ENTER, State.PRESSED));
        reactor.reactToInput(timer.getTime());
        assertFalse(handler.isRepeated(0));
        assertTrue(handler.isRepeated(201));
        assertEquals(handler.getNormalRepeatDelay(),
            handler.getCurrentRepeatDelay());
    }


    public void testRepeatitionActivatedAndHandlerExecutedOnInputReaction()
    {
        setupInput();
        input.notify(Event.create(Code.ENTER, State.PRESSED));
        timer.advance(201);
        reactor.reactToInput(timer.getTime());
        assertTrue(handler.isRepeated(0));
        assertEquals(handler.getFastRepeatDelay(),
            handler.getCurrentRepeatDelay());
        assertTrue(handler.executed());
        assertTrue(handler.pressed());
    }


    public void testRepeatitionDisactivatedOnEventReleased()
    {
        setupInput();
        input.notify(Event.create(Code.ENTER, State.PRESSED));
        timer.advance(201);
        reactor.reactToInput(timer.getTime());
        input.notify(Event.create(Code.ENTER, State.RELEASED));
        reactor.reactToInput(timer.getTime());
        assertFalse(handler.isRepeated(0));
    }

}
