package it.diamonds.menu;


import it.diamonds.GameLoop;


public class MenuActionVersusMode implements MenuAction
{
    public void execute(GameLoop gameLoop)
    {
        gameLoop.resetFinished();
        gameLoop.stopMenuLoop();
    }
}
