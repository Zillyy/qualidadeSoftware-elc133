package it.diamonds.grid.state;


import it.diamonds.engine.Environment;
import it.diamonds.grid.GridController;


public class WaitNextCrushState extends AbstractControllerState
{

    private long allGemsHaltedTimeStamp;

    private int delayBeforeNextCrush;

    private CrushState crushState;


    public WaitNextCrushState(Environment environment, long time)
    {
        this.allGemsHaltedTimeStamp = time;
        this.delayBeforeNextCrush = environment.getConfig().getInteger(
            "DelayBetweenCrushes");
        this.crushState = new CrushState(environment);
    }


    public WaitNextCrushState(Environment environment, long time,
        AbstractControllerState returnState)
    {
        this.allGemsHaltedTimeStamp = time;
        this.delayBeforeNextCrush = environment.getConfig().getInteger(
            "DelayBetweenCrushes");
        this.crushState = new CrushState(environment, returnState);
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("WaitNextCrush");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        if(allGemsHaltedTimeStamp + delayBeforeNextCrush <= timer)
        {
            return crushState.update(timer, gridController);
        }

        return this;
    }

}
