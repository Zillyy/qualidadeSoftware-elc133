package it.diamonds.tests.engine.video;


import it.diamonds.engine.video.Frame;
import junit.framework.TestCase;


public class TestFrame extends TestCase
{
    public void testLength()
    {
        Frame frame1 = new Frame(10, 10, 1000);
        assertEquals("Frame length must be 1000", 1000, frame1.getLength());

        Frame frame2 = new Frame(10, 10, 2000);
        assertEquals("Frame length must be 2000", 2000, frame2.getLength());
    }


    public void testGetCoordinates()
    {
        Frame frame1 = new Frame(10, 20, 1000);
        assertEquals("erroneous value from getX", 10, frame1.getX());
        assertEquals("erroneous value from getY", 20, frame1.getY());

        Frame frame2 = new Frame(15, 5, 2000);
        assertEquals("erroneous value from getX", 15, frame2.getX());
        assertEquals("erroneous value from getX", 5, frame2.getY());
    }

}
