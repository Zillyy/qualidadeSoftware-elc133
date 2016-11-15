package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public class CrushByFlashAction extends AbstractCrushAction
{
    public CrushByFlashAction(Grid grid)
    {
        super(grid);
        setAddAtScore(false);
    }


    public void apply(Grid grid, DroppableList gridElements)
    {
        forEachDroppable(grid);
    }


    protected void applyOn(Droppable gem)
    {
        if(gem.getGridObject().getType().isFlashingGem()
            && !gem.getFallingObject().isFalling())
        {
            updateCrushesOnFlash(gem);
        }
    }


    private void updateCrushesOnFlash(Droppable gem)
    {
        Droppable gemToDelete = getGemToDelete(gem);
        if(gemToDelete != null)
        {
            DroppableList gemList = getAllGemsSameOf(gemToDelete);
            gemList.add(gem);
            if(gemList.size() != 0)
            {
                crushDroppables(gemList);
            }
        }
    }


    private Droppable getGemToDelete(Droppable falshingGem)
    {
        Droppable gemToDelete = null;

        Cell cell = falshingGem.getCell();

        if(cell.getTopRow() < getGrid().getNumberOfRows() - 1)
        {
            gemToDelete = getGem(cell.getTopRow() + 1, cell.getLeftColumn());
        }

        if(cell.getLeftColumn() >= 1 && gemToDelete == null)
        {
            gemToDelete = getGem(cell.getTopRow(), cell.getLeftColumn() - 1);
        }

        if(cell.getLeftColumn() < getGrid().getNumberOfColumns() - 1
            && gemToDelete == null)
        {
            gemToDelete = getGem(cell.getTopRow(), cell.getLeftColumn() + 1);
        }

        if(cell.getTopRow() > 1 && gemToDelete == null)
        {
            gemToDelete = getGem(cell.getTopRow() - 1, cell.getLeftColumn());
        }

        return gemToDelete;
    }


    private Droppable getGem(int row, int column)
    {
        Droppable gem = getGrid().getDroppableAt(row, column);
        if(gem == null)
        {
            return null;
        }

        if(gem.getFallingObject() != null && gem.getFallingObject().isFalling())
        {
            return null;
        }

        if(gem.getGridObject().getType().isStone())
        {
            return null;
        }

        return gem;
    }


    private DroppableList getAllGemsSameOf(Droppable gemToDelete)
    {
        DroppableList gemList = new DroppableList();
        Droppable gem;
        for(int row = getGrid().getNumberOfRows() - 1; row >= 0; row--)
        {
            for(int column = 0; column < getGrid().getNumberOfColumns(); column++)
            {
                gem = getGrid().getDroppableAt(row, column);
                if(gem == null)
                {
                    continue;
                }

                if(gem.getGridObject().getColor() == gemToDelete.getGridObject().getColor())
                {
                    if(!gemList.contains(gem))
                    {
                        gemList.add(gem);
                    }
                }
            }
        }
        return gemList;
    }
}
