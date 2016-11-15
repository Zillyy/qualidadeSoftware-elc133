package it.diamonds.handlers;


import it.diamonds.GameLoop;
import it.diamonds.engine.input.AbstractEventHandler;
import it.diamonds.engine.input.InputReactor;


public class EscapeCommandHandler extends AbstractEventHandler
{
    private GameLoop gameLoop;


    public EscapeCommandHandler(GameLoop gameLoop)
    {
        this.gameLoop = gameLoop;
    }


    protected void executeWhenPressed(InputReactor inputReactor)
    {
        return;
    }


    protected void executeWhenReleased(InputReactor inputReactor)
    {
        gameLoop.stopLoop();
    }
}
