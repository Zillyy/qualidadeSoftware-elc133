package it.diamonds.tests.playfield;


import it.diamonds.GameLoop;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.engine.Environment;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;
import it.diamonds.playfield.PlayField;

import java.io.IOException;

import junit.framework.TestCase;


public abstract class AbstractPlayFieldTestCase extends TestCase
{
    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    protected Environment environment;

    protected GridController controller;

    protected Grid grid;

    protected GameLoop loop;

    protected DroppableFactory gemFactory;


    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work

    public void setUp() throws IOException
    {
        environment = Environment.createForTesting(800, 600, "");
        loop = GameLoop.createForTesting(environment);
        PlayField field = loop.getPlayFieldOne();
        controller = field.getGridController();
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
        grid = controller.getGrid();
        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));
        gemFactory = DroppableFactory.createForTesting(environment);
    }

}
