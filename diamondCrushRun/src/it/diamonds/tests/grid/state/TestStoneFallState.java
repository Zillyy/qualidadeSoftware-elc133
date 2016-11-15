package it.diamonds.tests.grid.state;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableType.GEM;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.Pattern;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;
import it.diamonds.grid.query.CanMoveDownQuery;
import it.diamonds.grid.state.StoneFallState;
import it.diamonds.tests.mocks.MockInputReactor;
import it.diamonds.tests.playfield.AbstractPlayFieldTestCase;

import java.io.IOException;


public class TestStoneFallState extends AbstractPlayFieldTestCase
{
    private Pattern pattern;

    private StoneFallState state;


    public void setUp() throws IOException
    {
        super.setUp();

        pattern = new Pattern(0);
        state = new StoneFallState(environment, pattern);
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("StoneFall"));
    }


    public void testNextState()
    {
        assertTrue(state.update(environment.getTimer().getTime(), controller).isCurrentState(
            "WaitBeforeNewGemsPair"));
    }


    public void testSameStateWithIncomingStones()
    {
        controller.setIncomingStones(3);
        assertSame(state, state.update(environment.getTimer().getTime(),
            controller));
    }


    public void testOneStoneAdded()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);
        assertTrue(grid.isDroppableAt(1, 0));
    }


    public void testTwoStonesAdded()
    {
        controller.setIncomingStones(2);
        state.update(environment.getTimer().getTime(), controller);
        assertTrue(grid.isDroppableAt(1, 0));
        assertTrue(grid.isDroppableAt(1, 1));
    }


    public void testSameStateWithStonesUnderControl()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);
        assertSame(state, state.update(environment.getTimer().getTime(),
            controller));
    }


    public void testStateChangesWhenStoneDrops()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);
        Droppable stone = grid.getDroppableAt(1, 0);
        stone.getFallingObject().drop();
        assertTrue(state.update(environment.getTimer().getTime(), controller).isCurrentState(
            "WaitBeforeNewGemsPair"));
    }


    public void testSameStateWhenOneOfTwoStonesDrops()
    {
        controller.setIncomingStones(2);
        state.update(environment.getTimer().getTime(), controller);
        Droppable stone1 = grid.getDroppableAt(1, 0);
        stone1.getFallingObject().drop();
        assertSame(state, state.update(environment.getTimer().getTime(),
            controller));
        Droppable stone2 = grid.getDroppableAt(1, 1);
        stone2.getFallingObject().drop();
        assertTrue(state.update(environment.getTimer().getTime(), controller).isCurrentState(
            "WaitBeforeNewGemsPair"));
    }


    public void testStonesAreUpdated()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);
        assertFalse(grid.isDroppableAt(0, 0));
        assertTrue(grid.isDroppableAt(1, 0));
    }


    public void testStoneGravity()
    {
        controller.setIncomingStones(4);
        state.update(environment.getTimer().getTime(), controller);
        assertEquals(10.0f, grid.getActualGravity(), 0.001f);
    }


    public void testGravityIsResetAfterStonesHaveFallen()
    {
        controller.setIncomingStones(4);
        while(state == state.update(environment.getTimer().getTime(),
            controller))
        {
            ;
        }
        assertEquals(0.5f, grid.getActualGravity(), 0.001f);
    }


    public void testStoneColors()
    {
        controller.setIncomingStones(8);
        state.update(environment.getTimer().getTime(), controller);
        for(int i = 0; i < 8; i++)
        {
            assertEquals(pattern.getDroppableColor(i),
                grid.getDroppableAt(1, i).getGridObject().getColor());
        }
    }


    public void testNoReinsertionOfPreviousStones()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);
        controller.setIncomingStones(1);
        try
        {
            state.update(environment.getTimer().getTime(), controller);
        }
        catch(IllegalArgumentException e)
        {
            fail("attempt to reinsert already inserted stones");
        }
    }


    public void testStonesDuringInsertionWillInsertedNext()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);

        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);

        assertEquals("there must be present only a stone", 1,
            controller.getGrid().getNumberOfDroppables());
    }


    private boolean isFirstRowFreeForFalling(GridController controller)
    {
        int columns = controller.getGrid().getNumberOfColumns();
        for(int i = 0; i < columns; i++)
        {
            if(controller.getGrid().isDroppableAt(1, i))
            {
                return false;
            }
        }
        return true;
    }


    public void testMoreThanEightStoneInsertion()
    {
        controller.setIncomingStones(9);

        state.update(environment.getTimer().getTime(), controller);
        while(!isFirstRowFreeForFalling(controller))
        {
            state.update(environment.getTimer().getTime(), controller);
        }
        state.update(environment.getTimer().getTime(), controller);

        assertEquals("The number of stones in the grid must be 9", 9,
            grid.getNumberOfDroppables());
        assertTrue("The ninth stone must be inserted in the first column",
            grid.isDroppableAt(1, 0));
        assertTrue("The ninth stone must be falling",
            grid.getDroppableAt(1, 0).getFallingObject().isFalling());
    }


    public void testMoreThanEightStoneColor()
    {
        controller.setIncomingStones(9);
        state.update(environment.getTimer().getTime(), controller);
        while(!isFirstRowFreeForFalling(controller))
        {
            state.update(environment.getTimer().getTime(), controller);
        }
        state.update(environment.getTimer().getTime(), controller);

        Droppable stone = grid.getDroppableAt(1, 0);

        assertEquals("The ninth stone color must be the first of th pattern",
            pattern.getDroppableColor(0), stone.getGridObject().getColor());
    }


    private void insertAndUpdate(Droppable gem, int row, int column)
    {
        grid.insertDroppable(gem, row, column);
        grid.updateDroppable(gem);
    }


    public void testStoneInsertionIfGemsPresent()
    {
        int rows = controller.getGrid().getNumberOfRows();
        int columns = controller.getGrid().getNumberOfColumns();

        for(int row = rows - 1; row >= 1; row--)
        {
            for(int column = 0; column < columns; column++)
            {
                insertAndUpdate(gemFactory.create(GEM, DIAMOND), row, column);
            }
        }

        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);

        Droppable stone = grid.getDroppableAt(0, 0);

        assertEquals("The number of droppable in the grid must be equals",
            8 * 13 + 1, grid.getNumberOfDroppables());

        assertNotNull("The stone must be inserted", stone);
    }


    public void testStoneInsertionIfStonesPresent()
    {
        insertStoneRows(13);

        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);

        Droppable stone = grid.getDroppableAt(0, 0);

        assertEquals("The number of stones in the grid must be equals",
            8 * 13 + 1, grid.getNumberOfDroppables());

        assertNotNull("The stone must be inserted", stone);
    }


    public void testMoreThanEightStoneInsertionIfStonesPresent()
    {
        insertStoneRows(12);
        controller.setIncomingStones(9);
        state.update(environment.getTimer().getTime(), controller);

        while(droppedGemCanMoveDown(grid))
        {
            state.update(environment.getTimer().getTime(), controller);
        }
        state.update(environment.getTimer().getTime(), controller);

        Droppable stone = grid.getDroppableAt(0, 0);

        assertEquals("The number of stones in the grid must be equals",
            8 * 12 + 9, grid.getNumberOfDroppables());
        assertNotNull("The stone must be inserted", stone);
    }


    public void testNoStoneInsertion()
    {
        int rows = controller.getGrid().getNumberOfRows();
        insertStoneRows(rows);
        controller.setIncomingStones(1);

        try
        {
            state.update(environment.getTimer().getTime(), controller);
        }
        catch(Exception e)
        {
            fail("The stone must not be insert");
        }
    }


    public void testStoneFallFaster()
    {
        controller.setIncomingStones(1);

        controller.update(environment.getTimer().getTime());

        assertEquals(environment.getConfig().getInteger(
            "StrongestGravityMultiplier") * 0.5, grid.getActualGravity(), 0.01);

        loop.getPlayerOneInput().notify(Event.create(Code.DOWN, State.PRESSED));

        controller.reactToInput(environment.getTimer().getTime());

        assertEquals(environment.getConfig().getInteger(
            "StrongestGravityMultiplier") * 0.5, grid.getActualGravity(), 0.01);

        loop.getPlayerOneInput().notify(Event.create(Code.DOWN, State.RELEASED));

        controller.reactToInput(environment.getTimer().getTime());

        assertEquals(environment.getConfig().getInteger(
            "StrongestGravityMultiplier") * 0.5, grid.getActualGravity(), 0.01);
    }


    public void testStoneDontReactToInput()
    {
        controller.setIncomingStones(1);
        controller.update(environment.getTimer().getTime());

        loop.getPlayerOneInput().notify(Event.create(Code.RIGHT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());

        assertTrue("Stone moved",
            grid.getDroppableAt(1, 0).getGridObject().getType().isStone());
    }


    private Droppable insertAndGetSingleStone()
    {
        controller.setIncomingStones(1);
        state.update(environment.getTimer().getTime(), controller);

        return grid.getDroppableAt(1, 0);
    }


    private void insertStoneRows(int numberOfRows)
    {
        controller.setIncomingStones(numberOfRows * 8);
        state.update(environment.getTimer().getTime(), controller);

        while(droppedGemCanMoveDown(grid))
        {
            state.update(environment.getTimer().getTime(), controller);
        }

        state.update(environment.getTimer().getTime(), controller);
    }


    public void testStoneFrameInFirstSegment()
    {
        Droppable stone = insertAndGetSingleStone();

        assertEquals("Stone must be using the first frame", 0,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneFrameInSecondSegment()
    {
        insertStoneRows(2);
        Droppable stone = insertAndGetSingleStone();

        assertEquals("Stone must be using the first frame", 1,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneFrameInThirdSegment()
    {
        insertStoneRows(4);

        // TODO: strano comportamento.
        insertAndGetSingleStone();
        Droppable stone = insertAndGetSingleStone();

        assertEquals("Stone must be using the third frame", 2,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneFrameInFourthSegment()
    {
        insertStoneRows(7);
        Droppable stone = insertAndGetSingleStone();

        assertEquals("Stone must be using the fourth frame", 3,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneFrameInFifthSegment()
    {
        insertStoneRows(10);
        Droppable stone = insertAndGetSingleStone();

        assertEquals("Stone must be using the fifth  frame", 4,
            stone.getAnimatedObject().getCurrentFrame());
    }


    protected boolean droppedGemCanMoveDown(Grid grid)
    {
        CanMoveDownQuery action = new CanMoveDownQuery(null);

        grid.doQuery(action);

        return action.getResult();
    }


    public void testStoneFrameIfFalling()
    {
        controller.setIncomingStones(2 * 8 + 1);
        state.update(environment.getTimer().getTime(), controller);

        while(droppedGemCanMoveDown(grid))
        {
            state.update(environment.getTimer().getTime(), controller);
        }

        state.update(environment.getTimer().getTime(), controller);

        Droppable stone = grid.getDroppableAt(11, 0);

        assertEquals("Stone must be using the first frame", 1,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneInsertionNotInFirstColumnIfSecondFree()
    {
        insertAndUpdate(gemFactory.create(GEM, EMERALD), 13, 0);

        Droppable stone = insertAndGetSingleStone();

        assertNull("The stone must not be insert in the first column", stone);

        stone = grid.getDroppableAt(1, 1);

        assertNotNull("The stone must be insert in the second column", stone);
    }


    public void testStoneInsertionNotInFirstColumnIfThirdFree()
    {
        insertAndUpdate(gemFactory.create(GEM, EMERALD), 13, 0);
        insertAndUpdate(gemFactory.create(GEM, EMERALD), 13, 1);

        Droppable stone = insertAndGetSingleStone();

        assertNull("The stone must not be insert in the first column", stone);

        stone = grid.getDroppableAt(1, 2);

        assertNotNull("The stone must be insert in the third column", stone);
    }


    public void testStoneInsertionNotInSecondAndFourthColumn()
    {
        insertAndUpdate(gemFactory.create(GEM, EMERALD), 13, 0);
        insertAndUpdate(gemFactory.create(GEM, EMERALD), 13, 2);

        controller.setIncomingStones(2);
        state.update(environment.getTimer().getTime(), controller);

        Droppable stone0 = grid.getDroppableAt(1, 0);
        Droppable stone1 = grid.getDroppableAt(1, 1);
        Droppable stone2 = grid.getDroppableAt(1, 2);
        Droppable stone3 = grid.getDroppableAt(1, 3);

        assertNull("The stone must not be insert in the first column", stone0);
        assertNotNull("The stone must be insert in the second column", stone1);
        assertNull("The stone must not be insert in the third column", stone2);
        assertNotNull("The stone must be insert in the fourth column", stone3);
    }


    public void testPassToGameOverState()
    {
        controller.setIncomingStones(13 * 8 + 5);
        controller.update(environment.getTimer().getTime());

        while(!(grid.isDroppableAt(0, 4) && !grid.getDroppableAt(0, 4).getFallingObject().isFalling()))
        {
            controller.update(environment.getTimer().getTime());
        }
        controller.update(environment.getTimer().getTime());

        assertTrue("The game must pass to GameOver", controller.isGameOver());
    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
