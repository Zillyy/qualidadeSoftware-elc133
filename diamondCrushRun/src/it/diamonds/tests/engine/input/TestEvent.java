package it.diamonds.tests.engine.input;


import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import junit.framework.TestCase;


public class TestEvent extends TestCase
{

    public void testCodeIsCorrect()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED);
        assertEquals(Code.DOWN, event.getCode());
    }


    public void testIsMethodWorksCorrectly()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED);
        assertTrue(event.is(Code.DOWN));
    }


    public void testStateIsCorrect()
    {
        Event event = Event.create(Code.DOWN, State.PRESSED);
        assertEquals(State.PRESSED, event.getState());
    }


    public void testEventIsPressed()
    {
        Event event = Event.create(Code.DOWN, State.PRESSED);
        assertTrue(event.isPressed());
    }


    public void testEventIsReleased()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED);
        assertTrue(event.isReleased());
    }


    public void testTimestamp()
    {
        Event event = Event.create(Code.DOWN, State.PRESSED, 123);
        assertEquals(123, event.getTimestamp());
    }


    public void testTimestampWasSetCorrectly()
    {
        Event event = Event.create(Code.DOWN, State.PRESSED, 123);
        assertEquals(123, event.getTimestamp());
    }


    public void testCodeIsChangedCorrectlyOnCopy()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED, 123);
        Event copy = event.copyAndChange(Code.UP);
        assertTrue(copy.is(Code.UP));
        assertTrue(copy.isReleased());
        assertEquals(123, copy.getTimestamp());
    }


    public void testTimestampIsChangedCorrectlyOnCopy()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED, 123);
        Event copy = event.copyAndChange(987);
        assertTrue(copy.is(Code.DOWN));
        assertTrue(copy.isReleased());
        assertEquals(987, copy.getTimestamp());
    }


    public void testCodeAndTimestampAreChangedCorrectlyOnCopy()
    {
        Event event = Event.create(Code.DOWN, State.RELEASED, 123);
        Event copy = event.copyAndChange(Code.UP, 987);
        assertTrue(copy.is(Code.UP));
        assertTrue(copy.isReleased());
        assertEquals(987, copy.getTimestamp());
    }

}
