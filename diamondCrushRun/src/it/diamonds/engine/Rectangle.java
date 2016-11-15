package it.diamonds.engine;


public class Rectangle
{
    private int left;

    private int top;

    private int right;

    private int bottom;


    public Rectangle(int left, int top, int right, int bottom)
    {
        if(left > right || top > bottom)
        {
            throw new IllegalArgumentException();
        }

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    public int getHeight()
    {
        return bottom - top + 1;
    }


    public int getWidth()
    {
        return right - left + 1;
    }


    public int top()
    {
        return top;
    }


    public int left()
    {
        return left;
    }


    public int right()
    {
        return right;
    }


    public int bottom()
    {
        return bottom;
    }


    public void resize(int width, int height)
    {
        bottom = top + height - 1;
        right = left + width - 1;
    }


    public void translateTo(int left, int top)
    {
        bottom = top + getHeight() - 1;
        right = left + getWidth() - 1;
        this.top = top;
        this.left = left;
    }


    public boolean equals(Rectangle rectangle)
    {
        return left == rectangle.left && top == rectangle.top
            && right == rectangle.right && bottom == rectangle.bottom;
    }


    public boolean equals(Object arg0)
    {
        return equals((Rectangle)arg0);
    }

}
