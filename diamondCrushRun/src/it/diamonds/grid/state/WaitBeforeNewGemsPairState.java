package it.diamonds.grid.state;


import it.diamonds.engine.Environment;
import it.diamonds.grid.GridController;


public class WaitBeforeNewGemsPairState extends AbstractReactiveState
{
    private int newGemDelay;

    private long timeBase;

    private GemsPairOnControlState gemsPairOnControlState;

    private GameOverState gameOverState;


    public WaitBeforeNewGemsPairState(Environment environment, long time)
    {
        this.newGemDelay = environment.getConfig().getInteger("NewGemDelay");
        this.timeBase = time;
        this.gemsPairOnControlState = new GemsPairOnControlState(environment);
        this.gameOverState = new GameOverState();
    }


    public boolean isCurrentState(String stateName)
    {
        return stateName.equals("WaitBeforeNewGemsPair");
    }


    public AbstractControllerState update(long timer,
        GridController gridController)
    {
        if(timeBase + newGemDelay <= timer)
        {
            if(gridController.isCenterColumnFull())
            {
                return gameOverState;
            }
            gridController.insertNewGemsPair();

            return gemsPairOnControlState;
        }
        return this;
    }

}
