package it.diamonds.engine.video;


import it.diamonds.engine.ComponentInterface;
import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;

import java.util.Hashtable;


public abstract class AbstractEngine implements DisplayInterface,
    ComponentInterface
{
    private Hashtable<String, Image> images = new Hashtable<String, Image>();


    public abstract int getDisplayWidth();


    public abstract int getDisplayHeight();


    public abstract void shutDown();


    public abstract boolean isWindowClosed();


    public abstract void setWindowTitle(String title);


    public abstract void updateDisplay();


    public abstract void clearDisplay();


    public int getPoolSize()
    {
        return images.size();
    }


    protected abstract Image createImage(String name, String type);


    public Image createImage(String name)
    {
        final String defaultType = ".png";

        Image image = images.get(name);

        if(image == null)
        {
            if(name.lastIndexOf('.') >= 0)
            {
                image = createImage(name, "");
            }
            else
            {
                image = createImage(name, defaultType);
            }

            images.put(name, image);
        }

        return image;
    }


    protected void cleanupImages()
    {
        for(Image image : images.values())
        {
            image.cleanup();
        }
        images.clear();
    }


    public abstract void drawImage(Point position, float width, float height,
        Image image, Rectangle imageRect);

}
