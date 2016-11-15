package it.diamonds.grid.state;


import it.diamonds.grid.GridController;


public class GameOverState extends AbstractControllerState
{
    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        return this;
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("GameOver");
    }

}
