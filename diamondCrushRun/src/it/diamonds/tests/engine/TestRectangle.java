package it.diamonds.tests.engine;


import it.diamonds.engine.Rectangle;
import junit.framework.TestCase;


public class TestRectangle extends TestCase
{

    public void testRectangleWidth()
    {
        Rectangle rectangle = new Rectangle(10, 10, 19, 24);
        assertEquals(10, rectangle.getWidth());

        Rectangle anotherRectangle = new Rectangle(10, 10, 24, 49);
        assertEquals(15, anotherRectangle.getWidth());
    }


    public void testRectangleHeight()
    {
        Rectangle rectangle = new Rectangle(10, 10, 19, 24);
        assertEquals(15, rectangle.getHeight());

        Rectangle anotherRectangle = new Rectangle(10, 10, 24, 49);
        assertEquals(40, anotherRectangle.getHeight());
    }


    public void testRectangleLeft()
    {
        Rectangle rectangle = new Rectangle(10, 10, 20, 25);
        assertEquals(10, rectangle.left());
    }


    public void testRectangleRight()
    {
        Rectangle rectangle = new Rectangle(10, 10, 20, 25);
        assertEquals(20, rectangle.right());
    }


    public void testRectangleBottom()
    {
        Rectangle rectangle = new Rectangle(10, 10, 20, 25);
        assertEquals(25, rectangle.bottom());
    }


    public void testRectangleTop()
    {
        Rectangle rectangle = new Rectangle(10, 10, 20, 25);
        assertEquals(10, rectangle.top());
    }


    public void testRectangleEquals()
    {
        Rectangle rectangle1 = new Rectangle(10, 10, 20, 25);
        Rectangle rectangle2 = new Rectangle(10, 10, 20, 25);
        Rectangle rectangle3 = new Rectangle(0, 5, 10, 15);

        assertTrue("rectangle1 must equals rectangle2",
            rectangle1.equals(rectangle2));
        assertTrue("rectangle1 must not equals rectangle3",
            !rectangle1.equals(rectangle3));
        assertTrue("rectangle2 must not equals rectangle3",
            !rectangle2.equals(rectangle3));
    }


    public void testInvalidHorizontalArgument()
    {
        try
        {
            @SuppressWarnings("unused")
            Rectangle rectangle = new Rectangle(20, 10, 10, 25);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }

        fail("Invalid argument exception not thrown for horizontal argument");
    }


    public void testInvalidVerticalArgument()
    {
        try
        {
            @SuppressWarnings("unused")
            Rectangle rectangle = new Rectangle(10, 20, 20, 15);
        }
        catch(IllegalArgumentException e)
        {
            return;
        }

        fail("Invalid argument exception not thrown for vertical argument");
    }


    public void testResize()
    {
        Rectangle rectangle = new Rectangle(0, 5, 10, 15);
        Rectangle resizedRectangle = new Rectangle(0, 5, 20, 35);

        rectangle.resize(resizedRectangle.getWidth(),
            resizedRectangle.getHeight());

        assertEquals("wrong width", resizedRectangle.getWidth(),
            rectangle.getWidth());
        assertEquals("wrong height", resizedRectangle.getHeight(),
            rectangle.getHeight());

        assertTrue(rectangle.equals(resizedRectangle));
    }


    public void testTranslate()
    {
        Rectangle rectangle = new Rectangle(0, 5, 10, 15);
        Rectangle translatedRectangle = new Rectangle(10, 10, 20, 20);

        rectangle.translateTo(translatedRectangle.left(),
            translatedRectangle.top());

        assertEquals("wrong width", translatedRectangle.getWidth(),
            rectangle.getWidth());
        assertEquals("wrong height", translatedRectangle.getHeight(),
            rectangle.getHeight());

        assertTrue(rectangle.equals(translatedRectangle));
    }

}
