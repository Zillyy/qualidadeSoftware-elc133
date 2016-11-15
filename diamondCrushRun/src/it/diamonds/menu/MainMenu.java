package it.diamonds.menu;


import it.diamonds.GameLoop;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.Image;
import it.diamonds.engine.video.Sprite;


public class MainMenu
{
    private Sprite sprite;

    private MenuItem selectedMenuItem;

    private boolean menuActionEntered;


    public MainMenu(AbstractEngine engine)
    {
        Rectangle rectangle = new Rectangle(0, 0, 511, 59);
        Image texture = engine.createImage("gfx/common/main_menu");

        sprite = new Sprite(0f, 227f, rectangle, texture);

        MenuItem.initialiseMenuItems();

        selectedMenuItem = MenuItem.STORY_MODE;

        menuActionEntered = false;
    }


    public Sprite getSprite()
    {
        return sprite;
    }


    private void moveMenuSprite(int index)
    {
        int offSet = 60 * index;
        sprite.setOrigin(0, offSet);
        sprite.setPosition(0f, offSet + 227f);
    }


    public void selectMenuItem(MenuItem menuItem)
    {
        this.selectedMenuItem = menuItem;
        moveMenuSprite(menuItem.index());
    }


    public void selectNextItem()
    {
        selectedMenuItem = selectedMenuItem.nextItem();
        moveMenuSprite(selectedMenuItem.index());
    }


    public void selectPreviousItem()
    {
        selectedMenuItem = selectedMenuItem.previousItem();

        moveMenuSprite(selectedMenuItem.index());
    }


    public MenuItem getSelectedItem()
    {
        return selectedMenuItem;
    }


    public void executeSelectedItem(GameLoop loop)
    {
        menuActionEntered = true;
        selectedMenuItem.execute(loop);
    }


    public boolean wasMenuActionEntered()
    {
        return menuActionEntered;
    }

}
