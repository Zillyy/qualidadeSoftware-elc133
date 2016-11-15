package it.diamonds.tests.mocks;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableGenerator;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.video.AbstractEngine;


public class MockDroppableGenerator implements DroppableGenerator
{
    private AbstractEngine engine;


    public MockDroppableGenerator(AbstractEngine engine)
    {
        this.engine = engine;
    }


    public Droppable extract()
    {
        return Gem.createForTesting(engine);
    }


    public Droppable getGemAt(int index)
    {
        return Gem.createForTesting(engine);
    }
}
