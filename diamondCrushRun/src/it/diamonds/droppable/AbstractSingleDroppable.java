package it.diamonds.droppable;


import it.diamonds.droppable.interfaces.AnimatedObject;
import it.diamonds.droppable.interfaces.ExtensibleObject;
import it.diamonds.droppable.interfaces.FallingObject;
import it.diamonds.droppable.interfaces.MergingObject;
import it.diamonds.droppable.interfaces.MoveableObject;
import it.diamonds.droppable.interfaces.ObjectWithCollisionSound;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.audio.Sound;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Frame;
import it.diamonds.engine.video.FrameList;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.Cell;
import it.diamonds.grid.Grid;


public abstract class AbstractSingleDroppable extends AbstractDroppable
    implements MoveableObject, ObjectWithCollisionSound, AnimatedObject,
    FallingObject
{
    private Sound collisionSound;

    private boolean stopped;

    private int currentFrame;

    private FrameList animationFrames = new FrameList();

    private long numberOfFramesInTexture = 6;

    private long animationCycleStart = 0;

    private long animationCycleLength = 0;


    protected AbstractSingleDroppable(AbstractEngine engine,
        DroppableType type, DroppableColor color, int animationDelay)
    {
        super(type, color);

        Rectangle rectangle = new Rectangle(0, 0, Cell.SIZE - 1, Cell.SIZE - 1);
        Sprite sprite = new Sprite(0, 0, rectangle,
            engine.createImage(getTextureName(type, color)));
        Cell cell = new Cell();

        super.setSprite(sprite);
        super.setCell(cell);

        addFrame(0, 0, animationDelay);
    }


    public int getScore()
    {
        return 0;
    }


    protected String getTextureName(DroppableType type, DroppableColor color)
    {
        String appendedColor = "/" + color.getName();
        return DROPPABLE_PATH.concat(type.getName()).concat(appendedColor);
    }


    protected void setNumberOfFramesInTexture(long numberOfFramesInTexture)
    {
        this.numberOfFramesInTexture = numberOfFramesInTexture;
    }


    private void playCollisionSound()
    {
        if(null != collisionSound)
        {
            collisionSound.play();
        }
    }


    public boolean isFalling()
    {
        return !stopped;
    }


    public void setCollisionSound(Sound sound)
    {
        if(sound == null)
        {
            throw new NullPointerException();
        }

        collisionSound = sound;
    }


    public Sound getCollisionSound()
    {
        return collisionSound;
    }


    public void drop()
    {
        if(isFalling())
        {
            stopped = true;
            playCollisionSound();
        }
    }


    public boolean canMoveDown(Grid grid)
    {
        if(getSprite().getPosition().getY() != grid.getRowUpperBound(getCell().getTopRow()))
        {
            return true;
        }

        if(getCell().getTopRow() == grid.getNumberOfRows() - 1)
        {
            return false;
        }

        return !grid.isDroppableAt(getCell().getBottomRow() + 1,
            getCell().getLeftColumn());
    }


    public void moveDown(Grid grid)
    {
        if(canMoveButNotWithFullGravity(grid))
        {
            getSprite().getPosition().setY(
                grid.getRowUpperBound(getCell().getTopRow()));
            drop();
            return;
        }
        super.moveDown(grid);
    }


    // TODO: Metodo non testato!
    public void moveToCell(int row, int column)
    {
        Cell cell = getCell();

        int deltaX = column - cell.getLeftColumn();
        int deltaY = row - cell.getTopRow();

        cell.setColumn(cell.getLeftColumn() + deltaX);
        cell.setRow(cell.getTopRow() + deltaY);

        getSprite().translate(Cell.SIZE * deltaX, Cell.SIZE * deltaY);
    }


    public int getNumberOfFrames()
    {
        return animationFrames.size();
    }


    public void addFrame(int x, int y, int delay)
    {
        animationFrames.add(new Frame(x, y, delay));
        animationCycleLength += delay;
    }


    public int getCurrentFrame()
    {
        return currentFrame;
    }


    public void update(long timer)
    {
        long animationTime = computeAnimationTime(timer);
        setCurrentFrame(findAnimationFrame(animationTime));
    }


    private long computeAnimationTime(long timer)
    {
        long timeElapsedSinceCycleStart = timer - animationCycleStart;

        animationCycleStart += animationCycleLength
            * (timeElapsedSinceCycleStart / animationCycleLength);

        timeElapsedSinceCycleStart %= animationCycleLength;

        return timeElapsedSinceCycleStart;
    }


    private int findAnimationFrame(long animationTime)
    {
        int frameIndex = 0;

        int frameLength = animationFrames.get(frameIndex).getLength();

        while(animationTime >= frameLength)
        {
            ++frameIndex;

            frameLength += animationFrames.get(frameIndex).getLength();
        }

        return frameIndex;
    }


    public void setCurrentFrame(int frameIndex)
    {
        Frame frame = animationFrames.get(frameIndex);

        getSprite().setOrigin(frame.getX(), frame.getY());

        currentFrame = frameIndex;
    }


    public int getFrameDuration(int index)
    {
        return animationFrames.get(index).getLength();
    }


    public void createAnimationSequence(int animationUpdateRate)
    {
        for(int i = 1; i < numberOfFramesInTexture; ++i)
        {
            addFrame(0, Cell.SIZE * i, animationUpdateRate);
        }
    }


    public MoveableObject getMoveableObject()
    {
        return this;
    }


    public ObjectWithCollisionSound getObjectWithCollisionSound()
    {
        return this;
    }


    public AnimatedObject getAnimatedObject()
    {
        return this;
    }


    public FallingObject getFallingObject()
    {
        return this;
    }


    public ExtensibleObject getExtensibleObject()
    {
        return null;
    }


    public MergingObject getMergingObject()
    {
        return null;
    }
}
