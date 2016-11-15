package it.diamonds.grid.state;


import it.diamonds.engine.input.InputReactorInterface;


public abstract class AbstractReactiveState extends AbstractControllerState
{
    public void reactToInput(InputReactorInterface input, long timer)
    {
        input.reactToInput(timer);
    }

}
