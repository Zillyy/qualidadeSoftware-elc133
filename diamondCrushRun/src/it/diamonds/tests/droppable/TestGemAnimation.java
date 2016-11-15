package it.diamonds.tests.droppable;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.TimerInterface;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;

import java.io.IOException;


public class TestGemAnimation extends AbstractGridTestCase
{
    private TimerInterface timer;

    private Droppable gem;


    public void setUp() throws IOException
    {
        super.setUp();
        timer = environment.getTimer();
        gem = Gem.createForTesting(environment.getEngine());
    }


    public void testNewGemHasNoFrames()
    {
        assertEquals("A new gem must have no frames", 1,
            gem.getAnimatedObject().getNumberOfFrames());
    }


    public void testAddingOneFrame()
    {
        gem.getAnimatedObject().addFrame(10, 10, 100);

        assertEquals("gem must have two frames", 2,
            gem.getAnimatedObject().getNumberOfFrames());
    }


    public void testAddingTwoFrames()
    {
        gem.getAnimatedObject().addFrame(10, 10, 100);
        gem.getAnimatedObject().addFrame(10, 20, 100);

        assertEquals("gem must have three frames", 3,
            gem.getAnimatedObject().getNumberOfFrames());
    }


    public void testCurrentFrame()
    {
        assertEquals("current frame must be 0", 0,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testUpdate()
    {
        gem.getAnimatedObject().addFrame(10, 10, 100);

        timer.advance(3500);
        gem.getAnimatedObject().update(timer.getTime());

        assertEquals("current frame must be 1 after an update", 1,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testUpdateWithTimer()
    {
        gem.getAnimatedObject().addFrame(10, 10, 1000);

        gem.getAnimatedObject().update(timer.getTime());
        assertEquals("current frame must be 0 after 0 milliseconds", 0,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testCyclingAnimation()
    {
        gem.getAnimatedObject().addFrame(10, 10, 1000);

        gem.getAnimatedObject().update(timer.getTime());
        timer.advance(3500 + 1000);
        gem.getAnimatedObject().update(timer.getTime());

        assertEquals("current frame must be 0 after a complete cycle", 0,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testUpdateWithThreeFrames()
    {
        gem.getAnimatedObject().addFrame(10, 10, 100);
        gem.getAnimatedObject().addFrame(20, 20, 100);

        timer.advance(3500);

        gem.getAnimatedObject().update(timer.getTime());

        timer.advance(1);
        gem.getAnimatedObject().update(timer.getTime());

        assertEquals("current frame must be 1", 1,
            gem.getAnimatedObject().getCurrentFrame());

        timer.advance(99);
        gem.getAnimatedObject().update(timer.getTime());

        assertEquals("current frame must be 2", 2,
            gem.getAnimatedObject().getCurrentFrame());

    }


    public void testOneUpdateForMultipleFrames()
    {
        gem.getAnimatedObject().addFrame(10, 10, 100);
        gem.getAnimatedObject().addFrame(20, 20, 100);

        timer.advance(3600);
        gem.getAnimatedObject().update(timer.getTime());

        assertEquals("current frame must be 2 after one update", 2,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testFrameLength()
    {
        gem.getAnimatedObject().addFrame(10, 10, 1000);

        assertEquals("frame length is wrong", 1000,
            gem.getAnimatedObject().getFrameDuration(1));
    }


    public void testTwoFrameLengths()
    {
        gem.getAnimatedObject().addFrame(10, 10, 1000);
        gem.getAnimatedObject().addFrame(10, 20, 100);

        assertEquals("frame one length is wrong", 1000,
            gem.getAnimatedObject().getFrameDuration(1));
        assertEquals("frame two length is wrong", 100,
            gem.getAnimatedObject().getFrameDuration(2));
    }


    public void testDrawTwoFrames()
    {
        timer.advance(3500);
        gem.getAnimatedObject().addFrame(10, 15, 100);
        gem.getAnimatedObject().addFrame(20, 25, 100);

        gem.getAnimatedObject().update(timer.getTime());
        gem.getSprite().draw(environment.getEngine());

        assertEquals("bad texture drawn", 10,
            ((MockEngine)environment.getEngine()).getImageRect().left());
        assertEquals("bad texture drawn", 15,
            ((MockEngine)environment.getEngine()).getImageRect().top());

        timer.advance(100);
        gem.getAnimatedObject().update(timer.getTime());
        gem.getSprite().draw(environment.getEngine());

        assertEquals("bad texture drawn", 20,
            ((MockEngine)environment.getEngine()).getImageRect().left());
        assertEquals("bad texture drawn", 25,
            ((MockEngine)environment.getEngine()).getImageRect().top());
    }


    public void testBrightFramesWithAnimation()
    {
        timer.advance(3500);
        gem.getAnimatedObject().addFrame(10, 15, 100);
        gem.getAnimatedObject().addFrame(20, 25, 100);

        gem.getSprite().useBrighterImage();

        gem.getAnimatedObject().update(timer.getTime());
        gem.getSprite().draw(environment.getEngine());

        assertEquals("bad texture drawn", 42,
            ((MockEngine)environment.getEngine()).getImageRect().left());
        assertEquals("bad texture drawn", 15,
            ((MockEngine)environment.getEngine()).getImageRect().top());

        gem.getSprite().useNormalImage();

        timer.advance(100);
        gem.getAnimatedObject().update(timer.getTime());
        gem.getSprite().draw(environment.getEngine());

        assertEquals("bad texture drawn", 20,
            ((MockEngine)environment.getEngine()).getImageRect().left());
        assertEquals("bad texture drawn", 25,
            ((MockEngine)environment.getEngine()).getImageRect().top());
    }


    public void testGridUpdatesAnimations()
    {
        timer.advance(3500);
        grid.insertDroppable(gem, 1, 1);

        gem.getAnimatedObject().addFrame(10, 15, 100);
        gem.getAnimatedObject().addFrame(10, 20, 100);

        grid.updateDroppableAnimations(timer.getTime());

        assertEquals("Gem animation hasn't been updated", 1,
            gem.getAnimatedObject().getCurrentFrame());

        timer.advance(100);
        grid.updateDroppableAnimations(timer.getTime());

        assertEquals("Gem animation hasn't been updated", 2,
            gem.getAnimatedObject().getCurrentFrame());
    }


    public void testCreateAnimationSequence()
    {
        timer.advance(3450 + 100 * 5);
        gem.getAnimatedObject().createAnimationSequence(100);

        gem.getAnimatedObject().update(timer.getTime());
        gem.getSprite().draw(environment.getEngine());

        assertEquals("Last frame of the sequence is wrong", 32 * 5,
            ((MockEngine)environment.getEngine()).getImageRect().top());
    }
}
