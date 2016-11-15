package it.diamonds.grid;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableGenerator;
import it.diamonds.droppable.GemQueue;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.Environment;
import it.diamonds.engine.Point;
import it.diamonds.engine.RandomGeneratorInterface;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.grid.action.UpdateFallsAction;
import it.diamonds.grid.query.CanMoveDownQuery;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.GemsPairOnControlState;
import it.diamonds.handlers.DropCommandHandler;
import it.diamonds.handlers.MirrorSlaveGemCommandHandler;
import it.diamonds.handlers.MoveLeftCommandHandler;
import it.diamonds.handlers.MoveRightCommandHandler;
import it.diamonds.handlers.RotateClockwiseCommandHandler;
import it.diamonds.handlers.RotateCounterclockwiseCommandHandler;
import it.diamonds.tests.mocks.MockDroppableGenerator;

import java.io.IOException;


public class GridController
{
    private Grid grid;

    private InputReactor inputReactor;

    private DroppableGenerator gemGenerator;

    private DroppablesPair gemsPair;

    private int numberOfPairsInserted = 0;

    private int incomingStones = 0;

    private int stonesToSend = 0;

    private boolean isCounterBoxToShow = false;

    private AbstractControllerState currentState;


    public GridController(Environment environment, Grid grid,
        InputReactor inputReactor, DroppableGenerator gemGenerator)
    {
        this.grid = grid;

        this.gemsPair = new DroppablesPair(grid, environment.getConfig());

        this.inputReactor = inputReactor;
        addGridInputHandlers();

        this.gemGenerator = gemGenerator;

        insertNewGemsPair();

        this.currentState = new GemsPairOnControlState(environment);
    }


    private void addGridInputHandlers()
    {
        inputReactor.addHandler(Code.BUTTON1,
            new RotateCounterclockwiseCommandHandler(gemsPair,
                inputReactor.getNormalRepeatDelay(),
                inputReactor.getFastRepeatDelay()));

        inputReactor.addHandler(Code.BUTTON2,
            new RotateClockwiseCommandHandler(gemsPair,
                inputReactor.getNormalRepeatDelay(),
                inputReactor.getFastRepeatDelay()));

        inputReactor.addHandler(Code.BUTTON3, new MirrorSlaveGemCommandHandler(
            gemsPair, inputReactor.getNormalRepeatDelay(),
            inputReactor.getFastRepeatDelay()));

        inputReactor.addHandler(Code.LEFT, new MoveLeftCommandHandler(gemsPair));
        inputReactor.addHandler(Code.RIGHT, new MoveRightCommandHandler(
            gemsPair));
        inputReactor.addHandler(Code.DOWN, new DropCommandHandler(grid,
            gemsPair));
    }


    public void insertNewGemsPair()
    {
        Droppable gem;

        if(!grid.isDroppableAt(1, 4))
        {
            gem = gemGenerator.extract();
            grid.insertDroppable(gem, 1, 4);
            gemsPair.setPivot(gem);

            gem = gemGenerator.extract();
            grid.insertDroppable(gem, 0, 4);
            gemsPair.setSlave(gem);
        }
        else
        {
            gem = gemGenerator.extract();
            grid.insertDroppable(gem, 0, 4);
            gem.getFallingObject().drop();
            gemsPair.setPivot(gem);
            gemsPair.setNoSlave();
        }

        ++numberOfPairsInserted;
    }


    public boolean isCenterColumnFull()
    {
        return grid.isColumnFull(4);
    }


    public boolean isGameOver()
    {
        return currentState.isCurrentState("GameOver");
    }


    public Grid getGrid()
    {
        return grid;
    }


    public DroppableGenerator getGemGenerator()
    {
        return gemGenerator;
    }


    public void update(long timer)
    {
        currentState = currentState.update(timer, this);
        grid.updateDroppableAnimations(timer);
    }


    public void reactToInput(long timer)
    {
        currentState.reactToInput(inputReactor, timer);
    }


    public DroppablesPair getGemsPair()
    {
        return this.gemsPair;
    }


    public static GridController create(Environment environment,
        InputReactor inputReactor, RandomGeneratorInterface generator,
        Point origin)
    {
        Rectangle rectangle = new Rectangle((int)origin.getX(),
            (int)origin.getY(), (int)origin.getX() + 255,
            (int)origin.getY() + 447);
        Grid grid = new Grid(environment, rectangle);

        GridController controller = new GridController(environment, grid,
            inputReactor, GemQueue.create(environment, generator));

        return controller;
    }


    public static GridController createForTesting(Environment environment)
        throws IOException
    {
        InputReactor inputReactor = new InputReactor(Input.create(
            environment.getKeyboard(), environment.getTimer()),
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        return new GridController(environment,
            Grid.createForTesting(environment), inputReactor,
            new MockDroppableGenerator(environment.getEngine()));
    }


    public int getNumberOfPairInserted()
    {
        return numberOfPairsInserted;
    }


    public void setIncomingStones(int value)
    {
        incomingStones = value;
    }


    public int getIncomingStones()
    {
        return incomingStones;
    }


    public void setStonesToSend(int value)
    {
        stonesToSend = value;
    }


    public int getStonesToSend()
    {
        return stonesToSend;
    }


    public void setCounterBoxVisibility(boolean visibility)
    {
        isCounterBoxToShow = visibility;
    }


    public boolean isCounterBoxToShow()
    {
        return isCounterBoxToShow;
    }


    public boolean droppedGemWithoutGemsPairCanMoveDown()
    {
        CanMoveDownQuery action = new CanMoveDownQuery(getGemsPair());
        getGrid().doQuery(action);
        return action.getResult();
    }


    public void updateFallsWithOutGemsPair()
    {
        getGrid().doAction(new UpdateFallsAction(getGemsPair()));
    }
}
