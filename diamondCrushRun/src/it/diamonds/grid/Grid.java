package it.diamonds.grid;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.droppable.interfaces.ExtensibleObject;
import it.diamonds.droppable.interfaces.MergingObject;
import it.diamonds.droppable.pair.Direction;
import it.diamonds.engine.Environment;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.DrawableInterface;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.action.AbstractAction;
import it.diamonds.grid.action.CreateNewBigGemsAction;
import it.diamonds.grid.action.CrushByChestAction;
import it.diamonds.grid.action.CrushByFlashAction;
import it.diamonds.grid.action.InsertDroppableAction;
import it.diamonds.grid.action.TryToMoveDroppableAction;
import it.diamonds.grid.action.UpdateAnimationsAction;
import it.diamonds.grid.action.UpdateStoneAction;
import it.diamonds.grid.query.AbstractQuery;
import it.diamonds.playfield.ScoreCalculator;

import java.io.IOException;


public final class Grid implements DrawableInterface
{
    private Sprite background;

    private DroppableList gridElements = new DroppableList();

    private float actualGravity;

    private float normalGravity;

    private float gravityMultiplier;

    private float strongestGravityMultiplier;

    private Rectangle bounds;

    private int rows;

    private int columns;

    private int chainCounter;

    private int crushedGemsCounter;

    private ScoreCalculator scoreCalculator;

    private CreateNewBigGemsAction createNewBigGemsAction;

    private CrushByChestAction crushByChestAction;

    private CrushByFlashAction crushByFlashAction;

    private UpdateStoneAction updateStoneAction;


    public Grid(Environment environment, Rectangle bounds)
    {
        rows = environment.getConfig().getInteger("rows");
        columns = environment.getConfig().getInteger("columns");

        scoreCalculator = new ScoreCalculator(
            environment.getConfig().getInteger("BonusPercentage"));

        this.bounds = bounds;

        createGridBackground(environment, bounds);
        initializeGravity(environment);

        initializeActions(environment);
    }


    private void initializeActions(Environment environment)
    {
        createNewBigGemsAction = new CreateNewBigGemsAction(
            environment.getEngine(), gridElements);
        crushByFlashAction = new CrushByFlashAction(this);
        crushByChestAction = new CrushByChestAction(this);
        updateStoneAction = new UpdateStoneAction();
    }


    private void createGridBackground(Environment environment, Rectangle bounds)
    {
        background = new Sprite(bounds.left(), bounds.top(),
            environment.getEngine().createImage("grid-background"));
    }


    private void initializeGravity(Environment environment)
    {
        normalGravity = 0.5f;
        actualGravity = normalGravity;
        gravityMultiplier = (float)environment.getConfig().getInteger(
            "GravityMultiplier");
        strongestGravityMultiplier = (float)environment.getConfig().getInteger(
            "StrongestGravityMultiplier");
    }


    public static Grid createForTesting(Environment environment)
        throws IOException
    {
        Grid grid = new Grid(environment, new Rectangle(40, 40, 295, 487));

        return grid;
    }


    public void insertDroppable(Droppable droppable, int row, int column)
    {
        doAction(new InsertDroppableAction(droppable, row, column));
    }


    public boolean isDroppableAt(int row, int column)
    {
        return getDroppableAt(row, column) != null;
    }


    public Droppable getDroppableAt(int row, int column)
    {
        for(Droppable droppable : gridElements)
        {
            if(droppable.getCell().containsSingleCell(row, column))
            {
                return droppable;
            }
        }

        return null;
    }


    public void draw(AbstractEngine engine)
    {
        background.draw(engine);

        for(Droppable droppable : gridElements)
        {
            droppable.getSprite().draw(engine);
        }

    }


    // TODO: Questo metodo non e' testato
    public void moveDroppableToCell(Droppable droppable, int row, int column)
    {
        doAction(new TryToMoveDroppableAction(droppable, row, column));
    }


    public void moveDroppable(Droppable droppable, Direction direction)
    {
        if(droppable == null)
        {
            throw new IllegalArgumentException();
        }

        Cell cell = droppable.getCell();

        moveDroppableToCell(droppable, cell.getTopRow() + direction.deltaY(),
            cell.getLeftColumn() + direction.deltaX());
    }


    public boolean droppableCanMove(Droppable droppable, Direction direction)
    {
        if(droppable == null)
        {
            return false;
        }

        Cell cell = droppable.getCell();

        return droppableCanBePutAt(cell.getTopRow() + direction.deltaY(),
            cell.getLeftColumn() + direction.deltaX());
    }


    public void updateDroppable(Droppable droppable)
    {
        if(droppable == null)
        {
            return;
        }

        if(!droppable.getFallingObject().isFalling())
        {
            return;
        }

        droppable.getMovingDownObject().moveDown(this);
    }


    public void updateDroppableAnimations(long timer)
    {
        doAction(new UpdateAnimationsAction(timer));
    }


    // FIXME: questo e' da rimuovere
    public int getNumberOfDroppables()
    {
        return gridElements.size();
    }


    public void alignDroppableToCellUpperBound(Droppable droppable)
    {
        Cell cell = droppable.getCell();

        float newX = bounds.left() + cell.getLeftColumn() * Cell.SIZE;
        float newY = bounds.top() + cell.getTopRow() * Cell.SIZE;

        droppable.getSprite().setPosition(newX, newY);
    }


    public void removeDroppableFromGrid(Droppable droppable)
    {
        gridElements.remove(droppable);
    }


    public int computeTotalScore()
    {
        return scoreCalculator.getScore();
    }


    public void setStrongestGravity()
    {
        actualGravity = normalGravity * strongestGravityMultiplier;
    }


    public void setStrongerGravity()
    {
        actualGravity = normalGravity * gravityMultiplier;
    }


    public void setNormalGravity()
    {
        actualGravity = normalGravity;
    }


    public void setGravity(float gravity)
    {
        normalGravity = gravity;
        actualGravity = normalGravity;
    }


    public float getActualGravity()
    {
        return actualGravity;
    }


    public boolean isColumnFull(int column)
    {
        if(isDroppableAt(0, column))
        {
            return !getDroppableAt(0, column).getFallingObject().isFalling();
        }

        return false;
    }


    private boolean isValidRow(int row)
    {
        return (row >= 0) && (row < rows);
    }


    private boolean isValidColumn(int column)
    {
        return (column >= 0) && (column < columns);
    }


    public boolean isValidCell(int row, int column)
    {
        return isValidColumn(column) && isValidRow(row);
    }


    public boolean droppableCanBePutAt(int row, int column)
    {
        return isValidCell(row, column) && !isDroppableAt(row, column);
    }


    public void updateBigGems()
    {
        DroppableList droppableToRemove = new DroppableList();

        createNewBigGems();

        for(Droppable droppable : gridElements)
        {
            ExtensibleObject extensibleObject = droppable.getExtensibleObject();

            if(extensibleObject != null)
            {
                extensibleObject.getIncludedGems().clear();

                extensibleObject.extend(this);

                droppableToRemove.addAll(extensibleObject.getIncludedGems());
            }
        }

        for(Droppable droppable : droppableToRemove)
        {
            removeDroppableFromGrid(droppable);
        }

        while(updateMergeAllBigGem())
        {
            ;
        }
    }


    private boolean updateMergeAllBigGem()
    {

        for(Droppable droppable : gridElements)
        {
            MergingObject mergingObject = droppable.getMergingObject();

            if(mergingObject != null)
            {
                if(mergingObject.mergeUp(this)
                    || mergingObject.mergeRight(this))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private void createNewBigGems()
    {
        doAction(createNewBigGemsAction);
    }


    public boolean isCellInAExtensibleObject(int row, int column)
    {
        for(Droppable droppable : gridElements)
        {
            ExtensibleObject extensibleObject = droppable.getExtensibleObject();

            if(extensibleObject != null
                && droppable.getCell().containsSingleCell(row, column))
            {
                return true;
            }
        }
        return false;
    }


    public int getNumberOfRows()
    {
        return rows;
    }


    public int getNumberOfColumns()
    {
        return columns;
    }


    public void updateCrushes()
    {
        doAction(crushByFlashAction);

        crushByChestAction.reset();
        doAction(crushByChestAction);

        crushedGemsCounter += crushByChestAction.getCrushedGemsCounter();
        if(crushByChestAction.isCrushed())
        {
            chainCounter++;
        }
    }


    public ScoreCalculator getScoreCalculator()
    {
        return scoreCalculator;
    }


    public int getCrushedGemsCounter()
    {
        return crushedGemsCounter;
    }


    public void resetCrushedGemsCounter()
    {
        crushedGemsCounter = 0;
    }


    public int getChainCounter()
    {
        return chainCounter;
    }


    public void resetChainCounter()
    {
        chainCounter = 0;
    }


    public void closeChain()
    {
        scoreCalculator.closeChain(chainCounter);
    }


    private boolean heightOfColumnNoStopCondition(int row, int column)
    {
        Droppable droppable = getDroppableAt(row, column);

        boolean noGemPresent = droppable == null;

        boolean fallingGemPresent = droppable != null
            && droppable.getFallingObject() != null
            && droppable.getFallingObject().isFalling();

        return fallingGemPresent || noGemPresent;
    }


    public int getHeightOfColumn(int column)
    {
        int i = 0;

        while(i < rows && heightOfColumnNoStopCondition(i, column))
        {
            i++;
        }

        return rows - i;
    }


    public void updateStone()
    {
        doAction(updateStoneAction);
    }


    public float getRowUpperBound(int row)
    {
        return row * Cell.SIZE + bounds.top();
    }


    public void doQuery(AbstractQuery query)
    {
        query.apply(this, gridElements);
    }


    public void doAction(AbstractAction action)
    {
        action.apply(this, gridElements);
    }


    public void incrementChainCounter()
    {
        chainCounter++;
    }

}
