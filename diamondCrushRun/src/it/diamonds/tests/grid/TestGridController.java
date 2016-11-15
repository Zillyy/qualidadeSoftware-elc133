package it.diamonds.tests.grid;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.Point;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.GridController;
import it.diamonds.tests.mocks.MockDroppableGenerator;
import it.diamonds.tests.mocks.MockRandomGenerator;
import it.diamonds.tests.mocks.MockTimer;

import java.io.IOException;


public class TestGridController extends AbstractGridTestCase
{

    private Droppable gem;

    private Input input;

    private InputReactor inputReactor;

    private DroppablesPair gemsPair;

    private int newGemDelay;

    private MockDroppableGenerator gemGenerator;


    public void setUp() throws IOException
    {
        super.setUp();

        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());
        inputReactor = new InputReactor(input, 150, 50);

        gemGenerator = new MockDroppableGenerator(environment.getEngine());
        controller = new GridController(environment, grid, inputReactor,
            gemGenerator);

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        grid.removeDroppableFromGrid(controller.getGemsPair().getSlave());
        gemsPair = controller.getGemsPair();
        gemsPair.setNoPivot();
        gemsPair.setNoSlave();

        gem = Gem.createForTesting(environment.getEngine());

        newGemDelay = environment.getConfig().getInteger("NewGemDelay");
    }


    private void fillColumn(int column)
    {
        for(int i = 0; i < 14; i++)
        {
            Droppable newGem = Gem.createForTesting(environment.getEngine());
            grid.insertDroppable(newGem, i, column);
            gemsPair.setPivot(newGem);

            newGem.getFallingObject().drop();
        }
    }


    public void testGemGeneratorCannotBeNull()
    {
        assertNotNull(controller.getGemGenerator());
    }


    public void testGetGemsPair()
    {
        assertNotNull(controller.getGemsPair());
    }


    public void testGridDoesntInsertNewGems()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        grid.updateDroppable(gem);
        assertEquals(gem, gemsPair.getPivot());
    }


    public void testGridControllerUpdateGrid()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        assertEquals(13, gem.getCell().getTopRow());
    }


    public void testNewGemsPairInsertion()
    {

        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(
            environment.getConfig().getInteger("NewGemDelay"));
        controller.update(environment.getTimer().getTime());

        assertNotNull(gemsPair.getPivot());
        assertTrue(gem != gemsPair.getPivot());
    }


    public void testNullGemUnderControlWhileWaiting()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());

        assertNull("gem under control is not null", gemsPair.getPivot());
    }


    public void testGemInsertedAfterTimeout()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        ((MockTimer)environment.getTimer()).setTime(newGemDelay);
        controller.update(environment.getTimer().getTime());
        assertTrue(gem != gemsPair.getPivot());
    }


    public void testWaitBeforeInsertionNewGemWithTimeBaseNotZero()
    {
        ((MockTimer)environment.getTimer()).setTime(150);
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(newGemDelay - 1);
        controller.update(environment.getTimer().getTime());
        assertNull(gemsPair.getPivot());
    }


    public void testAfterSecondDelay()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(newGemDelay);
        controller.update(environment.getTimer().getTime());

        gem = gemsPair.getPivot();
        gem.getFallingObject().drop();
        grid.removeDroppableFromGrid(gemsPair.getSlave());
        gemsPair.setNoSlave();

        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(newGemDelay);
        controller.update(environment.getTimer().getTime());

        assertNotNull("Grid didn't create a new gem", gemsPair.getPivot());
        assertTrue("Created gem must be a new one", gem != gemsPair.getPivot());
    }


    public void testInputWhileWaiting()
    {

        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        assertTrue(grid.isDroppableAt(13, 4));

        input.notify(Event.create(Code.RIGHT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        assertTrue(grid.isDroppableAt(13, 4));
    }


    public void testQueueNotEmptyBeforeGenerateNewGem()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        ((MockTimer)environment.getTimer()).setTime(newGemDelay);
        controller.update(environment.getTimer().getTime());
        assertTrue(!input.isEmpty());
    }


    public void testGridNotGameOver()
    {
        assertFalse(grid.isColumnFull(4));
    }


    public void testGridGameOver()
    {
        fillColumn(4);
        assertTrue(grid.isColumnFull(4));
    }


    public void testIsNotGameOver()
    {
        assertFalse(controller.isGameOver());
    }


    public void testIsGameOver()
    {
        fillColumn(4);
        controller.update(environment.getTimer().getTime());
        ((MockTimer)environment.getTimer()).setTime(environment.getConfig().getInteger(
            "NewGemDelay"));
        controller.update(environment.getTimer().getTime());
        assertTrue(controller.isGameOver());
    }


    public void testIsNotGameOverOnSingleGemCrush()
    {
        for(int i = 2; i < 14; i++)
        {
            Droppable newGem = createGem(EMERALD);
            grid.insertDroppable(newGem, i, 4);
            gemsPair.setPivot(newGem);

            newGem.getFallingObject().drop();
        }
        Droppable newGem = createChest(DIAMOND);
        grid.insertDroppable(newGem, 1, 4);
        gemsPair.setPivot(newGem);

        newGem.getFallingObject().drop();
        controller.insertNewGemsPair();

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(
            environment.getConfig().getInteger("NewGemDelay"));
        controller.update(environment.getTimer().getTime());
        assertFalse("Not here in game over", controller.isGameOver());
    }


    public void testNotGameOverInCrushState()
    {

        for(int i = 1; i < 14; i++)

        {

            Droppable newGem = Gem.createForTesting(environment.getEngine());

            grid.insertDroppable(newGem, i, 4);

            gemsPair.setPivot(newGem);

            newGem.getFallingObject().drop();

        }

        Droppable chest = createChest(DIAMOND);

        grid.insertDroppable(chest, 0, 4);

        grid.updateDroppable(chest);

        controller.update(environment.getTimer().getTime());

        assertFalse("Not here in game over", controller.isGameOver());

        controller.update(environment.getTimer().getTime());

        ((MockTimer)environment.getTimer()).setTime(environment.getConfig().getInteger(
            "NewGemDelay"));

        controller.update(environment.getTimer().getTime());

        assertFalse("Not here in game over", controller.isGameOver());

    }


    public void testIsNotGameOverWithFallingGem()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        assertFalse(controller.isGameOver());
    }


    public void testControllerGameOver()
    {
        try
        {
            fillColumn(4);
            controller.update(environment.getTimer().getTime());
            environment.getTimer().advance(300);
            controller.update(environment.getTimer().getTime());
        }
        catch(Exception e)
        {
            fail("GridController can't manage \"game over\" conditions");
        }
    }


    public void testInputMustBeProcessedWhileWaiting()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        controller.update(environment.getTimer().getTime());

        environment.getTimer().advance(newGemDelay - 100);

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());

        assertEquals("InputReactor not called",
            environment.getTimer().getTime(),
            inputReactor.getLastInputTimeStamp());
    }


    public void testInputStateSavedWhileWaiting()
    {
        grid.insertDroppable(gem, 13, 4);
        gemsPair.setPivot(gem);

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.RIGHT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());

        controller.update(environment.getTimer().getTime());

        environment.getTimer().advance(newGemDelay);
        controller.update(environment.getTimer().getTime());

        int newGemColumn = gemsPair.getPivot().getCell().getLeftColumn();

        input.notify(Event.create(Code.RIGHT, State.RELEASED));
        controller.reactToInput(environment.getTimer().getTime());

        assertEquals("gem wasn't moved left", newGemColumn - 1,
            gemsPair.getPivot().getCell().getLeftColumn());
    }


    public void testNoWaitStateWhenPivotGemCollide()
    {
        grid.insertDroppable(gem, 3, 4);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            4);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            5);

        gemsPair.setPivot(grid.getDroppableAt(2, 5));

        Droppable slaveGem = grid.getDroppableAt(2, 4);
        gemsPair.setSlave(slaveGem);

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(newGemDelay);
        controller.update(environment.getTimer().getTime());

        assertSame(slaveGem, gemsPair.getSlave());
    }


    public void testNoWaitStateWhenSlaveGemCollide()
    {
        grid.insertDroppable(gem, 3, 4);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            4);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            5);

        Droppable pivotGem = grid.getDroppableAt(2, 4);
        gemsPair.setPivot(pivotGem);

        gemsPair.setSlave(grid.getDroppableAt(2, 5));

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(newGemDelay);
        controller.update(environment.getTimer().getTime());

        assertSame(pivotGem, gemsPair.getPivot());
    }


    public void testSetIncomingStones()
    {
        controller.setIncomingStones(3);
        assertEquals(3, controller.getIncomingStones());
        controller.setIncomingStones(7);
        assertEquals(7, controller.getIncomingStones());
    }


    public void testStonesToSend()
    {
        controller.setStonesToSend(1);
        assertEquals("The number of stones to send must be 1", 1,
            controller.getStonesToSend());

        controller.setStonesToSend(0);
        assertEquals("The number of stones to send must be 0", 0,
            controller.getStonesToSend());
    }


    public void testCreate() throws IOException
    {
        int chestProb = environment.getConfig().getInteger("ChestProbability");
        int flashProb = environment.getConfig().getInteger("FlashProbability");

        int startChest = flashProb;
        int startGem = flashProb + chestProb;

        int mockValues[] = { startGem, 1, startChest, 1 };
        MockRandomGenerator randomGenerator = new MockRandomGenerator(
            mockValues);

        controller = GridController.create(environment, inputReactor,
            randomGenerator, new Point(0, 0));
        gemsPair = controller.getGemsPair();
        assertTrue(gemsPair.getPivot().getGridObject().getType().isGem());
        assertTrue(gemsPair.getSlave().getGridObject().getType().isChest());
    }


    public void testUpdateFallsWithOutGemsPair()
    {
        Droppable pivot = createGem(DIAMOND);
        Droppable slave = createGem(DIAMOND);
        Droppable other = createGem(DIAMOND);
        grid.insertDroppable(pivot, 5, 1);
        grid.insertDroppable(slave, 6, 1);
        grid.insertDroppable(other, 6, 2);

        controller.getGemsPair().setPivot(pivot);
        controller.getGemsPair().setSlave(slave);

        controller.updateFallsWithOutGemsPair();

        assertEquals(pivot, grid.getDroppableAt(5, 1));
        assertEquals(slave, grid.getDroppableAt(6, 1));
        assertEquals(other, grid.getDroppableAt(7, 2));
    }


    public void testDroppedGemWithoutGemsPairCanMoveDown()
    {
        Droppable pivot = createGem(DIAMOND);
        Droppable slave = createGem(DIAMOND);

        grid.insertDroppable(pivot, 5, 1);
        grid.insertDroppable(slave, 6, 1);
        controller.getGemsPair().setPivot(pivot);
        controller.getGemsPair().setSlave(slave);

        assertFalse(controller.droppedGemWithoutGemsPairCanMoveDown());

        grid.insertDroppable(createGem(DIAMOND), 6, 2);

        assertTrue(controller.droppedGemWithoutGemsPairCanMoveDown());
    }
}
