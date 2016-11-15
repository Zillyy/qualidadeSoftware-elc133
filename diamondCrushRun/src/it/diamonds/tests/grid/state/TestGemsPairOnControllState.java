package it.diamonds.tests.grid.state;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.GameLoop;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.GemsPairOnControlState;
import it.diamonds.playfield.PlayField;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockInputReactor;

import java.io.IOException;


public class TestGemsPairOnControllState extends AbstractGridTestCase
{
    private AbstractControllerState state;

    private GameLoop loop;

    private float normalGravity;

    private float strongestGravity;


    public void setUp() throws IOException
    {
        super.setUp();

        loop = GameLoop.createForTesting(environment);
        PlayField field = loop.getPlayFieldOne();
        controller = field.getGridController();
        state = new GemsPairOnControlState(environment);
        grid = controller.getGrid();

        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));

        normalGravity = 0.5f;
        strongestGravity = (float)environment.getConfig().getInteger(
            "StrongestGravityMultiplier")
            * normalGravity;
    }


    public void testCurrentStateWrongName()
    {
        assertFalse(state.isCurrentState("asdasdasd"));
    }


    public void testCurrentStateRightName()
    {
        assertTrue(state.isCurrentState("GemsPairOnControl"));
    }


    public void testBothGemsAreFalling()
    {
        controller.insertNewGemsPair();

        state = state.update(environment.getTimer().getTime(), controller);

        assertTrue("not correct state returned",
            state.isCurrentState("GemsPairOnControl"));
        assertEquals("not correct gravity", grid.getActualGravity(),
            normalGravity);
    }


    public void testOneGemsAreFalling()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createGem(DIAMOND), 12, 1);

        setGemsPair(createGem(DIAMOND), 11, 1, createGem(DIAMOND), 11, 2);

        state = state.update(environment.getTimer().getTime(), controller);

        assertTrue("not correct state returned",
            state.isCurrentState("GemsPairOnControl"));
        assertEquals("not correct gravity", grid.getActualGravity(),
            strongestGravity);
    }


    public void testBothGemsAreNotFalling()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createGem(DIAMOND), 12, 1);

        setGemsPair(createGem(DIAMOND), 11, 1, createGem(DIAMOND), 10, 1);

        state = state.update(environment.getTimer().getTime(), controller);

        // TODO manca test passaggio a StoneTurnState (refactoring needed)
        assertFalse("incorrect state returned", state.isCurrentState("Crush"));
    }


    private void setGemsPair(Gem gemPivot, int rowPivot, int columnPivot,
        Gem gemSlave, int rowSlave, int columnSlave)
    {
        DroppablesPair gemsPair = controller.getGemsPair();
        insertAndUpdate(gemPivot, rowPivot, columnPivot);
        insertAndUpdate(gemSlave, rowSlave, columnSlave);
        gemsPair.setSlave(gemSlave);
        gemsPair.setPivot(gemPivot);
    }


    public void testThisStateIsReactive()
    {
        MockInputReactor input = new MockInputReactor();
        state.reactToInput(input, 0);
        assertTrue(input.hasReacted());
    }

}
