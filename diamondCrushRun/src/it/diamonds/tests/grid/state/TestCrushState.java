package it.diamonds.tests.grid.state;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.CrushState;
import it.diamonds.grid.state.GemsPairOnControlState;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockInputReactor;

import java.io.IOException;


public class TestCrushState extends AbstractGridTestCase
{
    private AbstractControllerState state;


    public void setUp() throws IOException
    {
        super.setUp();
        state = new CrushState(environment);
        setDiamondsGemsPair(grid, controller.getGemsPair());
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("Crush"));
    }


    public void testDroppedGemCanMove()
    {
        insertAndUpdate(createGem(DIAMOND), 5, 1);
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue("not correct state returned",
            state.isCurrentState("GemFall"));
    }


    public void testCrushAndDroppedGemCanMove()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createChest(DIAMOND), 12, 1);
        insertAndUpdate(createChest(EMERALD), 11, 1);

        makeAllGemsFall();
        state = state.update(environment.getTimer().getTime(), controller);
        assertNull("Not crush appened", grid.getDroppableAt(13, 1));
        assertNull("Not crush appened", grid.getDroppableAt(12, 1));
    }


    public void testCrushAndDroppedGemCantMove()
    {
        Gem gem = createGem(DIAMOND);
        insertAndUpdate(gem, 13, 1);
        insertAndUpdate(createChest(DIAMOND), 12, 1);
        makeAllGemsFall();

        state = state.update(environment.getTimer().getTime(), controller);
        assertNull("Not crush appened", grid.getDroppableAt(13, 1));
        assertNull("Not crush appened", grid.getDroppableAt(12, 1));

        assertNull("not cleared gemsPair", controller.getGemsPair().getPivot());

        assertEquals("grid chain not closed", gem.getScore(),
            grid.getScoreCalculator().getScore());
    }


    public void testCrushAndDroppedGemCanMoveReturnState()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createChest(DIAMOND), 12, 1);
        insertAndUpdate(createChest(EMERALD), 11, 1);

        makeAllGemsFall();
        state = state.update(environment.getTimer().getTime(), controller);

        assertTrue("not correct state returned",
            state.isCurrentState("GemFall"));
    }


    public void testCrushAndDroppedGemCantMoveReturnState()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createChest(DIAMOND), 12, 1);
        makeAllGemsFall();

        state = state.update(environment.getTimer().getTime(), controller);

        /*
         * TODO manca test passaggio a stoneFallState(refactoring needed) stato
         * successivo: StoneFallState.update, cioè WaitBeforeNewGemsPairState
         */
        assertFalse("not correct state returned",
            state.isCurrentState("GemFall"));
        assertTrue("not correct state returned",
            state.isCurrentState("WaitBeforeNewGemsPair"));
    }


    public void testCrushStateCostructorWithReturnState()
    {
        GemsPairOnControlState gemsPairOnControlState = new GemsPairOnControlState(
            environment);
        long waitCrushDelay = environment.getConfig().getInteger(
            "DelayBetweenCrushes");
        state = new CrushState(environment, gemsPairOnControlState);

        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createChest(EMERALD), 11, 2);
        insertAndUpdate(createChest(DIAMOND), 10, 2);

        state = state.update(environment.getTimer().getTime(), controller);

        makeAllGemsFall();

        Droppable pivot = createGem(EMERALD);
        Droppable slave = createGem(EMERALD);
        grid.insertDroppable(pivot, 4, 4);
        grid.insertDroppable(slave, 5, 4);
        controller.getGemsPair().setPivot(pivot);
        controller.getGemsPair().setSlave(slave);

        state = state.update(environment.getTimer().getTime(), controller);

        int pivotRow = controller.getGemsPair().getPivot().getCell().getBottomRow();
        int slaveRow = controller.getGemsPair().getSlave().getCell().getBottomRow();

        state = state.update(environment.getTimer().getTime() + waitCrushDelay,
            controller);

        assertEquals(pivotRow + 1,
            controller.getGemsPair().getPivot().getCell().getBottomRow());
        assertEquals(slaveRow + 1,
            controller.getGemsPair().getSlave().getCell().getBottomRow());

        assertTrue(state.isCurrentState("GemsPairOnControl"));
    }


    public void testThisStateIsNotReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertFalse(input.hasReacted());
    }

}
