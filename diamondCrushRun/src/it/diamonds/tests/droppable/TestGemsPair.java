package it.diamonds.tests.droppable;


import static it.diamonds.droppable.pair.Direction.GO_LEFT;
import static it.diamonds.droppable.pair.Direction.GO_RIGHT;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.Cell;

import java.io.IOException;


public class TestGemsPair extends AbstractGemsPairTestCase
{
    private Droppable gem;


    public void setUp() throws IOException
    {
        super.setUp();

        gem = Gem.createForTesting(environment.getEngine());
    }


    public void testInsertGemsPair()
    {
        assertTrue(grid.isDroppableAt(0, 4));
        assertTrue(grid.isDroppableAt(1, 4));
    }


    public void testSetGemUnderControl()
    {
        gemsPair = new DroppablesPair(grid, environment.getConfig());
        assertTrue("slaveGem must be null before insertNewGemsPair",
            gemsPair.getPivot() == null);

        grid.insertDroppable(gem, 0, 0);
        gemsPair.setPivot(gem);

        assertSame("Wrong gem under control", gem, gemsPair.getPivot());
    }


    public void testInsertionOnFullColumn()
    {
        grid.removeDroppableFromGrid(gemsPair.getSlave());
        grid.removeDroppableFromGrid(gemsPair.getPivot());

        if(!grid.isDroppableAt(1, 4))
        {
            grid.insertDroppable(gem, 1, 4);
        }

        try
        {
            controller.insertNewGemsPair();
        }
        catch(IllegalArgumentException exceptionFullColumn)
        {
            fail("insertNewGemsPair insertion raised an exception");
        }

        assertNull("slaveGem should be null", gemsPair.getSlave());

        assertTrue("pivotGem should be in (x:4,y:0)",
            (gemsPair.getPivot().getCell().getTopRow() == 0)
                && (gemsPair.getPivot().getCell().getLeftColumn() == 4));

        assertFalse("single gem on top shouldn't be able to move",
            gemsPair.canReactToInput());
    }


    public void testSetNoGemUnderControl()
    {
        grid.insertDroppable(gem, 4, 2);
        gemsPair.setPivot(gem);
        gemsPair.setNoPivot();

        assertNull("gem under control is not null", gemsPair.getPivot());
    }


    public void testGetSlaveGem()
    {
        gemsPair = new DroppablesPair(grid, environment.getConfig());

        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 0,
            1);
        gemsPair.setPivot(grid.getDroppableAt(0, 1));

        assertNull("slaveGem must be null before insertNewGemsPair",
            gemsPair.getSlave());

        grid.insertDroppable(gem, 0, 0);
        gemsPair.setSlave(gem);

        assertNotNull("slaveGem must be not null after insertNewGemsPair",
            gemsPair.getSlave());
    }


    public void testSetNoSlaveGem()
    {
        grid.insertDroppable(gem, 4, 2);
        gemsPair.setSlave(gem);
        gemsPair.setNoSlave();

        assertNull("slaveGem is not null", gemsPair.getSlave());
    }


    public void testGemsPairUnderGravity()
    {
        grid.setGravity(1.0f);

        environment.getTimer().advance(
            10 /* environment.getConfig().getInteger("UpdateRate") */+ 1);
        controller.update(environment.getTimer().getTime());

        assertTrue("slaveGem didn't move correctly after gravity applied",
            grid.isDroppableAt(1, 4));
        assertTrue("pivotGem didn't move correctly after gravity applied",
            grid.isDroppableAt(2, 4));
    }


    public void testGemsPairMoveLeft()
    {
        gemsPair.move(GO_LEFT);

        assertTrue("slaveGem didn't move correctly after moveLeft",
            grid.isDroppableAt(0, 3));
        assertTrue("pivotGem didn't move correctly after moveLeft",
            grid.isDroppableAt(1, 3));
    }


    public void testGemsPairMoveRight()
    {
        gemsPair.move(GO_RIGHT);

        assertTrue("slaveGem didn't move correctly after moveRight",
            grid.isDroppableAt(0, 5));
        assertTrue("pivotGem didn't move correctly after moveRight",
            grid.isDroppableAt(1, 5));
    }


    public void testMoveRightWhileHorizontal()
    {
        controller.getGemsPair().rotateCounterclockwise();
        controller.getGemsPair().move(GO_RIGHT);

        assertTrue("slaveGem didn't move correctly after moveRight",
            grid.isDroppableAt(1, 5));
        assertTrue("pivotGem didn't move correctly after moveRight",
            grid.isDroppableAt(1, 4));

    }


    public void testGemsPairReactingToMoveLeftEvent()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));

        controller.reactToInput(environment.getTimer().getTime());

        assertTrue("slaveGem didn't move correctly after MoveLeft Event",
            grid.isDroppableAt(0, 3));
        assertTrue("pivotGem didn't move correctly after MoveLeft Event",
            grid.isDroppableAt(1, 3));
    }


    public void testGemsPairReactingToMoveRightEvent()
    {
        input.notify(Event.create(Code.RIGHT, State.PRESSED));

        controller.reactToInput(environment.getTimer().getTime());

        assertTrue("slaveGem didn't move correctly after MoveRight Event",
            grid.isDroppableAt(0, 5));

        assertTrue("pivotGem didn't move correctly after MoveRight Event",
            grid.isDroppableAt(1, 5));
    }


    public void testGemsPairDoesntSplitOnCommands()
    {
        Droppable gem1 = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(gem1, 1, 3);

        Droppable gem2 = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(gem2, 1, 5);

        gemsPair.move(GO_LEFT);

        assertEquals("slaveGem must not move left", 4,
            gemsPair.getSlave().getCell().getLeftColumn());

        gemsPair.move(GO_RIGHT);
        assertEquals("slaveGem must not move right", 4,
            gemsPair.getSlave().getCell().getLeftColumn());

        grid.removeDroppableFromGrid(gem1);
        grid.removeDroppableFromGrid(gem2);

        gem1 = Gem.createForTesting(environment.getEngine());
        gem2 = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(gem1, 0, 3);
        grid.insertDroppable(gem2, 0, 5);

        assertEquals("pivotGem must not move left", 4,
            gemsPair.getPivot().getCell().getLeftColumn());

        gemsPair.move(GO_RIGHT);
        assertEquals("pivotGem must not move right", 4,
            gemsPair.getPivot().getCell().getLeftColumn());
    }


    public void testGemsPairUpdate()
    {
        gemsPair.update(environment.getTimer().getTime());

        assertEquals("pivot gem doesn't move", 2,
            gemsPair.getPivot().getCell().getTopRow());
        assertEquals("slave doesn't move", 1,
            gemsPair.getSlave().getCell().getTopRow());
    }


    public void testUpdateWithSlaveUnder()
    {
        grid.removeDroppableFromGrid(gemsPair.getPivot());
        grid.removeDroppableFromGrid(gemsPair.getSlave());

        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 1,
            4);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            4);

        gemsPair.setPivot(grid.getDroppableAt(1, 4));
        gemsPair.setSlave(grid.getDroppableAt(2, 4));

        gemsPair.update(environment.getTimer().getTime());

        assertEquals("pivot gem doesn't move", 2,
            gemsPair.getPivot().getCell().getTopRow());
        assertEquals("slave doesn't move", 3,
            gemsPair.getSlave().getCell().getTopRow());
    }


    public void testUpdateWithNoSlave()
    {
        gemsPair.setNoSlave();

        grid.setGravity(Cell.SIZE);
        gemsPair.update(environment.getTimer().getTime());

        assertEquals("pivot gem doesn't move down", 2,
            gemsPair.getPivot().getCell().getTopRow());
    }


    public void testUpdateWithPivotStopped()
    {
        gemsPair.rotateCounterclockwise();
        gemsPair.getPivot().getFallingObject().drop();

        grid.setGravity(Cell.SIZE);
        gemsPair.update(environment.getTimer().getTime());

        assertEquals("slave gem doesn't move down", 2,
            gemsPair.getSlave().getCell().getTopRow());
    }


    public void testUpdateWithSlaveStopped()
    {
        gemsPair.getSlave().getFallingObject().drop();

        grid.setGravity(Cell.SIZE);
        gemsPair.update(environment.getTimer().getTime());

        assertEquals("pivot gem doesn't move down", 2,
            gemsPair.getPivot().getCell().getTopRow());
    }


    public void testNoReactionToInputWhenPivotStopped()
    {
        gemsPair.rotateCounterclockwise();
        gemsPair.getPivot().getFallingObject().drop();

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());

        assertEquals("slave gem has moved", 3,
            gemsPair.getSlave().getCell().getLeftColumn());
    }


    public void testNoReactionToInputWhenSlaveStopped()
    {
        gemsPair.getSlave().getFallingObject().drop();

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        controller.reactToInput(environment.getTimer().getTime());

        assertEquals("pivot gem has moved", 4,
            gemsPair.getPivot().getCell().getLeftColumn());
    }


    public void testPivotFinishesFallingFast()
    {
        gemsPair.getSlave().getFallingObject().drop();
        controller.update(environment.getTimer().getTime());
        assertEquals(10.0, grid.getActualGravity(), 0.001f);
    }


    public void testSlaveFinishesFallingFast()
    {
        gemsPair.rotateClockwise();
        gemsPair.getPivot().getFallingObject().drop();
        controller.update(environment.getTimer().getTime());
        assertEquals(10.0, grid.getActualGravity(), 0.001f);
    }


    public void testGemsPairToString()
    {
        grid.insertDroppable(gem, 0, 0);

        gemsPair.setPivot(gem);
        gemsPair.setSlave(gem);

        String gemsPairString = "diamondgems diamondgems";

        assertEquals("", gemsPairString, gemsPair.toString());
    }


    public void testGemsPairToStringWhenBothNull()
    {
        try
        {
            grid.insertDroppable(gem, 0, 0);
            gemsPair.setNoSlave();
            gemsPair.setNoPivot();

            gemsPair.toString();
        }
        catch(Exception ex)
        {
            fail("String conversion must work with no pivot Gem created");
        }
    }


    public void testGemsPairToStringWhenPivotNull()
    {
        try
        {
            grid.insertDroppable(gem, 0, 0);
            gemsPair.setSlave(gem);
            gemsPair.setNoPivot();

            gemsPair.toString();
        }
        catch(Exception ex)
        {
            fail("String conversion must work with no pivot Gem created");
        }
    }


    public void testGemsPairToStringWhenSlaveNull()
    {
        try
        {
            grid.insertDroppable(gem, 0, 0);
            gemsPair.setNoSlave();
            gemsPair.setPivot(gem);

            gemsPair.toString();
        }
        catch(Exception ex)
        {
            fail("String conversion must work with no slave Gem created");
        }
    }


    public void testOneGemIsNotFallingWithBothGemsFalling()
    {
        assertFalse(gemsPair.oneDroppableIsNotFalling());
    }


    public void testOneGemIsNotFallingWithSlaveGemNotFalling()
    {
        gemsPair.rotateClockwise();
        gemsPair.getSlave().getFallingObject().drop();

        assertTrue(gemsPair.oneDroppableIsNotFalling());
    }


    public void testOneGemIsNotFallingWithPivotNotFalling()
    {
        gemsPair.rotateClockwise();
        gemsPair.getPivot().getFallingObject().drop();

        assertTrue(gemsPair.oneDroppableIsNotFalling());
    }


    public void testOneGemIsNotFallingWithBothGemsNotFalling()
    {
        gemsPair.getPivot().getFallingObject().drop();
        gemsPair.getSlave().getFallingObject().drop();

        assertFalse(gemsPair.oneDroppableIsNotFalling());
    }


    public void testBothGemsAreNotFallingWithBothGemsFalling()
    {
        assertFalse(gemsPair.bothDroppablesAreNotFalling());
    }


    public void testBothGemsAreNotFallingWithBothGemsNotFalling()
    {
        gemsPair.getPivot().getFallingObject().drop();
        gemsPair.getSlave().getFallingObject().drop();

        assertTrue(gemsPair.bothDroppablesAreNotFalling());
    }


    public void testBothGemsAreNotFallingWithPivotGemFalling()
    {
        gemsPair.rotateClockwise();
        gemsPair.getSlave().getFallingObject().drop();

        assertFalse(gemsPair.bothDroppablesAreNotFalling());
    }


    public void testBothGemsAreNotFallingWithSlaveGemFalling()
    {
        gemsPair.rotateClockwise();
        gemsPair.getPivot().getFallingObject().drop();

        assertFalse(gemsPair.bothDroppablesAreNotFalling());
    }


    public void testSlaveGemFallsFaster()
    {
        final double strongestGravity = environment.getConfig().getInteger(
            "StrongestGravityMultiplier") * 0.5;

        controller.getGemsPair().rotateClockwise();
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.update(environment.getTimer().getTime());

        assertEquals(strongestGravity, grid.getActualGravity(), 0.01);

        input.notify(Event.create(Code.DOWN, State.PRESSED));

        controller.reactToInput(environment.getTimer().getTime());

        assertEquals(strongestGravity, grid.getActualGravity(), 0.01);

        input.notify(Event.create(Code.DOWN, State.RELEASED));

        controller.reactToInput(environment.getTimer().getTime());

        assertEquals(strongestGravity, grid.getActualGravity(), 0.01);
    }
}
