package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.SAPPHIRE;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.droppable.DroppableType;
import it.diamonds.grid.action.UpdateStoneAction;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestStoneUpdate extends AbstractGridTestCase
{
    private UpdateStoneAction updateStoneAction;

    private DroppableFactory gemFactory;


    public void setUp() throws IOException
    {
        super.setUp();

        updateStoneAction = new UpdateStoneAction();
        gemFactory = new DroppableFactory(environment);
    }


    public void testStoneIncrement()
    {
        Droppable gem = createStone(SAPPHIRE);
        gem.getAnimatedObject().createAnimationSequence(0);
        gem.getAnimatedObject().setCurrentFrame(0);
        grid.insertDroppable(gem, 0, 0);

        grid.doAction(updateStoneAction);

        assertEquals("Stone not updated correctly", 1,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneIncrementTwice()
    {
        Droppable gem = createStone(SAPPHIRE);
        gem.getAnimatedObject().createAnimationSequence(0);
        gem.getAnimatedObject().setCurrentFrame(1);
        grid.insertDroppable(gem, 0, 0);

        grid.doAction(updateStoneAction);

        assertEquals("Stone non updated correctly", 2,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testNotStoneNotIncremented()
    {
        Droppable gem = createGem(SAPPHIRE);
        gem.getAnimatedObject().createAnimationSequence(0);
        gem.getAnimatedObject().setCurrentFrame(0);
        grid.insertDroppable(gem, 0, 0);

        grid.doAction(updateStoneAction);

        assertEquals("Stone non updated correctly", 0,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testStoneIncrementOnInsertGemsPair()
    {
        insertAndUpdate(gemFactory.createStone(SAPPHIRE), 13, 0);
        assertEquals(0,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        insertAndDropGemsPair();

        advanceToNextInsert();
        assertEquals(1,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        advanceToNextInsert();
        assertEquals(2,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        advanceToNextInsert();
        assertEquals(3,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        advanceToNextInsert();
        assertEquals(4,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());
    }


    public void testStoneNotIncrementAfterCrush()
    {
        insertAndUpdate(gemFactory.createStone(SAPPHIRE), 13, 0);
        assertEquals(0,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        controller.insertNewGemsPair();
        insertAndUpdate(gemFactory.create(DroppableType.GEM, DIAMOND), 12, 2);
        insertAndUpdate(gemFactory.create(DroppableType.CHEST, DIAMOND), 11, 2);

        while(droppedGemCanMoveDown(grid))
        {
            controller.update(environment.getTimer().getTime());
        }

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(
            environment.getConfig().getInteger("DelayBetweenCrushes"));
        controller.update(environment.getTimer().getTime());

        assertEquals("The stone frame must not be incremented after a crush",
            1, grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());
    }


    /*
     * public void testStoneIncrementAndBigGemOnInsertGemsPair() { Droppable[] stones =
     * new Stone[4]; for(int i = 0; i < 4; i++) { stones[i] =
     * gemFactory.createStone(SAPPHIRE); stones[i].setCurrentFrame(4); }
     * insertAndUpdate(stones[0], 13, 0); insertAndUpdate(stones[1], 12, 0);
     * insertAndUpdate(stones[2], 13, 1); insertAndUpdate(stones[3], 12, 1);
     * insertAndStopGemsPair(); advanceToNextInsert(); assertNotNull("The gems from stones
     * must generate a BigGem", grid.getBigGemAt(13, 0)); }
     */

    public void advanceToNextInsert()
    {
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(
            environment.getConfig().getInteger("NewGemDelay"));
        controller.update(environment.getTimer().getTime());

        assertNotNull(grid.getDroppableAt(0, 4));
        assertNotNull(grid.getDroppableAt(1, 4));

        grid.removeDroppableFromGrid(grid.getDroppableAt(13, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(12, 4));
    }


    public void testStoneIncrementTiming()
    {
        insertAndUpdate(gemFactory.createStone(SAPPHIRE), 13, 0);
        assertEquals(0,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        insertAndDropGemsPair();
        makeAllGemsFall();
        controller.update(environment.getTimer().getTime());
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        controller.update(environment.getTimer().getTime());

        assertEquals(1,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());

        environment.getTimer().advance(
            environment.getConfig().getInteger("NewGemDelay"));
        controller.update(environment.getTimer().getTime());

        assertNotNull(grid.getDroppableAt(0, 4));
        assertNotNull(grid.getDroppableAt(1, 4));

        grid.removeDroppableFromGrid(grid.getDroppableAt(13, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(12, 4));
        assertEquals(1,
            grid.getDroppableAt(13, 0).getAnimatedObject().getCurrentFrame());
    }


    public void testExStonePosition()
    {
        Droppable stone = gemFactory.createStone(DroppableColor.EMERALD);
        grid.insertDroppable(stone, 5, 6);
        stone.getAnimatedObject().setCurrentFrame(6);

        grid.doAction(updateStoneAction);

        Droppable gem = grid.getDroppableAt(5, 6);
        assertEquals(232.0f, gem.getSprite().getPosition().getX());
        assertEquals(200.0f, gem.getSprite().getPosition().getY());
    }

}
