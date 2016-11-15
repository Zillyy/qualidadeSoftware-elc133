package it.diamonds.tests.droppable;


import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.Pattern;
import junit.framework.TestCase;


public class TestPattern extends TestCase
{
    private Pattern pattern;

    private DroppableColor[] colors;


    protected void setUp() throws Exception
    {
        super.setUp();

        pattern = new Pattern(1);
        colors = new DroppableColor[8];

        for(int i = 0; i < 8; i++)
        {
            colors[i] = pattern.getDroppableColor(i);
        }
    }


    public void testPatternCreation()
    {
        assertNotNull("A ColorType must be returned", colors[0]);
    }


    public void testDefaultPatternValues()
    {

        assertNotSame("1st e 2nd Color must be different", colors[0], colors[1]);

        assertNotSame("2nd e 4th Color must be different", colors[1], colors[3]);
        assertSame("2nd e 3rd Color must be equals", colors[1], colors[2]);
        assertNotSame("1st e 4th Color must be different", colors[0], colors[3]);

        assertNotSame("4th e 6th Color must be different", colors[3], colors[5]);
        assertSame("4th e 5th Color must be equals", colors[3], colors[4]);
        assertNotSame("1st e 6th Color must be different", colors[0], colors[5]);
        assertNotSame("2nd e 6th Color must be different", colors[1], colors[5]);

        assertNotSame("6th e 8th Color must be different", colors[5], colors[7]);
        assertSame("6th e 7th Color must be equals", colors[5], colors[6]);
        assertNotSame("1st e 8th Color must be different", colors[0], colors[7]);
        assertNotSame("2nd e 8th Color must be different", colors[1], colors[7]);
        assertNotSame("4th e 8th Color must be different", colors[3], colors[7]);
    }


    public void testNumberOfElements()
    {
        try
        {
            pattern.getDroppableColor(-1);
            fail("ArrayIndexOutOfBoundsException must be raised if index<0");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {

        }

        try
        {
            pattern.getDroppableColor(8);
            fail("ArrayIndexOutOfBoundsException must be raised if index>=8");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
        }
    }


    public void testNoColorIsNotPresent()
    {
        for(int index = 0; index < 8; index++)
        {
            if(colors[index].equals(DroppableColor.NO_COLOR))
            {
                fail("The NO_COLOR must not be present");
            }
        }
    }


    public void testDifferentMappingIfNewIstance()
    {
        Pattern newPattern = new Pattern(2);

        for(int i = 0; i < 8; i++)
        {
            assertNotSame("Colors must be different", colors[i],
                newPattern.getDroppableColor(i));
        }
    }
}
