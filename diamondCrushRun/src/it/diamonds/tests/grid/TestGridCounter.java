package it.diamonds.tests.grid;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;


public class TestGridCounter extends AbstractGridTestCase
{
    public void testCounterAfterDiamondInsertion()
    {
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 1,
            2);
        assertEquals(1, grid.getNumberOfDroppables());
    }


    public void testCounterAfterDiamondRemotion()
    {
        Droppable gem = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(gem, 1, 2);
        grid.removeDroppableFromGrid(gem);
        assertEquals(0, grid.getNumberOfDroppables());
    }


    public void testCounterAfterTwoDiamondInsertions()
    {
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 1,
            2);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 3,
            0);
        assertEquals(2, grid.getNumberOfDroppables());
    }
}
