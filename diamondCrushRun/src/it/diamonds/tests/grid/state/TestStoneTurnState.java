package it.diamonds.tests.grid.state;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableType.STONE;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Stone;
import it.diamonds.grid.state.StoneTurnState;
import it.diamonds.tests.mocks.MockInputReactor;
import it.diamonds.tests.playfield.AbstractPlayFieldTestCase;

import java.io.IOException;


public class TestStoneTurnState extends AbstractPlayFieldTestCase
{
    private StoneTurnState state;

    private Droppable stone;


    public void setUp() throws IOException
    {
        super.setUp();

        state = new StoneTurnState(environment);
        stone = gemFactory.create(STONE, DIAMOND);
    }


    public void testStateName()
    {
        assertTrue(state.isCurrentState("StoneTurn"));
    }


    public void testWrongStateName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testStoneTurnAction()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        grid.insertDroppable(stone, 13, 0);

        state.update(0, controller);
        environment.getTimer().advance(Stone.ANIMATION_FRAME_DELAY);
        state.update(0, controller); // first parameter ignored

        assertEquals(6, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStateChangeWithEmptyGrid()
    {
        assertFalse(state.update(0, controller).isCurrentState("StoneTurn"));
    }


    public void testStateChangeWithNoTurningStones()
    {
        stone.getAnimatedObject().setCurrentFrame(7);
        grid.insertDroppable(stone, 13, 0);
        assertFalse(state.update(0, controller).isCurrentState("StoneTurn"));
    }


    public void testStateChangeWithNoMoreTurningStones()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        grid.insertDroppable(stone, 13, 0);

        state.update(0, controller);
        environment.getTimer().advance(Stone.ANIMATION_FRAME_DELAY);

        assertTrue(state.update(0, controller).isCurrentState("StoneTurn"));
    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
