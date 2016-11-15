package it.diamonds.droppable.pair;


import static it.diamonds.droppable.pair.Position.BOTTOM;
import static it.diamonds.droppable.pair.Position.LEFT;
import static it.diamonds.droppable.pair.Position.RIGHT;
import static it.diamonds.droppable.pair.Position.TOP;
import static it.diamonds.droppable.pair.Position.UNDEFINED;
import it.diamonds.droppable.Droppable;
import it.diamonds.engine.Config;
import it.diamonds.engine.modifiers.Pulsation;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public final class DroppablesPair
{
    private Droppable pivot;

    private Droppable slave;

    private Grid grid;

    private Position slavePosition;

    private int pulsationLength;

    private float sizeMultiplier;


    public DroppablesPair(Grid grid, Config config)
    {
        this.grid = grid;
        slavePosition = UNDEFINED;
        pulsationLength = config.getInteger("PulsationLength");
        sizeMultiplier = config.getInteger("SizeMultiplier");
    }


    public Droppable getPivot()
    {
        return pivot;
    }


    public void setPivot(Droppable droppable)
    {
        pivot = droppable;

        updatePulsingState();
    }


    public void setNoPivot()
    {
        pivot = null;
        updatePulsingState();
    }


    public Droppable getSlave()
    {
        return slave;
    }


    public void setSlave(Droppable droppable)
    {
        slave = droppable;
        computeSlavePosition();
    }


    private void computeSlavePosition()
    {
        slavePosition = UNDEFINED;

        Cell slaveCell = slave.getCell();
        Cell pivotCell = pivot.getCell();

        if(slaveCell.getLeftColumn() == pivotCell.getLeftColumn())
        {
            if(slaveCell.getTopRow() == (pivotCell.getTopRow() + 1))
            {
                slavePosition = BOTTOM;
            }
            if(slaveCell.getTopRow() == (pivotCell.getTopRow() - 1))
            {
                slavePosition = TOP;
            }
        }

        if(slaveCell.getLeftColumn() == (pivotCell.getLeftColumn() - 1))
        {
            slavePosition = LEFT;
        }

        if(slaveCell.getLeftColumn() == (pivotCell.getLeftColumn() + 1))
        {
            slavePosition = RIGHT;
        }
    }


    public void setNoSlave()
    {
        slave = null;
        slavePosition = UNDEFINED;
        updatePulsingState();
    }


    private boolean droppablesAreHorizontal()
    {
        return slavePosition == Position.LEFT
            || slavePosition == Position.RIGHT;
    }


    public void move(Direction direction)
    {
        if(slave == null)
        {
            grid.moveDroppable(pivot, direction);
            return;
        }

        if(grid.droppableCanMove(pivot, direction)
            && grid.droppableCanMove(slave, direction))
        {
            grid.moveDroppable(pivot, direction);
            grid.moveDroppable(slave, direction);
        }
        else if(droppablesAreHorizontal())
        {
            if(grid.droppableCanMove(slave, direction))
            {
                grid.moveDroppable(slave, direction);
                grid.moveDroppable(pivot, direction);
            }
            else if(grid.droppableCanMove(pivot, direction))
            {
                grid.moveDroppable(pivot, direction);
                grid.moveDroppable(slave, direction);
            }
        }
    }


    private void rotateSlave(SlaveRotation rotation)
    {
        if(pivot == null || slave == null)
        {
            return;
        }

        Position position = slavePosition;

        Direction newDirection;

        Movement movement = rotation.getMovement(position);

        newDirection = movement.direction();

        position = movement.newPosition();

        if(grid.droppableCanMove(pivot, newDirection))
        {
            Cell pivotGridObject = pivot.getCell();

            grid.moveDroppableToCell(slave, pivotGridObject.getTopRow()
                + newDirection.deltaY(), pivotGridObject.getLeftColumn()
                + newDirection.deltaX());

            slavePosition = position;
        }
    }


    public void mirrorSlave()
    {
        rotateSlave(SlaveRotation.MIRROR);
    }


    public void rotateClockwise()
    {
        rotateSlave(SlaveRotation.CLOCKWISE);
    }


    public void rotateCounterclockwise()
    {
        rotateSlave(SlaveRotation.COUNTERCLOCKWISE);
    }


    public void update(long timer)
    {
        if(slavePosition == BOTTOM)
        {
            grid.updateDroppable(slave);
            grid.updateDroppable(pivot);
        }
        else
        {
            grid.updateDroppable(pivot);
            grid.updateDroppable(slave);
        }
        updatePulsingState();
    }


    public boolean canReactToInput()
    {
        if(slave == null)
        {
            return null != pivot && pivot.getFallingObject().isFalling();
        }
        return slave.getFallingObject().isFalling()
            && pivot.getFallingObject().isFalling();
    }


    public boolean isPulsing()
    {
        return pivot != null && slave != null
            && pivot.getFallingObject().isFalling()
            && slave.getFallingObject().isFalling();
    }


    private void updatePulsingState()
    {
        if(pivot == null)
        {
            return;
        }

        if(isPulsing())
        {
            if(!spriteIsPulsing(pivot.getSprite()))
            {
                spriteStartPulsation(pivot.getSprite());
            }
        }
        else
        {
            if(spriteIsPulsing(pivot.getSprite()))
            {
                spriteStopPulsation(pivot.getSprite());
            }
        }
    }


    private boolean spriteIsPulsing(Sprite sprite)
    {
        return sprite.getDrawModifier() != null;
    }


    private void spriteStartPulsation(Sprite sprite)
    {
        sprite.setDrawModifier(new Pulsation(sprite, pulsationLength,
            sizeMultiplier));
    }


    private void spriteStopPulsation(Sprite sprite)
    {
        sprite.removeDrawModifier();
    }


    public String toString()
    {
        if(pivot == null || slave == null)
        {
            return "";
        }

        return pivot.getGridObject().getColor().getName()
            + pivot.getGridObject().getType().getName() + " "
            + pivot.getGridObject().getColor().getName()
            + slave.getGridObject().getType().getName();
    }


    public boolean oneDroppableIsNotFalling()
    {
        if(slave == null || pivot == null)
        {
            return false;
        }

        return !pivot.getFallingObject().isFalling()
            ^ !slave.getFallingObject().isFalling();
    }


    public boolean bothDroppablesAreNotFalling()
    {
        if(slave == null && pivot != null)
        {
            return !pivot.getFallingObject().isFalling();
        }

        return !pivot.getFallingObject().isFalling()
            && !slave.getFallingObject().isFalling();
    }
}
