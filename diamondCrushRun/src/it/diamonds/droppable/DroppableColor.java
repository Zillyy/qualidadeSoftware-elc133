package it.diamonds.droppable;


public final class DroppableColor
{
    public static final DroppableColor EMERALD = new DroppableColor("emerald",
        40);

    public static final DroppableColor RUBY = new DroppableColor("ruby", 50);

    public static final DroppableColor SAPPHIRE = new DroppableColor(
        "sapphire", 60);

    public static final DroppableColor TOPAZ = new DroppableColor("topaz", 80);

    public static final DroppableColor DIAMOND = new DroppableColor("diamond",
        100);

    public static final DroppableColor NO_COLOR = new DroppableColor("nocolor",
        0);

    private String name;

    private int score;


    private DroppableColor(String name, int score)
    {
        this.name = name;
        this.score = score;
    }


    public String getName()
    {
        return name;
    }


    public int getScore()
    {
        return score;
    }
}
