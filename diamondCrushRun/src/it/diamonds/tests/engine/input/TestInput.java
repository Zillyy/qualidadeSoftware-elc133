package it.diamonds.tests.engine.input;


import it.diamonds.engine.TimerInterface;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.tests.mocks.MockKeyboard;
import it.diamonds.tests.mocks.MockTimer;
import junit.framework.TestCase;


public class TestInput extends TestCase
{
    private TimerInterface timer;

    private Input input;


    public void setUp()
    {
        timer = MockTimer.create();

        input = Input.create(MockKeyboard.create(), timer);
        input.setEventMappings(EventMappings.createForTesting());
    }


    public void testQueueEmpty()
    {
        assertTrue(input.isEmpty());
    }


    public void testQueueNotEmpty()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        assertFalse(input.isEmpty());
    }


    public void testFlushEventQueue()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.flushEvents();
        assertTrue(input.isEmpty());
    }


    public void testQueueItemRemoval()
    {
        input.notify(Event.create(Code.RIGHT, State.PRESSED));

        assertTrue(input.extractEvent().is(Code.RIGHT));
    }


    public void testQueueSequenceOrder()
    {
        input.notify(Event.create(Code.RIGHT, State.PRESSED));
        input.notify(Event.create(Code.UP, State.PRESSED));

        assertTrue(input.extractEvent().is(Code.RIGHT));
        assertTrue(input.extractEvent().is(Code.UP));
    }


    public void testEventState()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        assertTrue(input.extractEvent().isPressed());
        assertTrue(input.extractEvent().isReleased());
    }


    public void testEventTimestamp() throws InterruptedException
    {
        long timeShift = 100;
        input.notify(Event.create(Code.RIGHT, State.PRESSED));
        timer.advance(timeShift);
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        long firstTimestamp = input.extractEvent().getTimestamp();
        long secondTimeStamp = input.extractEvent().getTimestamp();
        assertEquals("Timestamps should differ for the timeShift quantity",
            firstTimestamp + timeShift, secondTimeStamp);
    }


    public void testNotifyEscape()
    {
        input.notify(Event.create(Code.ESCAPE, State.PRESSED));

        assertTrue(input.extractEvent().is(Code.ESCAPE));
    }


    public void testEventMappingsSetsOK()
    {
        input.setEventMappings(null);
        assertTrue(input.isEmpty());
        input.setEventMappings(EventMappings.createForTesting());
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        assertFalse(input.isEmpty());
    }

}
