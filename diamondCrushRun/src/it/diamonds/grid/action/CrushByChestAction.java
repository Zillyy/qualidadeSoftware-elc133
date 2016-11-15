package it.diamonds.grid.action;


import static it.diamonds.droppable.pair.Direction.GO_DOWN;
import static it.diamonds.droppable.pair.Direction.GO_LEFT;
import static it.diamonds.droppable.pair.Direction.GO_RIGHT;
import static it.diamonds.droppable.pair.Direction.GO_UP;

import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.pair.Direction;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public class CrushByChestAction extends AbstractCrushAction
{
    private boolean isCrushed = false;


    public CrushByChestAction(Grid grid)
    {
        super(grid);
        setAddAtScore(true);
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        if(gem.getGridObject().getType().isChest()
            && !gem.getFallingObject().isFalling())
        {
            isCrushed |= updateCrushesOnChest(gem);
        }
    }


    private boolean updateCrushesOnChest(Droppable gem)
    {
        DroppableList gemList = new DroppableList();
        getAdjacentCrushableGems(gem, gemList);

        if(gemList.size() == 0)
        {
            return false;
        }

        crushDroppables(gemList);
        return true;
    }


    private void getAdjacentCrushableGems(Droppable crushSourceDroppable,
        DroppableList adiacentCrushableGems)
    {
        Cell sourceCell = crushSourceDroppable.getCell();

        if(sourceCell.getLeftColumn() >= 1)
        {
            getLeftOrRightAdjacentCrushableGems(crushSourceDroppable,
                adiacentCrushableGems, GO_LEFT);
        }

        if(sourceCell.getRightColumn() < getGrid().getNumberOfColumns() - 1)
        {
            getLeftOrRightAdjacentCrushableGems(crushSourceDroppable,
                adiacentCrushableGems, GO_RIGHT);
        }

        if(sourceCell.getTopRow() >= 1)
        {
            getUpOrDownAdjacentCrushableGems(crushSourceDroppable,
                adiacentCrushableGems, GO_UP);
        }

        if(sourceCell.getBottomRow() < getGrid().getNumberOfRows() - 1)
        {
            getUpOrDownAdjacentCrushableGems(crushSourceDroppable,
                adiacentCrushableGems, GO_DOWN);
        }
    }


    private void getUpOrDownAdjacentCrushableGems(Droppable source,
        DroppableList adiacentCrushableGems, Direction direction)
    {
        int row = 0;

        if(direction == GO_UP)
        {
            row = source.getCell().getTopRow();
        }
        if(direction == GO_DOWN)
        {
            row = source.getCell().getBottomRow();
        }

        for(int column = source.getCell().getLeftColumn(); column <= source.getCell().getRightColumn(); column++)
        {
            Droppable toTest = getGrid().getDroppableAt(
                row + direction.deltaY(), column);
            tryToAddGemToCrushableGems(toTest, source, adiacentCrushableGems);
        }
    }


    private void getLeftOrRightAdjacentCrushableGems(Droppable source,
        DroppableList adiacentCrushableGems, Direction direction)
    {
        int column = 0;

        if(direction == GO_LEFT)
        {
            column = source.getCell().getLeftColumn();
        }
        if(direction == GO_RIGHT)
        {
            column = source.getCell().getRightColumn();
        }

        for(int row = source.getCell().getTopRow(); row <= source.getCell().getBottomRow(); row++)
        {
            Droppable toTest = getGrid().getDroppableAt(row,
                column + direction.deltaX());
            tryToAddGemToCrushableGems(toTest, source, adiacentCrushableGems);
        }
    }


    private void tryToAddGemToCrushableGems(Droppable droppableToTest,
        Droppable adjacentDroppable, DroppableList adjacentCrushableDroppables)
    {
        if(droppableToTest == null)
        {
            return;
        }

        if(droppableToTest.getFallingObject() != null
            && droppableToTest.getFallingObject().isFalling())
        {
            return;
        }

        if(droppableToTest.getGridObject().getType().isStone())
        {
            return;
        }

        if(droppableToTest.getGridObject().getColor() != adjacentDroppable.getGridObject().getColor())
        {
            return;
        }

        if(adjacentCrushableDroppables.contains(droppableToTest))
        {
            return;
        }

        if(!adjacentCrushableDroppables.contains(droppableToTest))
        {
            adjacentCrushableDroppables.add(droppableToTest);
        }

        crushAdjacentStones(droppableToTest);
        getAdjacentCrushableGems(droppableToTest, adjacentCrushableDroppables);
    }


    private void crushAdjacentStones(Droppable droppableToTest)
    {
        Direction[] directions = { GO_UP, GO_RIGHT, GO_DOWN, GO_LEFT };

        Cell droppableToTestCell = droppableToTest.getCell();

        Grid grid = getGrid();

        for(Direction direction : directions)
        {
            int adjacentRow = droppableToTestCell.getTopRow()
                + direction.deltaX();
            int adjacentColumn = droppableToTestCell.getLeftColumn()
                + direction.deltaY();

            if(!grid.isValidCell(adjacentRow, adjacentColumn))
            {
                continue;
            }

            Droppable adjacentDroppable = grid.getDroppableAt(adjacentRow,
                adjacentColumn);

            if(adjacentDroppable == null)
            {
                continue;
            }

            if(adjacentDroppable.getGridObject().getType().isStone())
            {
                getGridElements().remove(adjacentDroppable);
            }
        }
    }


    public boolean isCrushed()
    {
        return isCrushed;
    }


    public void reset()
    {
        super.reset();
        isCrushed = false;
    }
}
