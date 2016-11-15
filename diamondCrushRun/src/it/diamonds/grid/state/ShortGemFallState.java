package it.diamonds.grid.state;


import it.diamonds.engine.Environment;


public class ShortGemFallState implements GemFallStrategy
{

    private AbstractControllerState returnState;


    public ShortGemFallState(AbstractControllerState currentState)
    {
        this.returnState = currentState;
    }


    public AbstractControllerState returnState(Environment environment,
        long timer)
    {
        return new WaitNextCrushState(environment, timer, returnState);
    }

}
