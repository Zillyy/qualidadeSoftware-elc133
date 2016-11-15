package it.diamonds.grid.state;


import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.engine.Environment;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;


public class GemsPairOnControlState extends AbstractReactiveState
{
    private Environment environment;


    public GemsPairOnControlState(Environment environment)
    {
        this.environment = environment;
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("GemsPairOnControl");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        Grid grid = gridController.getGrid();
        DroppablesPair gemsPair = gridController.getGemsPair();
        gemsPair.update(timer);

        if(gemsPair.oneDroppableIsNotFalling())
        {
            grid.setStrongestGravity();
        }
        else if(gemsPair.bothDroppablesAreNotFalling())
        {
            grid.setNormalGravity();

            grid.resetChainCounter();
            grid.updateStone();
            gemsPair.setNoPivot();
            gemsPair.setNoSlave();

            return new StoneTurnState(environment).update(timer, gridController);
        }
        return this;
    }

}
