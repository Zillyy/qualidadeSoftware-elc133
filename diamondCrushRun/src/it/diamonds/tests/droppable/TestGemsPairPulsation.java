package it.diamonds.tests.droppable;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;


public class TestGemsPairPulsation extends AbstractGemsPairTestCase
{
    public void testWithPivotAndSlaveGems()
    {
        assertTrue("Pivot and slave gems aren't set-up", gemsPair.isPulsing());
    }


    public void testNoSlaveGem()
    {
        gemsPair.setNoSlave();
        assertFalse("Slave gem was set-up", gemsPair.isPulsing());
    }


    public void testNoPivotGem()
    {
        gemsPair.setNoPivot();
        assertFalse("Pivot gem was set-up", gemsPair.isPulsing());
    }


    public void testPivotIsNotFalling()
    {
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            4);
        controller.update(environment.getTimer().getTime());
        assertFalse("Pivot gem was falling down", gemsPair.isPulsing());
    }


    public void testSlaveIsNotFalling()
    {
        Droppable gem = Gem.createForTesting(environment.getEngine());
        grid.insertDroppable(gem, 1, 3);
        gemsPair.setPivot(gem);
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 2,
            3);
        controller.update(environment.getTimer().getTime());
        assertFalse("Pivot gem was falling down", gemsPair.isPulsing());
    }


    public void testPivotGemPulsingWhenGemsPairPulsing()
    {
        assertEquals("Pivot and GemsPair aren't pulsing together",
            gemsPair.isPulsing(),
            gemsPair.getPivot().getSprite().getDrawModifier() != null);
    }
}
