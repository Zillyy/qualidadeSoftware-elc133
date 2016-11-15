package it.diamonds.droppable.interfaces;


import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;


public interface GridObject
{
    DroppableType getType();


    DroppableColor getColor();


    boolean isSameOf(GridObject gridObject);
}
