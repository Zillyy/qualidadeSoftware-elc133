package it.diamonds.tests.mocks;


import it.diamonds.engine.video.Image;


public final class MockImage implements Image
{
    private int width;

    private int height;

    private String name;

    private boolean loaded;


    private MockImage(String name, String otherType)
    {
        this.name = name;
        loadTextureFromFile(this.name + otherType);
    }


    public static MockImage create(String name, String otherType)
    {
        return new MockImage(name, otherType);
    }


    private void checkForSpecialCases(String fileName)
    {
        if(fileName.equals("back000.jpg"))
        {
            width = 1024;
            height = 1024;
        }
    }


    private void loadTextureFromFile(String fileName) throws RuntimeException
    {
        if(fileName.equals("this_texture_doesnt_exist.png"))
        {
            throw new RuntimeException(
                "this_texture_doesnt_exist.png file not found");
        }
        else if(fileName.equals("textureTest.png"))
        {
            throw new RuntimeException(
                "Not power of two dimensione for textureTest.png");
        }
        else
        {
            width = 64;
            height = 64;
            loaded = true;
            checkForSpecialCases(fileName);
        }
    }


    public boolean isLoaded()
    {
        return loaded;
    }


    public void cleanup()
    {
        loaded = false;
    }


    // I HATE CHECKSTYLE
    public String getName()
    {
        return name;
    }


    public int getWidth()
    {
        return width;
    }


    // I HATE CHECKSTYLE
    public int getHeight()
    {
        return height;
    }


    public void enable()
    {
    }

}
