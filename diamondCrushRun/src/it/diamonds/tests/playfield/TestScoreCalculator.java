package it.diamonds.tests.playfield;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.playfield.ScoreCalculator;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestScoreCalculator extends TestCase
{
    private static final int BONUS = 100;

    private ScoreCalculator calculator;

    private Droppable diamond;

    private Droppable emerald;


    public void setUp()
    {
        AbstractEngine engine = new MockEngine(800, 600);

        diamond = Gem.create(engine, DroppableColor.DIAMOND, 0);
        emerald = Gem.create(engine, DroppableColor.EMERALD, 0);

        calculator = new ScoreCalculator(BONUS);
    }


    public void testInitialScore()
    {
        assertEquals("initial score must be zero", 0, calculator.getScore());
    }


    public void testOneScore()
    {
        calculator.addScoreForGem(diamond);
        calculator.closeChain(1);
        assertEquals(diamond.getScore(), calculator.getScore());
    }


    public void testAnotherScore()
    {
        calculator.addScoreForGem(emerald);
        calculator.closeChain(1);
        assertEquals(emerald.getScore(), calculator.getScore());
    }


    public void testTwoScores()
    {
        calculator.addScoreForGem(diamond);
        calculator.addScoreForGem(emerald);
        calculator.closeChain(1);
        assertEquals(diamond.getScore() + emerald.getScore(),
            calculator.getScore());
    }


    public void testMultipliedScore()
    {
        final int multiplier = 2;
        calculator.addScoreForGem(diamond);
        calculator.closeChain(multiplier);
        assertEquals(diamond.getScore() * multiplier, calculator.getScore());
    }


    public void testIncrementalScore()
    {
        calculator.addScoreForGem(diamond);
        calculator.closeChain(1);

        calculator.addScoreForGem(emerald);
        calculator.closeChain(1);

        assertEquals(diamond.getScore() + emerald.getScore(),
            calculator.getScore());
    }


    public void testIncrementalAndMultipliedScore()
    {
        final int multiplier = 2;

        calculator.addScoreForGem(diamond);
        calculator.closeChain(multiplier);

        calculator.addScoreForGem(emerald);
        calculator.closeChain(multiplier);

        assertEquals(diamond.getScore() * multiplier + emerald.getScore()
            * multiplier, calculator.getScore());
    }

}
