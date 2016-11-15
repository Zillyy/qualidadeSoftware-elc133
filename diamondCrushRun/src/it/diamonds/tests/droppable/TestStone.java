package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.DroppableColor.SAPPHIRE;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.droppable.gems.Stone;
import it.diamonds.engine.Environment;
import junit.framework.TestCase;


public class TestStone extends TestCase
{
    private DroppableFactory gemFactory;

    private Droppable stone;


    protected void setUp()
    {
        Environment environment = Environment.createForTesting(800, 600, "");

        gemFactory = DroppableFactory.createForTesting(environment);
        stone = gemFactory.createStone(DIAMOND);
    }


    public void testStoneScores()
    {
        assertEquals("Stone's score must be 0", 0, gemFactory.createStone(
            EMERALD).getScore());
        assertEquals("Stone's score must be 0", 0,
            gemFactory.createStone(RUBY).getScore());
        assertEquals("Stone's score must be 0", 0, gemFactory.createStone(
            SAPPHIRE).getScore());
        assertEquals("Stone's score must be 0", 0,
            gemFactory.createStone(TOPAZ).getScore());
        assertEquals("Stone's score must be 0", 0, gemFactory.createStone(
            DIAMOND).getScore());
    }


    public void testStoneNumberOfFrames()
    {
        assertEquals("Stone must have 8 frames", 8,
            stone.getAnimatedObject().getNumberOfFrames());
    }


    public void makeUpdateOnce()
    {
        stone.getAnimatedObject().update(0);
        stone.getAnimatedObject().update(Stone.ANIMATION_FRAME_DELAY);
    }


    public void testNoFrameAdvanceBeforeFifthFrame()
    {
        stone.getAnimatedObject().setCurrentFrame(0);
        makeUpdateOnce();
        assertEquals(0, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameAdvancesBeyondFifthOne()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        makeUpdateOnce();
        assertEquals(6, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testNoUpdateAtFirstTime()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        stone.getAnimatedObject().update(1234);
        assertEquals(5, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testNoFrameChangeBeforeTimerInterval()
    {
        stone.getAnimatedObject().setCurrentFrame(6);
        stone.getAnimatedObject().update(0);
        stone.getAnimatedObject().update(Stone.ANIMATION_FRAME_DELAY / 2);
        assertEquals(6, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameAdvancesWhenTimerIntervalElapses()
    {
        stone.getAnimatedObject().setCurrentFrame(6);
        makeUpdateOnce();
        assertEquals(7, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testNoFrameChangeImmediatelyAfterFirstInterval()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        makeUpdateOnce();
        stone.getAnimatedObject().update(
            Stone.ANIMATION_FRAME_DELAY + Stone.ANIMATION_FRAME_DELAY / 2);
        assertEquals(6, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameAdvancesAtSecondInterval()
    {
        stone.getAnimatedObject().setCurrentFrame(5);
        makeUpdateOnce();
        stone.getAnimatedObject().update(Stone.ANIMATION_FRAME_DELAY * 2);
        assertEquals(7, stone.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameChangesWithinRange()
    {
        stone.getAnimatedObject().setCurrentFrame(7);
        stone.getAnimatedObject().update(Stone.ANIMATION_FRAME_DELAY);
        assertEquals(7, stone.getAnimatedObject().getCurrentFrame());
    }
}
