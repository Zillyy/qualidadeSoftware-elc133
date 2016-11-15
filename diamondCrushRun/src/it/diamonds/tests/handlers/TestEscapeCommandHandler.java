package it.diamonds.tests.handlers;


import it.diamonds.GameLoop;
import it.diamonds.engine.Environment;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.handlers.EscapeCommandHandler;
import junit.framework.TestCase;


public class TestEscapeCommandHandler extends TestCase
{
    private Environment environment;

    private Input input;

    private InputReactor inputReactor;

    private GameLoop gameLoop;


    protected void setUp() throws Exception
    {
        super.setUp();

        environment = Environment.createForTesting(800, 600, "");
        gameLoop = GameLoop.createForTesting(environment);
        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());

        inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        inputReactor.addHandler(Code.ESCAPE, new EscapeCommandHandler(gameLoop));
    }


    public void testGameExit()
    {
        input.notify(Event.create(Code.ESCAPE, State.PRESSED));
        input.notify(Event.create(Code.ESCAPE, State.RELEASED));

        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("The GameLoop must be stopped", gameLoop.isFinished());
    }


    public void testGameNotExit()
    {
        input.notify(Event.create(Code.ESCAPE, State.PRESSED));
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertFalse("The GameLoop must be running", gameLoop.isFinished());
    }

}
