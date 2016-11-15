package it.diamonds.grid;


public class Cell
{
    public static final int SIZE = 32;

    private int left = -1;

    private int right = -1;

    private int top = 1;

    private int bottom = 1;


    public Cell(int left, int top, int right, int bottom)
    {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public Cell()
    {
        ;
    }


    public void setRow(int row)
    {
        bottom = row + this.getHeight() - 1;
        this.top = row;
    }


    public int getTopRow()
    {
        return top;
    }


    public int getBottomRow()
    {
        return bottom;
    }


    public void setColumn(int column)
    {
        right = column + this.getWidth() - 1;
        this.left = column;
    }


    public int getLeftColumn()
    {
        return left;
    }


    public int getRightColumn()
    {
        return right;
    }


    public int getWidth()
    {
        return right - left + 1;
    }


    public int getHeight()
    {
        return bottom - top + 1;
    }


    public void resizeToContain(int row, int column)
    {
        if(column < left)
        {
            left = column;
        }
        if(column > right)
        {
            right = column;
        }
        if(row < top)
        {
            top = row;
        }
        if(row > bottom)
        {
            bottom = row;
        }
    }


    public boolean containsSingleCell(int row, int column)
    {
        return row >= getTopRow() && row <= getBottomRow()
            && column >= getLeftColumn() && column <= getRightColumn();
    }


    public boolean equals(Cell cell)
    {
        return left == cell.left && top == cell.top && right == cell.right
            && bottom == cell.bottom;
    }


    @Override
    public boolean equals(Object arg0)
    {
        return equals((Cell)arg0);
    }
}
