package it.diamonds.grid.state;


import it.diamonds.engine.Environment;


public class NormalGemFallState implements GemFallStrategy
{
    public AbstractControllerState returnState(Environment environment,
        long timer)
    {
        return new WaitNextCrushState(environment, timer);
    }

}
