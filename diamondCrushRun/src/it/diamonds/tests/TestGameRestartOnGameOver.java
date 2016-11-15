package it.diamonds.tests;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.GameLoop;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.playfield.PlayField;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestGameRestartOnGameOver extends AbstractGridTestCase
{
    private int newGemDelay;

    private GameLoop loop;

    private PlayField field1;

    private PlayField field2;

    private int restartGameDelay;


    public void setUp() throws IOException
    {
        super.setUp();

        loop = GameLoop.createForTesting(environment);
        field1 = loop.getPlayFieldOne();
        field2 = loop.getPlayFieldTwo();
        controller = field1.getGridController();
        grid = controller.getGrid();
        setDiamondsGemsPair(grid, controller.getGemsPair());

        this.newGemDelay = environment.getConfig().getInteger("NewGemDelay");
        this.updateRate = environment.getConfig().getInteger("UpdateRate");
        this.restartGameDelay = environment.getConfig().getInteger(
            "RestartGameDelay");
        ;
    }


    public void testPassToGameOverState() throws IOException
    {
        fillFourthColumn();

        environment.getTimer().advance(newGemDelay);
        loop.doOneStep();

        // verifico che il restart nn sia ancora stato fatto
        assertEquals(field1, loop.getPlayFieldOne());
        assertEquals(field2, loop.getPlayFieldTwo());

        environment.getTimer().advance(restartGameDelay - 2);
        loop.doOneStep();

        // verifico che il restart nn sia ancora stato fatto
        assertEquals(field1, loop.getPlayFieldOne());
        assertEquals(field2, loop.getPlayFieldTwo());

        environment.getTimer().advance(1);
        loop.doOneStep();

        // TODO manca test che � stato settato il background(come si fa?)
        assertNotSame("field1 must be different from the old one", field1,
            loop.getPlayFieldOne());
        assertNotSame("field2 must be different from the old one", field2,
            loop.getPlayFieldTwo());
    }


    public void testNotMoveRightOnRestartWaitState() throws IOException
    {
        Droppable slaveGem;
        Droppable pivotGem;

        fillFourthColumn();

        loop.getPlayerTwoInput().notify(Event.create(Code.RIGHT, State.PRESSED));
        loop.getPlayerTwoInput().notify(Event.create(Code.DOWN, State.PRESSED));
        loop.getPlayerTwoInput().notify(
            Event.create(Code.BUTTON1, State.PRESSED));

        environment.getTimer().advance(newGemDelay);

        slaveGem = field2.getGridController().getGemsPair().getSlave();
        int slaveRow = slaveGem.getCell().getTopRow();
        int slaveColumn = slaveGem.getCell().getLeftColumn();

        pivotGem = field2.getGridController().getGemsPair().getPivot();
        int pivotRow = pivotGem.getCell().getTopRow();
        int pivotColumn = pivotGem.getCell().getLeftColumn();

        environment.getTimer().advance(restartGameDelay - 1);
        loop.doOneStep();

        assertEquals(
            field2.getGridController().getGemsPair().getSlave().getCell().getLeftColumn(),
            slaveColumn);
        // slaveRow +1 perche la gravit� sulla gemsPair viene testata prima del check
        // del
        // gameOver(in gridController.update)
        assertEquals(
            field2.getGridController().getGemsPair().getSlave().getCell().getTopRow(),
            slaveRow + 1);

        assertEquals(
            field2.getGridController().getGemsPair().getPivot().getCell().getLeftColumn(),
            pivotColumn);
        // slaveRow +1 perche la gravit� sulla gemsPair viene testata prima del check
        // del
        // gameOver(in gridController.update)
        assertEquals(
            field2.getGridController().getGemsPair().getPivot().getCell().getTopRow(),
            pivotRow + 1);

    }


    public void testControllerDontReactForKeyPressedOnDelay()
        throws IOException
    {
        int inputRate = environment.getConfig().getInteger("InputRate");

        fillFourthColumn();

        loop.getPlayerTwoInput().notify(Event.create(Code.RIGHT, State.PRESSED));
        loop.getPlayerTwoInput().notify(Event.create(Code.DOWN, State.PRESSED));
        loop.getPlayerTwoInput().notify(
            Event.create(Code.BUTTON1, State.PRESSED));

        environment.getTimer().advance(newGemDelay);
        loop.doOneStep();

        environment.getTimer().advance(restartGameDelay);
        loop.doOneStep();

        environment.getTimer().advance(inputRate);
        loop.doOneStep();

        DroppablesPair newGemsPairOne = loop.getPlayFieldOne().getGridController().getGemsPair();
        DroppablesPair newGemsPairTwo = loop.getPlayFieldTwo().getGridController().getGemsPair();

        assertEquals("New pivot of field One is misplaced", 4,
            newGemsPairOne.getPivot().getCell().getLeftColumn());
        assertEquals("New pivot of field One is misplaced", 2,
            newGemsPairOne.getPivot().getCell().getTopRow());

        assertEquals("New slave of field One is misplaced", 4,
            newGemsPairOne.getSlave().getCell().getLeftColumn());
        assertEquals("New slave of field One is misplaced", 1,
            newGemsPairOne.getSlave().getCell().getTopRow());

        assertEquals("New pivot of field Two is misplaced", 4,
            newGemsPairTwo.getPivot().getCell().getLeftColumn());
        assertEquals("New pivot of field Two is misplaced", 2,
            newGemsPairTwo.getPivot().getCell().getTopRow());

        assertEquals("New slave of field Two is misplaced", 4,
            newGemsPairTwo.getSlave().getCell().getLeftColumn());
        assertEquals("New slave of field Two is misplaced", 1,
            newGemsPairTwo.getSlave().getCell().getTopRow());

    }


    private void fillFourthColumn()
    {
        makeAllGemsFall();
        for(int row = 11; row >= 0; row--)
        {
            insertAndUpdate(createGem(DIAMOND), row, 4);
        }
    }

}
