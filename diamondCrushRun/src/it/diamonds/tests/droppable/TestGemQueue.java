package it.diamonds.tests.droppable;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.GemQueue;
import it.diamonds.tests.engine.AbstractEnvironmentTestCase;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;
import java.util.NoSuchElementException;


public class TestGemQueue extends AbstractEnvironmentTestCase
{
    private GemQueue queue;

    private MockRandomGenerator mockGenerator;

    private int startGem;

    private int startChest;


    public void setUp() throws IOException
    {
        super.setUp();

        mockGenerator = new MockRandomGenerator();
        queue = GemQueue.createForTesting(environment, mockGenerator);

        int chestProb = environment.getConfig().getInteger("ChestProbability");
        int flashProb = environment.getConfig().getInteger("FlashProbability");

        startGem = flashProb + chestProb;
        startChest = flashProb;
    }


    public void testEmptyQueue()
    {
        try
        {
            queue.extract();
        }
        catch(NoSuchElementException e)
        {
            return;
        }
        fail("queue must be empty");
    }


    public void testFullQueue()
    {
        queue.fillQueueRandomly();
        for(int i = 0; i < GemQueue.MAX_QUEUE_SIZE; i++)
        {
            try
            {
                queue.extract();
            }
            catch(NoSuchElementException e)
            {
                fail("queue not full after creation");
            }
        }
    }


    public void testQueueIsAlwaysFull()
    {
        queue.fillQueueRandomly();
        for(int i = 0; i < GemQueue.MAX_QUEUE_SIZE; i++)
        {
            queue.extract();
        }
        try
        {
            queue.extract();
            queue.extract();
        }
        catch(NoSuchElementException e)
        {
            fail("queue isn't always full");
        }
    }


    public void testExtractedDroppablesAreCorrect()
    {
        int randomSequence[] = { startGem, 1, startChest, 1 };
        mockGenerator.setNumbers(randomSequence);
        queue.fillQueueRandomly();
        assertTrue(queue.extract().getGridObject().getType().isGem());
        assertTrue(queue.extract().getGridObject().getType().isChest());
    }


    public void testGetGemEmptyQueue()
    {
        try
        {
            queue.getGemAt(0);
        }
        catch(Exception e)
        {
            return;
        }
        fail("");
    }


    public void testGetFirstGem()
    {
        queue.fillQueueRandomly();
        Droppable gem = queue.getGemAt(0);
        assertSame(gem, queue.extract());
    }


    public void testGetSecondGem()
    {
        queue.fillQueueRandomly();
        Droppable gem = queue.getGemAt(1);
        queue.extract();
        assertSame(gem, queue.extract());
    }


    public void testGetInvalidGem()
    {
        queue.fillQueueRandomly();
        try
        {
            queue.getGemAt(2);
        }
        catch(IndexOutOfBoundsException e)
        {
            return;
        }
        fail("");
    }

}
