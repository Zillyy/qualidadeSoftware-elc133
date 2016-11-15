package it.diamonds.droppable.interfaces;


import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public interface ExtensibleObject
{

    void extend(Grid grid);


    DroppableList getIncludedGems();

}
