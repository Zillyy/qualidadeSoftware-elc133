package it.diamonds.droppable;


public final class DroppableType
{
    public static final DroppableType GEM = new DroppableType("gems");

    public static final DroppableType CHEST = new DroppableType("boxes");

    public static final DroppableType STONE = new DroppableType("stones");

    public static final DroppableType FLASHING_GEM = new DroppableType(
        "flashing");

    private final String name;


    private DroppableType(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }


    public boolean isChest()
    {
        return this.equals(CHEST);
    }


    public boolean isFlashingGem()
    {
        return this.equals(FLASHING_GEM);
    }


    public boolean isStone()
    {
        return this.equals(STONE);
    }


    public boolean isGem()
    {
        return this.equals(GEM);
    }

}
