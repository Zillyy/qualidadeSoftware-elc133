package it.diamonds.tests.grid.state;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.GameLoop;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.GemFallState;
import it.diamonds.playfield.PlayField;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockInputReactor;

import java.io.IOException;


public class TestGemFallState extends AbstractGridTestCase
{
    private AbstractControllerState state;

    private GameLoop loop;


    public void setUp() throws IOException
    {
        super.setUp();

        loop = GameLoop.createForTesting(environment);
        PlayField field = loop.getPlayFieldOne();
        controller = field.getGridController();
        state = new GemFallState(environment);
        grid = controller.getGrid();

        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("GemFall"));
    }


    public void testDontReactToDownKeyDuringGemFallState()
    {
        Droppable gem = createGem(DroppableColor.DIAMOND);
        insertAndUpdate(gem, 0, 0);

        controller.update(environment.getTimer().getTime());
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


    public void testDroppedGemCanMove()
    {
        makeAllGemsFall();
        grid.insertDroppable(createGem(DIAMOND), 5, 1);
        assertNull(grid.getDroppableAt(6, 1));
        state = state.update(environment.getTimer().getTime(), controller);
        assertNotNull("Gem not fallDown", grid.getDroppableAt(6, 1));
    }


    public void testDroppedGemCantMoveReturnState()
    {
        makeAllGemsFall();
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue(state.isCurrentState("WaitNextCrush"));
    }


    public void testDroppedGemCanMoveReturnState()
    {
        makeAllGemsFall();
        grid.insertDroppable(createGem(DIAMOND), 5, 1);
        assertNull(grid.getDroppableAt(6, 1));
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue(state.isCurrentState("GemFall"));
        assertNotNull(grid.getDroppableAt(6, 1));
    }


    public void testGemsPairDontFallDown()
    {
        makeAllGemsFall();
        Droppable pivot = createGem(DIAMOND);
        Droppable slave = createGem(DIAMOND);
        Droppable other = createGem(DIAMOND);
        grid.insertDroppable(pivot, 5, 1);
        grid.insertDroppable(slave, 6, 1);
        grid.insertDroppable(other, 6, 2);

        controller.getGemsPair().setPivot(pivot);
        controller.getGemsPair().setSlave(slave);

        state.update(environment.getTimer().getTime(), controller);

        assertEquals(pivot, grid.getDroppableAt(5, 1));
        assertEquals(slave, grid.getDroppableAt(6, 1));
        assertEquals(other, grid.getDroppableAt(7, 2));
    }


    public void testGemsPairCanFallButNotReturnGemFallState()
    {
        makeAllGemsFall();
        Droppable pivot = createGem(DIAMOND);
        Droppable slave = createGem(DIAMOND);
        grid.insertDroppable(pivot, 5, 1);
        grid.insertDroppable(slave, 6, 1);

        controller.getGemsPair().setPivot(pivot);
        controller.getGemsPair().setSlave(slave);

        assertFalse(state.update(environment.getTimer().getTime(), controller).isCurrentState(
            "GemFall"));

    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
