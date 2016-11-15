package it.diamonds.tests.mocks;


import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.Image;

import java.util.ArrayList;
import java.util.List;


public final class MockEngine extends AbstractEngine
{
    private int width;

    private int height;

    private boolean shutDown = true;

    private class DrawInfo
    {
        private Rectangle imageRect;

        private Point quadPosition;

        private float quadWidth;

        private float quadHeight;

        private Image image;
    }

    private List<DrawInfo> drawInfoList = new ArrayList<DrawInfo>();

    private int numberOfQuadsDrawn = 0;


    public MockEngine(int width, int height)
    {
        this.width = width;
        this.height = height;
    }


    public int getNumberOfQuadsDrawn()
    {
        return numberOfQuadsDrawn;
    }


    private DrawInfo getDrawInfo(int i)
    {
        return drawInfoList.get(drawInfoList.size() - i - 1);
    }


    public int getDisplayWidth()
    {
        return width;
    }


    public int getDisplayHeight()
    {
        return height;
    }


    public Rectangle getImageRect()
    {
        return getImageRect(0);
    }


    public Rectangle getImageRect(int i)
    {
        return getDrawInfo(i).imageRect;
    }


    public Point getQuadPosition()
    {
        return getQuadPosition(0);
    }


    public Point getQuadPosition(int i)
    {
        return getDrawInfo(i).quadPosition;
    }


    public float getQuadWidth()
    {
        return getQuadWidth(0);
    }


    public float getQuadWidth(int i)
    {
        return getDrawInfo(i).quadWidth;
    }


    public float getQuadHeight()
    {
        return getQuadHeight(0);
    }


    public float getQuadHeight(int i)
    {
        return getDrawInfo(i).quadHeight;
    }


    public void shutDown()
    {
        cleanupImages();
        shutDown = true;
    }


    public boolean isWindowClosed()
    {
        return shutDown;
    }


    public void updateDisplay()
    {
        ;
    }


    // FIXME: questo metodo deve avere implementazione vuota
    public void clearDisplay()
    {
        numberOfQuadsDrawn = 0;
    }


    public Image getImage()
    {
        return getImage(0);
    }


    public Image getImage(int i)
    {
        return getDrawInfo(i).image;
    }


    public int getImageDrawOrder(Image image)
    {
        int drawOrder = 0;

        for(DrawInfo drawInfo : drawInfoList)
        {
            drawOrder++;

            if(drawInfo.image.getName() == image.getName())
            {
                return drawOrder;
            }
        }

        return 0;
    }


    protected Image createImage(String name, String otherType)
    {
        return MockImage.create(name, otherType);
    }


    public boolean wasImageDrawn(Image image)
    {
        return getImageDrawOrder(image) > 0;
    }


    public void drawImage(Point position, float width, float height,
        Image image, Rectangle imageRect)
    {

        DrawInfo drawInfo = new DrawInfo();

        drawInfo.imageRect = new Rectangle(imageRect.left(), imageRect.top(),
            imageRect.right(), imageRect.bottom());

        drawInfo.quadPosition = new Point(position.getX(), position.getY());

        drawInfo.quadWidth = width;
        drawInfo.quadHeight = height;

        drawInfo.image = image;

        drawInfoList.add(drawInfo);

        ++numberOfQuadsDrawn;
    }


    @Override
    public void setWindowTitle(String title)
    {
        // TODO Auto-generated method stub

    }

}
