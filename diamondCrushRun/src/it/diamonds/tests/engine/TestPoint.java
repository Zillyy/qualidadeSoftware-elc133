package it.diamonds.tests.engine;


import it.diamonds.engine.Point;
import junit.framework.TestCase;


public class TestPoint extends TestCase
{
    public void testConstruction()
    {
        Point point = new Point(10, 15);

        assertEquals(10, point.getX(), 0.0001f);
        assertEquals(15, point.getY(), 0.0001f);
    }


    public void testSetters()
    {
        Point point = new Point(5, 5);

        assertEquals(5, point.getX(), 0.0001f);
        assertEquals(5, point.getY(), 0.0001f);

        point.setX(10);
        point.setY(15);

        assertEquals(10, point.getX(), 0.0001f);
        assertEquals(15, point.getY(), 0.0001f);
    }


    public void testEquals()
    {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(10, 5);
        Point point3 = new Point(5, 10);

        assertTrue("point1 must equals point3", point1.equals(point3));
        assertFalse("point1 must not equals point2", point1.equals(point2));
    }


    public void testOverridenEquals()
    {
        Point point1 = new Point(5, 10);
        Point point2 = new Point(10, 5);
        Point point3 = new Point(5, 10);

        assertTrue("point1 must equals point3", point1.equals((Object)point3));
        assertFalse("point1 must not equals point2",
            point1.equals((Object)point2));
    }
}
