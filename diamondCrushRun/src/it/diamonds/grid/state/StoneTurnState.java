package it.diamonds.grid.state;


import it.diamonds.engine.Environment;
import it.diamonds.grid.GridController;
import it.diamonds.grid.action.TurnStonesIntoGemsAction;


public class StoneTurnState extends AbstractControllerState
{
    private Environment environment;


    public StoneTurnState(Environment environment)
    {
        this.environment = environment;
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        TurnStonesIntoGemsAction action = new TurnStonesIntoGemsAction(
            environment, gridController.getGrid());

        gridController.getGrid().doAction(action);
        if(action.getNumberOfTurningStones() > 0)
        {
            return this;
        }

        return new CrushState(environment).update(timer, gridController);
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("StoneTurn");
    }

}
