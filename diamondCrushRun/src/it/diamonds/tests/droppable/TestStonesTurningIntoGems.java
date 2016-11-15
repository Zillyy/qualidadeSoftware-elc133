package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableType.GEM;
import static it.diamonds.droppable.DroppableType.STONE;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.droppable.gems.Stone;
import it.diamonds.engine.TimerInterface;
import it.diamonds.grid.action.TurnStonesIntoGemsAction;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestStonesTurningIntoGems extends AbstractGridTestCase
{
    private TimerInterface timer;

    private TurnStonesIntoGemsAction action;

    private DroppableFactory gemFactory;

    private Droppable stone;


    public void setUp() throws IOException
    {
        super.setUp();

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        grid.removeDroppableFromGrid(controller.getGemsPair().getSlave());

        timer = environment.getTimer();
        action = new TurnStonesIntoGemsAction(environment, grid);
        gemFactory = new DroppableFactory(environment);
        stone = gemFactory.create(STONE, DIAMOND);
        grid.insertDroppable(stone, 13, 5);
    }


    public void testNoFrameChangeBeforeTimerInterval()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        grid.doAction(action);
        assertEquals(5, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameNumberIncremented()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        grid.doAction(action);
        timer.advance(Stone.ANIMATION_FRAME_DELAY);

        /*
         * we need to call the action twice because the first time it won't react due to
         * the fact that it needs to set the lastUpdateTimeStamp field; actually, we just
         * want that the action updates the stone *once*!
         */
        grid.doAction(action);

        assertEquals(6, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testActionActsOnStonesOnly()
    {
        Droppable gem = gemFactory.create(GEM, DIAMOND);
        gem.getAnimatedObject().setCurrentFrame(2);
        grid.insertDroppable(gem, 0, 0);
        timer.advance(100);
        grid.doAction(action);
        assertEquals(2, gem.getAnimatedObject().getCurrentFrame());
    }


    public void testActionActsOnStonesBeyondFourthFrameOnly()
    {
        stone.getAnimatedObject().setCurrentFrame(2);
        timer.advance(100);
        grid.doAction(action);
        assertEquals(2, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testStonesNotReplacedBeforeSeventhFrame()
    {
        stone.getAnimatedObject().setCurrentFrame(6);
        timer.advance(100);
        grid.doAction(action);
        assertSame(grid.getDroppableAt(13, 5), stone);
    }


    public void testStoneReplacedAfterSeventhFrame()
    {
        stone.getAnimatedObject().setCurrentFrame(7);
        timer.advance(100);
        grid.doAction(action);
        assertNotSame(grid.getDroppableAt(4, 5), stone);
    }


    public void testTurningStonesCounterStartsOnZero()
    {
        assertEquals(0, action.getNumberOfTurningStones());
    }


    public void testTurningStonesCounterOnEmptyGrid()
    {
        grid.doAction(action);
        assertEquals(0, action.getNumberOfTurningStones());
    }


    public void testCounterWithoutTurningStones()
    {
        stone.getAnimatedObject().setCurrentFrame(3); // not a turning stone
        grid.doAction(action);
        assertEquals(0, action.getNumberOfTurningStones());
    }


    public void testCounterWithTurningStones()
    {
        stone.getAnimatedObject().setCurrentFrame(6);
        grid.doAction(action);
        assertEquals(1, action.getNumberOfTurningStones());
    }


    public void testCounterWithTurnedStones()
    {
        Droppable turnedStone = gemFactory.create(STONE, DIAMOND);
        turnedStone.getAnimatedObject().setCurrentFrame(7);

        stone.getAnimatedObject().setCurrentFrame(6);
        grid.insertDroppable(turnedStone, 13, 6);

        grid.doAction(action);
        assertEquals(1, action.getNumberOfTurningStones());
    }

}
