package it.diamonds.droppable.interfaces;


import it.diamonds.grid.Grid;


public interface MovingDownObject
{
    void moveDown(Grid grid);


    boolean canMoveDown(Grid grid);
}
