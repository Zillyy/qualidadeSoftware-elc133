package it.diamonds.tests.engine.input;


import it.diamonds.engine.TimerInterface;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.tests.mocks.MockKeyboard;
import it.diamonds.tests.mocks.MockTimer;
import junit.framework.TestCase;


// FIXME: convertire in una MockHandler o qualcosa del genere

final class HandlerForTestInputReactor extends AbstractEventHandler
{

    private boolean executed;

    private boolean pressed;


    public static HandlerForTestInputReactor create()
    {
        return new HandlerForTestInputReactor();
    }


    @Override
    protected void executeWhenPressed(InputReactor inputReactor)
    {
        executed = true;
        pressed = true;
    }


    @Override
    protected void executeWhenReleased(InputReactor inputReactor)
    {
        executed = true;
        pressed = false;
    }


    public boolean executed()
    {
        return executed;
    }


    public boolean pressed()
    {
        return pressed;
    }
}



public class TestInputReactor extends TestCase
{
    private TimerInterface timer;

    private Input input;

    private InputReactor reactor;

    private HandlerForTestInputReactor handler;

    private Event eventEnterPressed = Event.create(Code.ENTER, State.PRESSED);

    private Event eventEnterReleased = Event.create(Code.ENTER, State.RELEASED);


    public void setUp()
    {
        timer = MockTimer.create();
        input = Input.create(MockKeyboard.create(), timer);
        input.setEventMappings(EventMappings.createForTesting());
        reactor = new InputReactor(input, 50, 60);
        handler = HandlerForTestInputReactor.create();
        reactor.addHandler(Code.ENTER, handler);
    }


    public void testRepetitionDelaysSet()
    {
        assertEquals(50, reactor.getNormalRepeatDelay());
        assertEquals(60, reactor.getFastRepeatDelay());
    }


    public void testHandlerSet()
    {
        assertEquals(handler, reactor.getEventHandler(Code.ENTER));
    }


    public void testNoHandlerSet()
    {
        reactor = new InputReactor(input, 50, 60);
        assertNull(reactor.getEventHandler(Code.ENTER));
    }


    public void testHandlerRepeationDelaysSameAsInputReactor()
    {
        assertEquals(reactor.getNormalRepeatDelay(),
            handler.getNormalRepeatDelay());
        assertEquals(reactor.getFastRepeatDelay(), handler.getFastRepeatDelay());
    }


    public void testHandlerPressed()
    {
        input.notify(eventEnterPressed);
        reactor.reactToInput(timer.getTime());
        assertTrue(handler.executed());
        assertTrue(handler.pressed());
    }


    public void testHandlerReleased()
    {
        input.notify(eventEnterReleased);
        reactor.reactToInput(timer.getTime());
        assertTrue(handler.executed());
        assertFalse(handler.pressed());
    }


    public void testInputTimeCorrect()
    {
        long timeShift = 100;

        input.notify(eventEnterPressed);
        long firstInstant = timer.getTime();
        reactor.reactToInput(firstInstant);

        timer.advance(timeShift);

        input.notify(eventEnterReleased);
        long secondInstant = timer.getTime();
        reactor.reactToInput(secondInstant);

        assertEquals(
            "The Input Reactor last input timestamp must coincide with the last event timestamp.",
            firstInstant + timeShift, reactor.getLastInputTimeStamp());
    }


    public void testEmptyQueue()
    {
        input.notify(eventEnterPressed);
        reactor.emptyQueue();
        reactor.reactToInput(timer.getTime());
        assertFalse(handler.executed());
    }
}
