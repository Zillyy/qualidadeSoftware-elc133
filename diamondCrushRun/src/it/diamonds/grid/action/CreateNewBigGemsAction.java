package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.gems.BigGem;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public class CreateNewBigGemsAction extends AbstractAction
{

    private transient AbstractEngine engine;

    private transient DroppableList droppables;


    public CreateNewBigGemsAction(AbstractEngine engine,
        DroppableList droppables)
    {
        this.engine = engine;
        this.droppables = droppables;
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        super.apply(grid, gridElements);
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        Cell cell = gem.getCell();

        int row = cell.getTopRow();
        int column = cell.getLeftColumn();

        if(isGemNotValidForBigGem(gem))
        {
            return;
        }

        if(isGemNeighbourValidForBigGem(gem, -1, 0)
            && isGemNeighbourValidForBigGem(gem, 0, 1)
            && isGemNeighbourValidForBigGem(gem, -1, 1))
        {
            BigGem bigGem = new BigGem(
                row - 1,
                column,
                gem.getGridObject().getColor(),
                engine,
                getGrid().getDroppableAt(row - 1, column).getSprite().getPosition());

            bigGem.addGem(gem);
            bigGem.addGem(getGrid().getDroppableAt(row - 1, column));
            bigGem.addGem(getGrid().getDroppableAt(row, column + 1));
            bigGem.addGem(getGrid().getDroppableAt(row - 1, column + 1));

            for(Droppable droppable : bigGem.getIncludedGems())
            {
                getGrid().removeDroppableFromGrid(droppable);
            }

            droppables.add(bigGem);
        }
    }


    private boolean isGemNotValidForBigGem(Droppable gem)
    {
        int row = gem.getCell().getTopRow();
        int column = gem.getCell().getLeftColumn();

        if(getGrid().isCellInAExtensibleObject(row, column))
        {
            return true;
        }

        if(!gem.getGridObject().getType().isGem())
        {
            return true;
        }

        return false;
    }


    private boolean isGemNeighbourValidForBigGem(Droppable gem, int rowOffset,
        int columnOffset)
    {
        Cell cell = gem.getCell();

        int otherColumn = cell.getLeftColumn() + columnOffset;
        int otherRow = cell.getTopRow() + rowOffset;

        if(!getGrid().isValidCell(otherRow, otherColumn))
        {
            return false;
        }

        if(!getGrid().isDroppableAt(otherRow, otherColumn))
        {
            return false;
        }

        if(getGrid().isCellInAExtensibleObject(otherRow, otherColumn))
        {
            return false;
        }

        Droppable otherGem = getGrid().getDroppableAt(otherRow, otherColumn);
        if(otherGem.getFallingObject().isFalling())
        {
            return false;
        }

        if(gem.getGridObject().isSameOf(otherGem.getGridObject()))
        {
            return true;
        }

        return false;
    }
}
