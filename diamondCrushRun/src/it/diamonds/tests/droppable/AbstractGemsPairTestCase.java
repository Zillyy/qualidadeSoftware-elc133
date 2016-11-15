package it.diamonds.tests.droppable;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.Point;
import it.diamonds.engine.RandomGenerator;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;
import it.diamonds.tests.engine.AbstractEnvironmentTestCase;

import java.io.IOException;


public abstract class AbstractGemsPairTestCase extends
    AbstractEnvironmentTestCase
{
    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    protected GridController controller;

    protected Input input;

    protected Grid grid;

    protected DroppablesPair gemsPair;


    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work

    public void setUp() throws IOException
    {
        super.setUp();

        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());

        InputReactor inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        controller = GridController.create(environment, inputReactor,
            new RandomGenerator(), new Point(0, 0));

        grid = controller.getGrid();
        gemsPair = controller.getGemsPair();

        grid.removeDroppableFromGrid(gemsPair.getPivot());
        grid.removeDroppableFromGrid(gemsPair.getSlave());

        gemsPair.setNoPivot();

        controller.insertNewGemsPair();
    }

}
