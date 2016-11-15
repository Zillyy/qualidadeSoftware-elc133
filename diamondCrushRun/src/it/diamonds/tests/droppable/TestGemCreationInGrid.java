package it.diamonds.tests.droppable;


import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.grid.GridController;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockDroppableGenerator;

import java.io.IOException;


public class TestGemCreationInGrid extends AbstractGridTestCase
{
    public void setUp() throws IOException
    {
        super.setUp();

        InputReactor inputReactor = new InputReactor(Input.create(
            environment.getKeyboard(), environment.getTimer()),
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        /* TODO: check if reinitialization of GridController can be eliminated */
        controller = new GridController(environment, grid, inputReactor,
            new MockDroppableGenerator(environment.getEngine()));
    }


    public void testGemInsertion()
    {
        assertNotNull("no gem inserted in grid after creation",
            controller.getGemsPair().getPivot());
    }
}
