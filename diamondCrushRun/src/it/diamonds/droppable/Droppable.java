package it.diamonds.droppable;


import it.diamonds.droppable.interfaces.AnimatedObject;
import it.diamonds.droppable.interfaces.ExtensibleObject;
import it.diamonds.droppable.interfaces.FallingObject;
import it.diamonds.droppable.interfaces.GridObject;
import it.diamonds.droppable.interfaces.MergingObject;
import it.diamonds.droppable.interfaces.MoveableObject;
import it.diamonds.droppable.interfaces.MovingDownObject;
import it.diamonds.droppable.interfaces.ObjectWithCollisionSound;
import it.diamonds.engine.video.Sprite;
import it.diamonds.grid.Cell;


public interface Droppable
{

    Sprite getSprite();


    Cell getCell();


    int getScore();


    MoveableObject getMoveableObject();


    AnimatedObject getAnimatedObject();


    ObjectWithCollisionSound getObjectWithCollisionSound();


    FallingObject getFallingObject();


    GridObject getGridObject();


    MovingDownObject getMovingDownObject();


    ExtensibleObject getExtensibleObject();


    MergingObject getMergingObject();

}
