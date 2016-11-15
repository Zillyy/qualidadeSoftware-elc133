package it.diamonds.tests.droppable;


import it.diamonds.droppable.AbstractSingleDroppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.tests.mocks.MockEngine;


public class AbstractDroppableForTesting extends AbstractSingleDroppable
{
    public AbstractDroppableForTesting()
    {
        super(new MockEngine(0, 0), DroppableType.GEM, DroppableColor.DIAMOND,
            0);
    }
}
