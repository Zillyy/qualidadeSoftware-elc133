package it.diamonds.tests.grid;


import it.diamonds.grid.Cell;
import junit.framework.TestCase;


public class TestCell extends TestCase
{
    private Cell cell;


    public void setUp()
    {
        cell = new Cell();
    }


    private void setDefaultCell()
    {
        cell.setRow(2);
        cell.setColumn(2);
    }


    public void testPosition()
    {
        cell.setRow(15);
        cell.setColumn(1);

        assertEquals("another row expected", 15, cell.getTopRow());
        assertEquals("another column expected", 1, cell.getLeftColumn());
    }


    public void testMovingDown()
    {
        cell.setRow(15);
        cell.setColumn(1);

        cell.setRow(cell.getTopRow() + 1);

        assertEquals("cell doesn't change as expected", 16, cell.getTopRow());
    }


    public void testRightColumn()
    {
        cell.setColumn(1);
        assertEquals(1, cell.getRightColumn());

        cell.setColumn(2);
        assertEquals(2, cell.getRightColumn());
    }


    public void testBottomRow()
    {
        cell.setRow(1);
        assertEquals(1, cell.getBottomRow());

        cell.setRow(2);
        assertEquals(2, cell.getBottomRow());
    }


    public void testDefaultWidth()
    {
        assertEquals(1, cell.getWidth());
    }


    public void testDefaultHeight()
    {
        assertEquals(1, cell.getHeight());
    }


    public void testResizeToContainRightOneColumn()
    {
        setDefaultCell();
        cell.resizeToContain(2, 3);
        assertEquals(2, cell.getLeftColumn());
        assertEquals(2, cell.getWidth());
    }


    public void testResizeToContainRightColumns()
    {
        setDefaultCell();
        cell.resizeToContain(2, 4);
        assertEquals(2, cell.getLeftColumn());
        assertEquals(3, cell.getWidth());
    }


    public void testResizeToContainLeftOneColumn()
    {
        setDefaultCell();
        cell.resizeToContain(2, 1);
        assertEquals(1, cell.getLeftColumn());
        assertEquals(2, cell.getWidth());
    }


    public void testResizeToContainLeftColumns()
    {
        setDefaultCell();
        cell.resizeToContain(2, 0);
        assertEquals(0, cell.getLeftColumn());
        assertEquals(3, cell.getWidth());
    }


    public void testResizeToContainBottomOneRow()
    {
        setDefaultCell();
        cell.resizeToContain(3, 2);
        assertEquals(2, cell.getTopRow());
        assertEquals(2, cell.getHeight());
    }


    public void testResizeToContainBottomRows()
    {
        setDefaultCell();
        cell.resizeToContain(4, 2);
        assertEquals(2, cell.getTopRow());
        assertEquals(3, cell.getHeight());
    }


    public void testResizeToContainTopOneRow()
    {
        setDefaultCell();
        cell.resizeToContain(1, 2);
        assertEquals(1, cell.getTopRow());
        assertEquals(2, cell.getHeight());
    }


    public void testResizeToContainTopRows()
    {
        setDefaultCell();
        cell.resizeToContain(0, 2);
        assertEquals(0, cell.getTopRow());
        assertEquals(3, cell.getHeight());
    }


    public void testNotContains()
    {
        setDefaultCell();
        assertFalse(cell.containsSingleCell(3, 3));
    }


    public void testContains()
    {
        setDefaultCell();
        assertTrue(cell.containsSingleCell(2, 2));
    }


    public void testNotContainsColumn()
    {
        setDefaultCell();
        assertFalse(cell.containsSingleCell(2, 3));
    }


    public void testNotContainsRow()
    {
        setDefaultCell();
        assertFalse(cell.containsSingleCell(3, 2));
    }


    public void testContainsExtendingColumn()
    {
        setDefaultCell();
        cell.resizeToContain(2, 3);
        assertTrue(cell.containsSingleCell(2, 3));
    }


    public void testContainsExtendingRow()
    {
        setDefaultCell();
        cell.resizeToContain(3, 2);
        assertTrue(cell.containsSingleCell(3, 2));
    }


    public void testContainsExtendingMoreColumn()
    {
        setDefaultCell();
        cell.resizeToContain(2, 4);
        assertTrue(cell.containsSingleCell(2, 3));
    }


    public void testContainsExtendingMoreRow()
    {
        setDefaultCell();
        cell.resizeToContain(4, 2);
        assertTrue(cell.containsSingleCell(3, 2));
    }

}
