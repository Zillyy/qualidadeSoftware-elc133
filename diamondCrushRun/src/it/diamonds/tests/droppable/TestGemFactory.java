package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.NO_COLOR;
import static it.diamonds.droppable.DroppableType.CHEST;
import static it.diamonds.droppable.DroppableType.FLASHING_GEM;
import static it.diamonds.droppable.DroppableType.GEM;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableFactory;
import it.diamonds.engine.Environment;
import junit.framework.TestCase;


public class TestGemFactory extends TestCase
{
    private DroppableFactory factory;


    public void setUp()
    {
        Environment environment = Environment.createForTesting(800, 600, "");
        factory = DroppableFactory.createForTesting(environment);
    }


    public void testGemCreation()
    {
        Droppable gem = factory.create(GEM, DIAMOND);
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type diamond", DIAMOND,
            gem.getGridObject().getColor());
    }


    public void testChestCreation()
    {
        Droppable gem = factory.create(CHEST, DIAMOND);
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("does not return Chest of type diamond", DIAMOND,
            gem.getGridObject().getColor());
    }


    public void testFlashGemCreation()
    {
        Droppable gem = factory.create(FLASHING_GEM, NO_COLOR);
        assertEquals("does not return Flashing gem of type diamond",
            FLASHING_GEM, gem.getGridObject().getType());

    }


    public void testCreateFlashingGem()
    {
        Droppable gem = factory.createFlashingGem();
        assertEquals("does not return Gem of type topaz", FLASHING_GEM,
            gem.getGridObject().getType());

    }


    public void testFlashingGemFirstFrameDuration()
    {
        Droppable gem = factory.createFlashingGem();
        assertEquals("Flashing gem first frame duration must be 0", 0,
            gem.getAnimatedObject().getFrameDuration(0));
    }


    public void testFlashingGemAnimationLenght()
    {
        Droppable gem = factory.createFlashingGem();
        assertEquals("Flashing gem animation lenght must be 8", 8,
            gem.getAnimatedObject().getNumberOfFrames());
    }
}
