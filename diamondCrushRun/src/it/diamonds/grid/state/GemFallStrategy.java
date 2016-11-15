package it.diamonds.grid.state;


import it.diamonds.engine.Environment;


public interface GemFallStrategy
{
    AbstractControllerState returnState(Environment environment, long timer);
}
