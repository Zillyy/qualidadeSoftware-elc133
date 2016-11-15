package it.diamonds.tests.grid.state;


import it.diamonds.GameLoop;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.WaitNextCrushState;
import it.diamonds.playfield.PlayField;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockInputReactor;
import it.diamonds.tests.mocks.MockTimer;

import java.io.IOException;


public class TestWaitNextCrushState extends AbstractGridTestCase
{
    private AbstractControllerState state;

    private int delayBeforeNextCrush;


    public void setUp() throws IOException
    {
        super.setUp();

        GameLoop loop = GameLoop.createForTesting(environment);
        PlayField field = loop.getPlayFieldOne();
        controller = field.getGridController();
        grid = controller.getGrid();
        this.delayBeforeNextCrush = environment.getConfig().getInteger(
            "DelayBetweenCrushes");
        state = new WaitNextCrushState(environment,
            environment.getTimer().getTime());
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("WaitNextCrush"));
    }


    public void testInitState()
    {
        float normalGravity = 0.5f;
        state = new WaitNextCrushState(environment,
            environment.getTimer().getTime());
        for(int t = 1; t <= delayBeforeNextCrush; t++)
        {
            assertEquals("not correct gravity", normalGravity,
                grid.getActualGravity(), 0.001f);
            assertTrue(state.isCurrentState("WaitNextCrush"));

            environment.getTimer().advance(1);
            state = state.update(environment.getTimer().getTime(), controller);
        }
        // TODO manca test passaggio a crushState(refactoring needed)
        assertFalse(state.isCurrentState("WaitNextCrush"));
    }


    public void testInitState2()
    {
        state = new WaitNextCrushState(environment, 12345);
        ((MockTimer)environment.getTimer()).setTime(0);

        environment.getTimer().advance(12345 + delayBeforeNextCrush - 1);
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue(state.isCurrentState("WaitNextCrush"));

        environment.getTimer().advance(1);
        state = state.update(environment.getTimer().getTime(), controller);
        assertFalse(state.isCurrentState("WaitNextCrush"));
    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
