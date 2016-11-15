package it.diamonds.grid.state;


import it.diamonds.engine.input.InputReactorInterface;
import it.diamonds.grid.GridController;


public abstract class AbstractControllerState
{
    public abstract AbstractControllerState update(long timer,
        GridController gridController);


    public abstract boolean isCurrentState(String stateName);


    public void reactToInput(InputReactorInterface input, long timer)
    {
        ;
    }

}
