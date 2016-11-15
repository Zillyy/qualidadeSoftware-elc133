package it.diamonds.droppable.gems;


import it.diamonds.droppable.AbstractDroppable;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.DroppableType;
import it.diamonds.droppable.interfaces.AnimatedObject;
import it.diamonds.droppable.interfaces.ExtensibleObject;
import it.diamonds.droppable.interfaces.FallingObject;
import it.diamonds.droppable.interfaces.MergingObject;
import it.diamonds.droppable.interfaces.MoveableObject;
import it.diamonds.droppable.interfaces.ObjectWithCollisionSound;
import it.diamonds.engine.Point;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Sprite;
import it.diamonds.engine.video.TiledSprite;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public class BigGem extends AbstractDroppable implements ExtensibleObject,
    MergingObject
{
    private static final String GEMGROUP_PATH = "gfx/droppables/tiles/";

    private Sprite sprite;

    private Cell cell;

    private DroppableList includedGems;


    public BigGem(int row, int column, DroppableColor color,
        AbstractEngine engine, Point position)
    {
        super(DroppableType.GEM, color);

        cell = new Cell(column, row, column, row);
        sprite = new TiledSprite(position.getX(), position.getY(),
            engine.createImage(GEMGROUP_PATH + color.getName()), cell);

        super.setCell(cell);
        super.setSprite(sprite);

        includedGems = new DroppableList();
    }


    public void addGem(Droppable droppable)
    {
        Cell droppableCell = droppable.getCell();
        int gemColumn = droppableCell.getLeftColumn();
        int gemRow = droppableCell.getTopRow();

        cell.resizeToContain(gemRow, gemColumn);

        includedGems.add(droppable);
    }


    public DroppableList getIncludedGems()
    {
        return includedGems;
    }


    public void extend(Grid grid)
    {
        extendUp(grid);
        extendRight(grid);
        extendLeft(grid);
        extendDown(grid);

    }


    private void extendUp(Grid grid)
    {
        final int row = cell.getTopRow() - 1;

        if(row < 0)
        {
            return;
        }

        if(canAddRow(grid, row))
        {
            addRow(grid, row);
            sprite.getPosition().setY(sprite.getPosition().getY() - Cell.SIZE);
        }
    }


    private void extendDown(Grid grid)
    {
        final int row = cell.getBottomRow() + 1;

        if(row >= grid.getNumberOfRows())
        {
            return;
        }

        if(canAddRow(grid, row))
        {
            addRow(grid, row);
        }
    }


    private void extendRight(Grid grid)
    {
        final int column = cell.getRightColumn() + 1;

        if(column >= grid.getNumberOfColumns())
        {
            return;
        }

        if(canAddAColumn(grid, column))
        {
            addColumn(grid, column);
        }
    }


    private void extendLeft(Grid grid)
    {
        final int column = cell.getLeftColumn() - 1;

        if(column < 0)
        {
            return;
        }

        if(canAddAColumn(grid, column))
        {
            addColumn(grid, column);
            sprite.getPosition().setX(sprite.getPosition().getX() - Cell.SIZE);
        }
    }


    private boolean canAddRow(Grid grid, int row)
    {
        for(int column = cell.getLeftColumn(); column <= cell.getRightColumn(); ++column)
        {
            final Droppable gem = grid.getDroppableAt(row, column);

            if(isGemNotAddable(gem)
                || grid.isCellInAExtensibleObject(row, column))
            {
                return false;
            }
        }

        return true;
    }


    private void addRow(Grid grid, int row)
    {
        DroppableList droppableList = new DroppableList();
        for(int column = cell.getLeftColumn(); column <= cell.getRightColumn(); ++column)
        {
            droppableList.add(grid.getDroppableAt(row, column));
        }
        addGem(droppableList);
    }


    private void addGem(DroppableList droppableList)
    {
        for(Droppable droppable : droppableList)
        {
            addGem(droppable);
        }
    }


    private boolean canAddAColumn(Grid grid, int column)
    {
        for(int row = cell.getTopRow(); row <= cell.getBottomRow(); ++row)
        {
            final Droppable gem = grid.getDroppableAt(row, column);

            if(isGemNotAddable(gem)
                || grid.isCellInAExtensibleObject(row, column))
            {
                return false;
            }
        }

        return true;
    }


    private void addColumn(Grid grid, int column)
    {
        DroppableList droppableList = new DroppableList();
        for(int row = cell.getTopRow(); row <= cell.getBottomRow(); ++row)
        {
            droppableList.add(grid.getDroppableAt(row, column));
        }
        addGem(droppableList);
    }


    private boolean isGemNotAddable(Droppable gem)
    {
        return gem == null
            || !isSameOf(gem.getGridObject())
            || (gem.getFallingObject() != null && gem.getFallingObject().isFalling());
    }


    public boolean mergeUp(Grid grid)
    {
        Droppable otherDroppable = grid.getDroppableAt(cell.getTopRow() - 1,
            cell.getRightColumn());

        if(otherDroppable == null)
        {
            return false;
        }

        if(otherDroppable.getGridObject().getColor() != super.getColor()
            || otherDroppable.getCell().getLeftColumn() != cell.getLeftColumn()
            || otherDroppable.getCell().getWidth() != cell.getWidth())
        {
            return false;
        }

        sprite.setPosition(otherDroppable.getSprite().getPosition().getX(),
            otherDroppable.getSprite().getPosition().getY());

        cell.resizeToContain(otherDroppable.getCell().getTopRow(),
            cell.getRightColumn());

        grid.removeDroppableFromGrid(otherDroppable);
        return true;
    }


    public boolean mergeRight(Grid grid)
    {
        Droppable otherDroppable = grid.getDroppableAt(cell.getTopRow(),
            cell.getRightColumn() + 1);

        if(otherDroppable == null)
        {
            return false;
        }

        if(otherDroppable.getGridObject().getColor() != super.getColor()
            || otherDroppable.getCell().getTopRow() != cell.getTopRow()
            || otherDroppable.getCell().getHeight() != cell.getHeight())
        {
            return false;
        }

        cell.resizeToContain(cell.getTopRow(),
            otherDroppable.getCell().getRightColumn());

        grid.removeDroppableFromGrid(otherDroppable);
        return true;
    }


    public void moveDown(Grid grid)
    {
        if(!canMoveDown(grid))
        {
            return;
        }

        super.moveDown(grid);
    }


    public boolean canMoveDown(Grid grid)
    {
        if(getSprite().getPosition().getY() != grid.getRowUpperBound(getCell().getTopRow()))
        {
            return true;
        }

        if(cell.getBottomRow() == grid.getNumberOfRows() - 1)
        {
            return false;
        }

        for(int column = cell.getLeftColumn(); column <= cell.getRightColumn(); column++)
        {
            if(grid.isDroppableAt(cell.getBottomRow() + 1, column))
            {
                return false;
            }
        }

        return true;
    }


    public int getScore()
    {
        return super.getColor().getScore();
    }


    public MoveableObject getMoveableObject()
    {
        return null;
    }


    public AnimatedObject getAnimatedObject()
    {
        return null;
    }


    public ObjectWithCollisionSound getObjectWithCollisionSound()
    {
        return null;
    }


    public FallingObject getFallingObject()
    {
        return null;
    }


    public ExtensibleObject getExtensibleObject()
    {
        return this;
    }


    public MergingObject getMergingObject()
    {
        return this;
    }

}
