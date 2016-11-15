package it.diamonds.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.DroppableColor.SAPPHIRE;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import it.diamonds.engine.RandomGenerator;


public class Pattern
{
    private static final int[] DEFAULT = { 1, 2, 2, 3, 3, 4, 4, 5 };

    private static final DroppableColor COLORS[] = { DIAMOND, RUBY, SAPPHIRE,
        EMERALD, TOPAZ };

    private DroppableColor[] colorsMapping;


    public Pattern(int baseIndex)
    {
        int colorsCount = COLORS.length;

        colorsMapping = new DroppableColor[colorsCount];

        for(int i = 0; i < colorsCount; i++)
        {
            colorsMapping[i] = COLORS[(baseIndex + i) % colorsCount];
        }
    }


    public Pattern()
    {
        this((new RandomGenerator()).extract(DEFAULT.length));
    }


    public DroppableColor getDroppableColor(int index)
    {
        return colorsMapping[DEFAULT[index] - 1];
    }


    public String toString()
    {
        String res = "";
        for(DroppableColor color : colorsMapping)
        {
            res += color.getName() + ", ";
        }
        return res;
    }

}
