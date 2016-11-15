package it.diamonds.droppable;


import it.diamonds.droppable.interfaces.GridObject;
import it.diamonds.droppable.interfaces.MovingDownObject;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public abstract class AbstractDroppable implements Droppable, MovingDownObject,
    GridObject
{
    public static final String DROPPABLE_PATH = "gfx/droppables/";

    private Sprite sprite;

    private Cell cell;

    private DroppableType type;

    private DroppableColor color;


    protected AbstractDroppable(DroppableType type, DroppableColor color)
    {
        this.type = type;
        this.color = color;
    }


    public abstract boolean canMoveDown(Grid grid);


    public abstract int getScore();


    public DroppableType getType()
    {
        return type;
    }


    public boolean isSameOf(GridObject gridObject)
    {
        return gridObject.getType() == type && gridObject.getColor() == color;
    }


    public DroppableColor getColor()
    {
        return color;
    }


    protected boolean canMoveButNotWithFullGravity(Grid grid)
    {
        float droppableFutureLowerLimit = getSprite().getPosition().getY()
            + grid.getActualGravity() + getCell().getHeight() * Cell.SIZE;

        float gridBottom = grid.getRowUpperBound(grid.getNumberOfRows());
        if(droppableFutureLowerLimit > gridBottom)
        {
            return true;
        }

        float nextRowsUpperLimit = grid.getRowUpperBound(getCell().getTopRow() + 1);
        if(droppableFutureLowerLimit <= nextRowsUpperLimit)
        {
            return false;
        }

        return searchForDoppablesInTheLowerRow(grid);
    }


    private boolean searchForDoppablesInTheLowerRow(Grid grid)
    {
        int rowUnderDroppable = getCell().getBottomRow() + 1;
        for(int column = getCell().getLeftColumn(); column <= getCell().getRightColumn(); ++column)
        {
            if(grid.isDroppableAt(rowUnderDroppable, column))
            {
                return true;
            }
        }
        return false;
    }


    public void moveDown(Grid grid)
    {
        if(canMoveButNotWithFullGravity(grid))
        {
            getSprite().getPosition().setY(
                grid.getRowUpperBound(getCell().getTopRow()));
            return;
        }

        getSprite().translate(0, grid.getActualGravity());

        if(getSprite().getPosition().getY() > grid.getRowUpperBound(getCell().getTopRow()))
        {
            getCell().setRow(getCell().getTopRow() + 1);
        }
    }


    public Sprite getSprite()
    {
        return sprite;
    }


    protected void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
    }


    public Cell getCell()
    {
        return cell;
    }


    protected void setCell(Cell cell)
    {
        this.cell = cell;
    }


    public GridObject getGridObject()
    {
        return this;
    }


    public MovingDownObject getMovingDownObject()
    {
        return this;
    }
}
