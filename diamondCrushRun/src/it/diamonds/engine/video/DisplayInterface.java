package it.diamonds.engine.video;


import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;


public interface DisplayInterface
{

    int getDisplayWidth();


    int getDisplayHeight();


    void shutDown();


    boolean isWindowClosed();


    void setWindowTitle(String title);


    void updateDisplay();


    void clearDisplay();


    void drawImage(Point position, float width, float height, Image image,
        Rectangle imageRect);

}
