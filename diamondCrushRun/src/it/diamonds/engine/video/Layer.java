package it.diamonds.engine.video;


import java.util.ArrayList;


public class Layer extends ArrayList<DrawableInterface>
{
    static final long serialVersionUID = 123456793;


    public void drawLayer(AbstractEngine engine)
    {
        for(DrawableInterface item : this)
        {
            item.draw(engine);
        }
    }
}
