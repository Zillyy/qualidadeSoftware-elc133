package it.diamonds.menu;


import it.diamonds.GameLoop;


public class MenuActionQuit implements MenuAction
{
    public void execute(GameLoop gameLoop)
    {
        gameLoop.stopMenuLoop();
        gameLoop.stopLoop();
    }
}
