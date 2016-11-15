package it.diamonds.droppable;


import it.diamonds.engine.Environment;
import it.diamonds.engine.RandomGenerator;
import it.diamonds.engine.RandomGeneratorInterface;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.util.LinkedList;


public final class GemQueue implements DroppableGenerator
{
    public static final int MAX_QUEUE_SIZE = 2;

    private LinkedList<Droppable> queue;

    private RandomDroppableFactory randomDroppableFactory;


    private GemQueue()
    {
        queue = new LinkedList<Droppable>();
    }


    public static GemQueue createForTesting(Environment environment,
        MockRandomGenerator randomGenerator)
    {
        GemQueue gemQueue = new GemQueue();
        gemQueue.randomDroppableFactory = RandomDroppableFactory.createForTesting(
            environment, randomGenerator);

        return gemQueue;
    }


    public static GemQueue createForTesting(Environment environment)
    {
        return GemQueue.create(environment, new RandomGenerator());
    }


    public static GemQueue create(Environment environment,
        RandomGeneratorInterface randomGenerator)
    {
        GemQueue gemQueue = new GemQueue();

        gemQueue.randomDroppableFactory = new RandomDroppableFactory(
            environment, randomGenerator);

        gemQueue.fillQueueRandomly();

        return gemQueue;
    }


    private void insertLast(Droppable gem)
    {
        if(queue.size() >= MAX_QUEUE_SIZE)
        {
            throw new IllegalArgumentException();
        }
        queue.addLast(gem);
    }


    private void createOne()
    {
        Droppable drop = randomDroppableFactory.createRandomDroppable();
        insertLast(drop);
    }


    public void fillQueueRandomly()
    {
        while(queue.size() < MAX_QUEUE_SIZE)
        {
            createOne();
        }
    }


    public Droppable extract()
    {
        Droppable droppable = queue.removeFirst();

        fillQueueRandomly();

        return droppable;
    }


    public Droppable getGemAt(int index)
    {
        return queue.get(index);
    }
}
