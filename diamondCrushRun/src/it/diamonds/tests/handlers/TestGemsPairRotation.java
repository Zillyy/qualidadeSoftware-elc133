package it.diamonds.tests.handlers;


import it.diamonds.droppable.gems.Gem;
import it.diamonds.tests.droppable.AbstractGemsPairTestCase;


public class TestGemsPairRotation extends AbstractGemsPairTestCase
{

    public void testRotationWithDestinationOccupied()
    {
        grid.insertDroppable(Gem.createForTesting(environment.getEngine()), 1,
            5);

        gemsPair.rotateClockwise();

        assertSame("slave must not move", gemsPair.getSlave(),
            grid.getDroppableAt(0, 4));
    }


    public void testGemsPairCanRotateClockwise()
    {
        assertSame("slaveGem should have been up", grid.getDroppableAt(0, 4),
            gemsPair.getSlave());

        gemsPair.rotateClockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 5),
            gemsPair.getSlave());

        gemsPair.rotateClockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(2, 4),
            gemsPair.getSlave());

        gemsPair.rotateClockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 3),
            gemsPair.getSlave());

        gemsPair.rotateClockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(0, 4),
            gemsPair.getSlave());
    }


    public void testGemsPairCanRotateCounterclockwise()
    {
        assertSame("slaveGem should have been up", grid.getDroppableAt(0, 4),
            gemsPair.getSlave());

        gemsPair.rotateCounterclockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 3),
            gemsPair.getSlave());

        gemsPair.rotateCounterclockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(2, 4),
            gemsPair.getSlave());

        gemsPair.rotateCounterclockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(1, 5),
            gemsPair.getSlave());

        gemsPair.rotateCounterclockwise();
        assertSame("slaveGem didn't rotate", grid.getDroppableAt(0, 4),
            gemsPair.getSlave());
    }

}
