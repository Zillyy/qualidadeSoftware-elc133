package it.diamonds.tests.grid;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.gems.Chest;
import it.diamonds.droppable.gems.FlashingGem;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.droppable.gems.Stone;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;
import it.diamonds.grid.action.UpdateFallsAction;
import it.diamonds.grid.query.CanMoveDownQuery;
import it.diamonds.tests.engine.AbstractEnvironmentTestCase;

import java.io.IOException;


public abstract class AbstractGridTestCase extends AbstractEnvironmentTestCase
{
    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    protected Grid grid;

    protected GridController controller;

    protected int updateRate;


    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work

    public void setUp() throws IOException
    {
        super.setUp();

        updateRate = environment.getConfig().getInteger("UpdateRate");

        controller = GridController.createForTesting(environment);
        grid = controller.getGrid();

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        grid.removeDroppableFromGrid(controller.getGemsPair().getSlave());
    }


    protected void insertAndUpdate(Droppable gem, int row, int column)
    {
        grid.insertDroppable(gem, row, column);
        grid.updateDroppable(gem);
    }


    protected FlashingGem createFlashingGem()
    {
        return FlashingGem.create(environment.getEngine(), 1);
    }


    protected Stone createStone(DroppableColor color)
    {
        return Stone.create(environment.getEngine(), color);
    }


    protected Chest createChest(DroppableColor color)
    {
        return Chest.create(environment.getEngine(), color, 3500);
    }


    protected Gem createGem(DroppableColor color)
    {
        return Gem.create(environment.getEngine(), color, 3500);
    }


    protected void insertAndDropGemsPair()
    {
        controller.insertNewGemsPair();
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
    }


    protected void makeAllGemsFall()
    {
        while(droppedGemCanMoveDown(grid))
        {
            updateFalls(grid);
        }
        controller.update(environment.getTimer().getTime());
    }


    protected void updateFalls(Grid grid)
    {
        grid.doAction(new UpdateFallsAction(null));
    }


    protected boolean droppedGemCanMoveDown(Grid grid)
    {
        CanMoveDownQuery action = new CanMoveDownQuery(null);

        grid.doQuery(action);

        return action.getResult();
    }


    protected void insertBigGem(DroppableColor color, int startRow,
        int startColumn, int endRow, int endColumn)
    {
        for(int i = endRow; i >= startRow; i--)
        {
            for(int j = startColumn; j <= endColumn; j++)
            {
                Gem gem = createGem(color);
                grid.insertDroppable(gem, i, j);
                gem.drop();
                grid.updateDroppable(gem);
            }
        }
    }


    protected void insert2x2BlockOfGems(DroppableColor color, int row,
        int column)
    {
        insertBigGem(color, row, column, row + 1, column + 1);
    }


    protected void insert2x3BlockOfGems(DroppableColor color, int row,
        int column)
    {
        insertBigGem(color, row, column, row + 2, column + 1);
    }


    protected void setDiamondsGemsPair(Grid grid, DroppablesPair gemsPair)
    {
        grid.removeDroppableFromGrid(gemsPair.getPivot());
        grid.removeDroppableFromGrid(gemsPair.getSlave());

        Droppable droppable = createGem(DIAMOND);
        grid.insertDroppable(droppable, 0, 4);
        gemsPair.setSlave(droppable);

        droppable = createGem(DIAMOND);
        grid.insertDroppable(droppable, 1, 4);
        gemsPair.setPivot(droppable);

    }


    protected int getNumberOfExtensibleObject()
    {
        DroppableList extensibleObjects = new DroppableList();

        for(int row = 0; row < grid.getNumberOfRows(); row++)
        {
            for(int column = 0; column < grid.getNumberOfColumns(); column++)
            {
                Droppable droppable = grid.getDroppableAt(row, column);

                if(droppable == null)
                {
                    continue;
                }

                if(droppable.getExtensibleObject() == null)
                {
                    continue;
                }

                if(!extensibleObjects.contains(droppable))
                {
                    extensibleObjects.add(droppable);
                }
            }
        }

        return extensibleObjects.size();
    }
}
