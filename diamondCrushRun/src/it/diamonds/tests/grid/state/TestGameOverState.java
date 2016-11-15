package it.diamonds.tests.grid.state;


import it.diamonds.GameLoop;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.GameOverState;
import it.diamonds.playfield.PlayField;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockInputReactor;

import java.io.IOException;


public class TestGameOverState extends AbstractGridTestCase
{
    private AbstractControllerState state;


    public void setUp() throws IOException
    {
        super.setUp();

        GameLoop loop = GameLoop.createForTesting(environment);
        PlayField field = loop.getPlayFieldOne();
        controller = field.getGridController();
        state = new GameOverState();
        grid = controller.getGrid();
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("GameOver"));
    }


    public void testReturnedState()
    {
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue(state.isCurrentState("GameOver"));
    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
