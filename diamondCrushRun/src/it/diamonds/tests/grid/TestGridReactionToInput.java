package it.diamonds.tests.grid;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventHandler;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.GridController;
import it.diamonds.handlers.MirrorSlaveGemCommandHandler;
import it.diamonds.handlers.RotateClockwiseCommandHandler;
import it.diamonds.handlers.RotateCounterclockwiseCommandHandler;
import it.diamonds.tests.mocks.MockDroppableGenerator;
import it.diamonds.tests.mocks.MockTimer;

import java.io.IOException;


public class TestGridReactionToInput extends AbstractGridTestCase
{
    static final int GRID_COLUMNS = 8;

    static final int GRID_ROWS = 14;

    private Droppable gem;

    private Input input;

    private InputReactor inputReactor;

    private DroppablesPair gemsPair;


    public void setUp() throws IOException
    {
        super.setUp();

        gem = Gem.createForTesting(environment.getEngine());

        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());

        inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        controller = new GridController(environment, grid, inputReactor,
            new MockDroppableGenerator(environment.getEngine()));

        gemsPair = controller.getGemsPair();

        grid.removeDroppableFromGrid(gemsPair.getSlave());
        gemsPair.setNoSlave();
        grid.removeDroppableFromGrid(gemsPair.getPivot());
        gemsPair.setNoPivot();

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
    }


    public void testMirrorSlaveHandlerRepeatDelay()
    {
        MirrorSlaveGemCommandHandler handler = new MirrorSlaveGemCommandHandler(
            new DroppablesPair(grid, environment.getConfig()), 500, 1000);

        assertEquals(1000, handler.getFastRepeatDelay());
        assertEquals(500, handler.getNormalRepeatDelay());
    }


    public void testRotateClockwiseHandlerRepeatDelay()
    {
        RotateClockwiseCommandHandler handler = new RotateClockwiseCommandHandler(
            new DroppablesPair(grid, environment.getConfig()), 500, 1000);

        assertEquals(1000, handler.getFastRepeatDelay());
        assertEquals(500, handler.getNormalRepeatDelay());
    }


    public void testRotateCounterclockwiseHandlerRepeatDelay()
    {
        RotateCounterclockwiseCommandHandler handler = new RotateCounterclockwiseCommandHandler(
            new DroppablesPair(grid, environment.getConfig()), 500, 1000);

        assertEquals(1000, handler.getFastRepeatDelay());
        assertEquals(500, handler.getNormalRepeatDelay());
    }


    public void testCustomRepeatDelay()
    {
        RotateClockwiseCommandHandler handler = new RotateClockwiseCommandHandler(
            new DroppablesPair(grid, environment.getConfig()),
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));
        handler.setFastRepeatDelay(55512);
        handler.setNormalRepeatDelay(2354);

        assertEquals(55512, handler.getFastRepeatDelay());
        assertEquals(2354, handler.getNormalRepeatDelay());
    }


    public void testCurrentRepeatDelayInitialValue()
    {
        EventHandler handler = inputReactor.getEventHandler(Code.LEFT);

        handler.setNormalRepeatDelay(1500);

        assertEquals(1500, handler.getCurrentRepeatDelay());
    }


    public void testCurrentRepeatDelayAfterFirstCommand()
    {
        EventHandler handler = inputReactor.getEventHandler(Code.LEFT);

        handler.setFastRepeatDelay(1000);
        handler.setNormalRepeatDelay(1500);

        generateKeyPressed(Code.LEFT);

        assertEquals(1500, handler.getCurrentRepeatDelay());
    }


    public void testCurrentRepeatDelayAfterSecondCommandRepeat()
    {
        EventHandler handler = inputReactor.getEventHandler(Code.LEFT);

        handler.setFastRepeatDelay(1000);
        handler.setNormalRepeatDelay(1500);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(handler.getCurrentRepeatDelay() + 1);

        inputReactor.reactToInput(environment.getTimer().getTime());

        assertEquals(1000, handler.getCurrentRepeatDelay());
    }


    public void testRotateCounterclockwise()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.BUTTON1);
        generateKeyReleased(Code.BUTTON1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't rotate counterclockwise", grid.getDroppableAt(2,
            3) == gemsPair.getSlave());
    }


    public void testRotateClockwise()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.BUTTON2);
        generateKeyReleased(Code.BUTTON2);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't rotate clockwise",
            grid.getDroppableAt(2, 2) == gemsPair.getSlave());
    }


    public void testRotateMirrorSlaveGem()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.BUTTON3);
        generateKeyReleased(Code.BUTTON3);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't mirrored",
            grid.getDroppableAt(2, 2) == gemsPair.getSlave());
    }


    public void testMoveLeft()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't move to the left", grid.isDroppableAt(2, 3));
    }


    public void testMoveLeftWithRotation()
    {
        setUpRightAndLeftMovementTests();
        gemsPair.rotateCounterclockwise();

        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 3),
            gemsPair.getSlave());
        assertSame("pivotGem's not right to the slave", grid.getDroppableAt(1,
            4), gemsPair.getPivot());

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertSame("slaveGem didn't move to the left",
            grid.getDroppableAt(1, 2), gemsPair.getSlave());

        assertSame("pivotGem didn't move to the left",
            grid.getDroppableAt(1, 3), gemsPair.getPivot());
    }


    public void testMoveRightWithRotation()
    {
        setUpRightAndLeftMovementTests();
        gemsPair.rotateCounterclockwise();
        gemsPair.rotateCounterclockwise();
        gemsPair.rotateCounterclockwise();

        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 5),
            gemsPair.getSlave());

        assertSame("pivotGem's not left to the slave",
            grid.getDroppableAt(1, 4), gemsPair.getPivot());

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertSame("slaveGem didn't move to the left",
            grid.getDroppableAt(1, 6), gemsPair.getSlave());

        assertSame("pivotGem didn't move to the left",
            grid.getDroppableAt(1, 5), gemsPair.getPivot());
    }


    private void setUpRightAndLeftMovementTests()
    {
        if(grid.isDroppableAt(0, 4))
        {
            grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        }

        if(grid.isDroppableAt(1, 4))
        {
            grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));
        }

        controller.insertNewGemsPair();
        gemsPair = controller.getGemsPair();

        assertEquals("slaveGem is not on the top", grid.getDroppableAt(0, 4),
            gemsPair.getSlave());

        assertEquals("pivotGem is not under the slave", grid.getDroppableAt(1,
            4), gemsPair.getPivot());
    }


    public void testMoveLeftWithCollision()
    {
        grid.insertDroppable(gem, 2, 0);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem must not move to the left", grid.isDroppableAt(2, 0));
    }


    public void testMoveRight()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't move to the right", grid.isDroppableAt(2, 5));
    }


    public void testMoveRightWithCollision()
    {
        grid.insertDroppable(gem, 2, GRID_COLUMNS - 1);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem must not move to the right", grid.isDroppableAt(2,
            GRID_COLUMNS - 1));
    }


    public void testRapidInputs()
    {
        try
        {
            grid.insertDroppable(gem, 0, 1);
            generateKeyPressed(Code.RIGHT);
            generateKeyPressed(Code.LEFT);
            inputReactor.reactToInput(environment.getTimer().getTime());
        }
        catch(Exception e)
        {
            fail("Rapid input sequences make the program crash");
        }
    }


    public void testIsNotFalling()
    {
        grid.insertDroppable(gem, GRID_ROWS - 1, 0);
        gemsPair.setPivot(gem);
        grid.updateDroppable(gem);

        assertFalse(gem.getFallingObject().isFalling());
    }


    public void testLeftAndRightBothPressed()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem may not move when Left & Right key are both pressed",
            grid.isDroppableAt(2, 4));
    }


    public void testLeftAndRightSequenceInTwoPollings()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() - 1);

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem may not move when Left & Right key are both pressed",
            grid.isDroppableAt(2, 4));
    }


    public void testLeftStillPressedAndRightPressed()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem may not move when Left key is repeated & Right key is pressed",
            grid.isDroppableAt(2, 3));

    }


    public void testRightStillPressedAndLeftPressed()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem may not move when Right key is repeated & Left key is pressed",
            grid.isDroppableAt(2, 5));
    }


    public void testLeftStillPressedAndRightPressedAfterFirstRepetition()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() / 2);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem may not move when Left key is repeated & Right key is pressed",
            grid.isDroppableAt(2, 2));
    }


    public void testRightStillPressedAndLeftPressedAfterFirstRepetition()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() / 2);
        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem may not move when Left key is repeated & Right key is pressed",
            grid.isDroppableAt(2, 6));
    }


    public void testLeftAndRightStillBothPressed()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        ((MockTimer)environment.getTimer()).setTime(environment.getTimer().getTime()
            + inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem may not move when Left & Right key are both pressed",
            grid.isDroppableAt(2, 4));
    }


    public void testGravityWhileDownKeyIsPressed()
    {
        grid.insertDroppable(gem, 0, 4);
        gemsPair.setPivot(gem);

        float multiplied = grid.getActualGravity()
            * environment.getConfig().getInteger("GravityMultiplier");

        generateKeyPressed(Code.DOWN);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertEquals(multiplied, grid.getActualGravity());

        generateKeyReleased(Code.DOWN);
        inputReactor.reactToInput(environment.getTimer().getTime());
    }


    public void testReactionToDownKey()
    {
        grid.insertDroppable(gem, 2, 4);
        gemsPair.setPivot(gem);

        float oldYPosition = gem.getSprite().getPosition().getY();
        generateKeyPressed(Code.DOWN);

        inputReactor.reactToInput(environment.getTimer().getTime());
        grid.updateDroppable(gemsPair.getPivot());

        float movement = 0.5f * environment.getConfig().getInteger(
            "GravityMultiplier");

        assertEquals("gem doesn't move down as expected", oldYPosition
            + movement, gem.getSprite().getPosition().getY());
    }


    public void updateGridAndGenerateInput(float gravity, Code key)
    {
        gemsPair.setPivot(gem);
        grid.setGravity(gravity);
        grid.updateDroppable(gemsPair.getPivot());

        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());
        generateKeyPressed(key);

        try
        {
            inputReactor.reactToInput(environment.getTimer().getTime());
        }
        catch(IllegalArgumentException e)
        {
            fail("fail due to exception when erroneusly trying to move gem");
        }
    }


    public void testRightCollisionWithGem()
    {
        grid.insertDroppable(gem, 5, 2);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 5,
            3);

        updateGridAndGenerateInput(0.0f, Code.RIGHT);

        assertEquals("gem has moved", 2, gem.getCell().getLeftColumn());
    }


    public void testLeftCollisionWithGem()
    {
        grid.insertDroppable(gem, 5, 2);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 5,
            1);

        updateGridAndGenerateInput(0.0f, Code.LEFT);

        assertEquals("gem has moved", 2, gem.getCell().getLeftColumn());
    }


    public void testLeftCollisionWithGemWhileMoving()
    {
        grid.insertDroppable(gem, 4, 2);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 5,
            1);

        updateGridAndGenerateInput(1.0f, Code.LEFT);

        assertEquals("gem has moved", 2, gem.getCell().getLeftColumn());
    }


    public void testRightCollisionWithGemWhileMoving()
    {
        grid.insertDroppable(gem, 4, 2);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 5,
            3);

        updateGridAndGenerateInput(1.0f, Code.RIGHT);

        assertEquals("gem has moved", 2, gem.getCell().getLeftColumn());
    }


    public void testKeyLeftLessThanDelay()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);

        inputReactor.reactToInput(environment.getTimer().getTime());
        ((MockTimer)environment.getTimer()).setTime(environment.getTimer().getTime()
            + inputReactor.getNormalRepeatDelay() - 1);
        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue(
            "Gem has moved more than once with Left being pressed for less than Delay",
            grid.isDroppableAt(2, 4));
    }


    public void testKeyLeftMoreThanDelay()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem isn't moving according to the correct delay with Left Key being pressed",
            grid.isDroppableAt(2, 3));
    }


    public void testKeyRightLessThanDelay()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);

        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() - 1);
        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue(
            "Gem has moved more than once with Left being pressed for less than Delay",
            grid.isDroppableAt(2, 6));
    }


    public void testKeyRightMoreThanDelay()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem isn't moving according to the correct delay with Right Key being pressed",
            grid.isDroppableAt(2, 7));
    }


    public void testKeyRightNotRepeatedAfterFirstDelay()
    {
        grid.insertDroppable(gem, 2, 3);
        gemsPair.setPivot(gem);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem isn't moving according to the correct delay with Right Key being pressed",
            grid.isDroppableAt(2, 5));
    }


    public void testKeyLeftNotRepeatedAfterFirstDelay()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);
        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(
            "Gem isn't moving according to the correct delay with Right Key being pressed",
            grid.isDroppableAt(2, 3));
    }


    public void testRepeatDelayUpdateOnKeyPression()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        long fastDelay = inputReactor.getEventHandler(Code.RIGHT).getFastRepeatDelay();

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertEquals("UpdateRate wasn't set correctly after a repetition",
            fastDelay,
            inputReactor.getEventHandler(Code.RIGHT).getFastRepeatDelay());
    }


    public void testRepeatDelayUpdateOnKeyRelease()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        long delayBefore = inputReactor.getNormalRepeatDelay();

        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay() + 1);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(1);

        generateKeyReleased(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertEquals(
            "UpdateRate wasn't set back correctly after a key release",
            delayBefore, inputReactor.getNormalRepeatDelay());
    }


    public void testKeyLeftReleased()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        inputReactor.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(1);
        generateKeyReleased(Code.LEFT);
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay());
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't stop moving after Left Key being released",
            grid.isDroppableAt(2, 4));
    }


    public void testKeyRightReleased()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);
        generateKeyPressed(Code.RIGHT);
        inputReactor.reactToInput(environment.getTimer().getTime());
        environment.getTimer().advance(1);
        generateKeyReleased(Code.RIGHT);
        environment.getTimer().advance(inputReactor.getNormalRepeatDelay());
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Gem didn't stop moving after Right Key being released",
            grid.isDroppableAt(2, 6));
    }


    public void testEmptyInputQueue()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        generateKeyReleased(Code.LEFT);
        generateKeyPressed(Code.RIGHT);
        generateKeyReleased(Code.RIGHT);

        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue(input.isEmpty());
    }


    public void testMultipleLeftKeyPressed()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        generateKeyReleased(Code.LEFT);
        generateKeyPressed(Code.LEFT);
        generateKeyReleased(Code.LEFT);

        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue(
            "Gem didn't move twice with Left Key pressed twice by user.",
            grid.isDroppableAt(2, 3));
    }


    public void testMultipleRightKeyPressed()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.RIGHT);
        generateKeyReleased(Code.RIGHT);
        generateKeyPressed(Code.RIGHT);
        generateKeyReleased(Code.RIGHT);

        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue(
            "Gem didn't move twice with Right Key pressed twice by user.",
            grid.isDroppableAt(2, 7));
    }


    public void testRapidSequence()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);

        generateKeyPressed(Code.LEFT);
        generateKeyReleased(Code.LEFT);
        generateKeyPressed(Code.LEFT);
        generateKeyReleased(Code.LEFT);
        generateKeyPressed(Code.RIGHT);
        generateKeyReleased(Code.RIGHT);

        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue("Grid didn't react correctly to fast sequence.",
            grid.isDroppableAt(2, 4));
    }


    public void testReactionWithEmptyQueue()
    {
        grid.insertDroppable(gem, 2, 5);
        gemsPair.setPivot(gem);
        inputReactor.reactToInput(environment.getTimer().getTime());
        assertTrue("Gem moved in response to no input",
            grid.isDroppableAt(2, 5));
    }


    public void testEmptyInputQueuefromInputReactor()
    {
        generateKeyPressed(Code.LEFT);
        inputReactor.emptyQueue();
        assertTrue(input.isEmpty());
    }


    public void testRotateCounterclockwiseOnZ()
    {
        controller.insertNewGemsPair();
        generateKeyPressed(Code.BUTTON1);

        inputReactor.reactToInput(environment.getTimer().getTime());

        assertSame("gems pair doesn't rotate", gemsPair.getSlave(),
            grid.getDroppableAt(1, 3));
    }


    private void generateKeyPressed(Code code)
    {
        input.notify(Event.create(code, State.PRESSED));
    }


    private void generateKeyReleased(Code code)
    {
        input.notify(Event.create(code, State.RELEASED));
    }
}
