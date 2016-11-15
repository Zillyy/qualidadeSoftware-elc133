package it.diamonds.menu;


import it.diamonds.GameLoop;


public class MenuItem
{
    public static final MenuItem STORY_MODE = new MenuItem(0,
        new MenuActionNone(), "STORY MODE");

    public static final MenuItem VERSUS_MODE = new MenuItem(1,
        new MenuActionVersusMode(), "VERSUS MODE");

    public static final MenuItem ADVANCED_MODE = new MenuItem(2,
        new MenuActionNone(), "ADVANCED MODE");

    public static final MenuItem CUSTOM_MODE = new MenuItem(3,
        new MenuActionNone(), "CUSTOM MODE");

    public static final MenuItem OPTIONS = new MenuItem(4,
        new MenuActionNone(), "OPTIONS");

    public static final MenuItem QUIT = new MenuItem(5, new MenuActionQuit(),
        "QUIT");

    public static final MenuItem BACK = new MenuItem(6, new MenuActionNone(),
        "BACK");

    private int index;

    private String name;

    private MenuAction action;

    private MenuItem previousItem;

    private MenuItem nextItem;


    public MenuItem(int index, MenuAction action, String name)
    {
        this.index = index;
        this.action = action;
        this.name = name;
    }


    public final int index()
    {
        return this.index;
    }


    private MenuAction action()
    {
        return this.action;
    }


    public void execute(GameLoop gameLoop)
    {
        action().execute(gameLoop);
    }


    // Needed by JUnit to print out the name of the object
    public String toString()
    {
        return name;
    }


    public final MenuItem previousItem()
    {
        return previousItem;
    }


    public final MenuItem nextItem()
    {
        return nextItem;
    }


    public static void initialiseMenuItems()
    {
        MenuItem.VERSUS_MODE.previousItem = MenuItem.QUIT;
        MenuItem.VERSUS_MODE.nextItem = MenuItem.QUIT;

        MenuItem.QUIT.previousItem = MenuItem.VERSUS_MODE;
        MenuItem.QUIT.nextItem = MenuItem.VERSUS_MODE;
    }
}
