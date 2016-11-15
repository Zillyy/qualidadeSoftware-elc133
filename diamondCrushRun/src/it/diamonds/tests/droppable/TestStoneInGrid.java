package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.tests.grid.AbstractGridTestCase;


public class TestStoneInGrid extends AbstractGridTestCase
{
    public void testStoneIsNotCrushingOnFlash()
    {
        insertAndUpdate(createStone(DIAMOND), 13, 0);
        insertAndUpdate(createFlashingGem(), 13, 1);

        grid.updateCrushes();

        assertEquals("Grid must contain 2 elements", 2,
            grid.getNumberOfDroppables());
    }


    public void testStoneIsNotAnimated()
    {
        DroppableFactory gemFactory = DroppableFactory.createForTesting(environment);

        Droppable stone = gemFactory.createStone(EMERALD);

        grid.insertDroppable(stone, 13, 2);

        grid.updateDroppableAnimations(environment.getTimer().getTime());
        int startingFrame = stone.getAnimatedObject().getCurrentFrame();

        environment.getTimer().advance(
            environment.getConfig().getInteger("GemAnimationUpdateRate") + 1);
        grid.updateDroppableAnimations(environment.getTimer().getTime());

        assertEquals("Stone must not be animated", startingFrame,
            stone.getAnimatedObject().getCurrentFrame());
    }


    public void testNotFormingBigGem()
    {
        insertAndUpdate(createStone(EMERALD), 13, 4);
        insertAndUpdate(createStone(EMERALD), 12, 4);
        insertAndUpdate(createStone(EMERALD), 13, 3);
        insertAndUpdate(createStone(EMERALD), 12, 3);

        grid.updateBigGems();

        assertEquals(0, getNumberOfExtensibleObject());
    }
}
