package it.diamonds.grid.state;


import it.diamonds.engine.Environment;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;


public class GemFallState extends AbstractControllerState
{
    private Environment environment;

    private GemFallStrategy gemFallStrategy;


    public GemFallState(Environment environment)
    {
        this.environment = environment;
        this.gemFallStrategy = new NormalGemFallState();
    }


    public GemFallState(Environment environment, AbstractControllerState state)
    {
        this.environment = environment;
        this.gemFallStrategy = new ShortGemFallState(state);
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("GemFall");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        Grid grid = gridController.getGrid();

        grid.setStrongestGravity();
        gridController.updateFallsWithOutGemsPair();

        if(gridController.droppedGemWithoutGemsPairCanMoveDown())
        {
            return this;
        }

        grid.setNormalGravity();
        return gemFallStrategy.returnState(environment, timer).update(timer,
            gridController);
    }
}
