package it.diamonds.grid.state;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.Pattern;
import it.diamonds.engine.Environment;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;


public class StoneFallState extends AbstractControllerState
{
    private static final int[] STONES_FRAMES_BASED_ON_ROW = { 0, 0, 1, 1, 2, 2,
        2, 3, 3, 3, 4, 4, 4, 4 };

    private Environment environment;

    private Pattern pattern;

    private boolean firstUpdate = true;

    private int stonesToInsert;

    private DroppableFactory factory;

    private DroppableList stones = new DroppableList();

    private int[] heights = new int[8];


    public StoneFallState(Environment environment, Pattern pattern)
    {
        this.environment = environment;
        this.pattern = pattern;
        factory = new DroppableFactory(environment);
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("StoneFall");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        if(firstUpdate)
        {
            firstUpdate(gridController);
            sendStone(gridController);
        }

        if(gridController.isCenterColumnFull())
        {
            return new GameOverState();
        }

        if(stonesToInsert != 0)
        {
            insertStones(gridController);
        }

        if(areAllDropped(stones) && stonesToInsert == 0)
        {
            firstUpdate = true;
            gridController.setIncomingStones(0);
            gridController.getGrid().setNormalGravity();
            return new WaitBeforeNewGemsPairState(environment, timer);
        }
        else
        {
            makeAllDrop(gridController, stones);
            return this;
        }
    }


    private void firstUpdate(GridController gridController)
    {
        gridController.getGrid().setStrongestGravity();

        int stonesToSend = 0;
        int incomingStones = gridController.getIncomingStones();
        int crushedGems = gridController.getGrid().getCrushedGemsCounter();
        gridController.getGrid().resetCrushedGemsCounter();

        if(crushedGems >= 2)
        {
            stonesToSend = crushedGems;
        }

        stonesToInsert = incomingStones - stonesToSend;

        if(incomingStones != 0 && stonesToInsert < incomingStones)
        {
            gridController.setCounterBoxVisibility(true);
        }

        stones.clear();

        registerHeightOfColumns(gridController.getGrid());

        gridController.getGrid().setStrongestGravity();
        firstUpdate = false;
    }


    private void sendStone(GridController gridController)
    {
        if(stonesToInsert <= 0)
        {
            gridController.setStonesToSend(Math.abs(stonesToInsert));
            stonesToInsert = 0;
        }
    }


    private void registerHeightOfColumns(Grid grid)
    {
        for(int i = 0; i < grid.getNumberOfColumns(); i++)
        {
            heights[i] = grid.getHeightOfColumn(i);
        }
    }


    private void insertStones(GridController controller)
    {
        int insertedStones = 0;

        controller.getGrid().setStrongestGravity();

        for(int i = 0; i < stonesToInsert; i++)
        {
            int column = getLessFullColumn(controller.getGrid());

            if(isColumnFreeForFalling(controller, column))
            {
                Droppable stone = factory.createStone(pattern.getDroppableColor(column));

                int height = heights[column];
                stone.getAnimatedObject().setCurrentFrame(
                    STONES_FRAMES_BASED_ON_ROW[height]);

                controller.getGrid().insertDroppable(stone, 0, column);

                heights[column]++;
                insertedStones++;
                stones.add(stone);
            }
        }

        stonesToInsert -= insertedStones;
    }


    private int getLessFullColumn(Grid grid)
    {
        int height = grid.getNumberOfRows();
        int result = 0;

        int columns = grid.getNumberOfColumns();

        for(int column = 0; column < columns; column++)
        {
            if(heights[column] < height)
            {
                height = heights[column];
                result = column;
            }
        }

        return result;
    }


    private boolean isColumnFreeForFalling(GridController controller, int column)
    {
        if(controller.getGrid().isDroppableAt(0, column))
        {
            return false;
        }

        if(controller.getGrid().isDroppableAt(1, column))
        {
            Droppable gem = controller.getGrid().getDroppableAt(1, column);
            if(gem.getFallingObject().isFalling())
            {
                return false;
            }
        }
        return true;
    }


    private boolean areAllDropped(DroppableList stones)
    {
        boolean result = true;
        for(Droppable stone : stones)
        {
            result &= !stone.getFallingObject().isFalling();
        }
        return result;
    }


    private void makeAllDrop(GridController controller, DroppableList stones)
    {
        for(Droppable stone : stones)
        {
            controller.getGrid().updateDroppable(stone);
        }
    }

}
