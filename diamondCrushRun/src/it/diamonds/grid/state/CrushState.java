package it.diamonds.grid.state;


import it.diamonds.droppable.Pattern;
import it.diamonds.engine.Environment;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;


public class CrushState extends AbstractControllerState
{
    private static Pattern pattern = new Pattern();

    private AbstractControllerState returnState;

    private AbstractControllerState gemFallState;


    public CrushState(Environment environment)
    {
        this.returnState = new StoneFallState(environment, pattern);
        this.gemFallState = new GemFallState(environment);
    }


    public CrushState(Environment environment, AbstractControllerState state)
    {
        this.returnState = state;
        this.gemFallState = new GemFallState(environment, state);
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("Crush");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        Grid grid = gridController.getGrid();

        grid.updateBigGems();

        grid.updateCrushes();

        if(!gridController.droppedGemWithoutGemsPairCanMoveDown())
        {
            gridController.getGrid().closeChain();
            return returnState.update(timer, gridController);
        }

        return gemFallState;
    }

}
