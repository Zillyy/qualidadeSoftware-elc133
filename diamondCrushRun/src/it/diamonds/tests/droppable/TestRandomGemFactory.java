package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.DroppableColor.SAPPHIRE;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import static it.diamonds.droppable.DroppableType.CHEST;
import static it.diamonds.droppable.DroppableType.GEM;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableType;
import it.diamonds.droppable.RandomDroppableFactory;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;


public class TestRandomGemFactory extends AbstractGridTestCase
{

    private RandomDroppableFactory factory;

    private final int randomIntArray[] = { 4, 0, 4, 0, 0, 1 };

    private int startFlash;

    private int endFlash;

    private int startChest;

    private int endChest;

    private int startGem;

    private int endGem;


    public void setUp() throws IOException
    {
        super.setUp();

        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(randomIntArray));

        int chestProb = environment.getConfig().getInteger("ChestProbability");
        int flashProb = environment.getConfig().getInteger("FlashProbability");

        startFlash = 0;
        endFlash = flashProb - 1;
        startChest = flashProb;
        endChest = flashProb + chestProb - 1;
        startGem = flashProb + chestProb;
        endGem = 99;

    }


    public void testRandomGemSequence()
    {
        Droppable gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type topaz", TOPAZ,
            gem.getGridObject().getColor());

        gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type diamond", DIAMOND,
            gem.getGridObject().getColor());

        gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type diamond", TOPAZ,
            gem.getGridObject().getColor());

        gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type ruby", DIAMOND,
            gem.getGridObject().getColor());

        gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type ruby", DIAMOND,
            gem.getGridObject().getColor());

        gem = factory.createRandomGem();
        assertEquals("does not return a Gem", GEM,
            gem.getGridObject().getType());
        assertEquals("does not return Gem of type sapphire", RUBY,
            gem.getGridObject().getColor());
    }


    public void testRandomChestSequence()
    {
        Droppable gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 1 does not return Chest of type topaz", TOPAZ,
            gem.getGridObject().getColor());

        gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 2 does not return Chest of type diamond",
            DIAMOND, gem.getGridObject().getColor());

        gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 3 does not return Chest of type ruby", RUBY,
            gem.getGridObject().getColor());

        gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 4 does not return Chest of type diamond",
            DIAMOND, gem.getGridObject().getColor());

        gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 5 does not return Chest of type ruby", RUBY,
            gem.getGridObject().getColor());

        gem = factory.createRandomChest();
        assertEquals("does not return a Chest", CHEST,
            gem.getGridObject().getType());
        assertEquals("extraction 6 does not return Chest of type sapphire",
            SAPPHIRE, gem.getGridObject().getColor());
    }


    public void testCorrectGemAndChestAndFlashProportion()
    {
        int[] percentages = { startGem, 1, endGem, 1, startFlash, startChest,
            1, endChest, 1, endFlash };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(percentages));

        Droppable gem = factory.createRandomDroppable();
        assertTrue("does not return a Gem",
            gem.getGridObject().getType().isGem());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Gem",
            gem.getGridObject().getType().isGem());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Flash",
            gem.getGridObject().getType().isFlashingGem());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Chest",
            gem.getGridObject().getType().isChest());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Chest",
            gem.getGridObject().getType().isChest());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Flash",
            gem.getGridObject().getType().isFlashingGem());

    }


    public void testNotTwoFlashSequence()
    {
        int indexes[] = { startFlash, endFlash };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        Droppable gem = factory.createRandomDroppable();
        assertTrue("does return a Flash",
            gem.getGridObject().getType().isFlashingGem());

        gem = factory.createRandomDroppable();
        assertFalse("does not return a Flash",
            gem.getGridObject().getType().isFlashingGem());
    }


    public void testSameIndexForGemAndChest()
    {
        int indexes[] = { 1, 0, 1 };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        Droppable gem = factory.createRandomGem();
        assertSame("The gem is not a ruby", gem.getGridObject().getColor(),
            RUBY);
        assertSame("The gem is not a gem", gem.getGridObject().getType(), GEM);

        factory.createRandomGem();

        gem = factory.createRandomChest();
        assertSame("The chest is not a chest", gem.getGridObject().getType(),
            CHEST);
        assertSame("It is not a Chest of type ruby",
            gem.getGridObject().getColor(), RUBY);
    }


    public void testNotTwoEqualChestSequence()
    {
        int indexes[] = { 2, 1 };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        assertNotSame("The two chest are of the same type",
            factory.createRandomChest().getGridObject().getColor(),
            factory.createRandomChest().getGridObject().getColor());
    }


    public void testFlashChestFlashSequence()
    {
        int indexes[] = { startFlash, startChest, 1, endFlash };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        Droppable gem = factory.createRandomDroppable();
        assertTrue("does return a Flash",
            gem.getGridObject().getType().isFlashingGem());

        gem = factory.createRandomDroppable();
        assertTrue("does not return a Flash",
            gem.getGridObject().getType().isChest());

        gem = factory.createRandomDroppable();
        assertTrue("does return a Flash",
            gem.getGridObject().getType().isFlashingGem());
    }


    public void testChestGemChestSequence()
    {
        int indexes[] = { startChest, 1, startGem, 4, endChest, 1 };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        DroppableType firstChest = factory.createRandomDroppable().getGridObject().getType();
        factory.createRandomDroppable().getGridObject().getType();
        DroppableType lastChest = factory.createRandomDroppable().getGridObject().getType();
        assertSame("first chest was not of same type of last chest",
            firstChest, lastChest);
    }


    public void testGemGemChestGemSequence()
    {
        int indexes[] = { startGem, 1, startGem, 2, startChest, 3, startGem, 1 };
        factory = RandomDroppableFactory.createForTesting(environment,
            new MockRandomGenerator(indexes));

        DroppableType firstGem = factory.createRandomDroppable().getGridObject().getType();
        factory.createRandomDroppable().getGridObject().getType();
        factory.createRandomDroppable().getGridObject().getType();
        DroppableType lastGem = factory.createRandomDroppable().getGridObject().getType();
        assertSame("first Gem was not of same type of last Gem", firstGem,
            lastGem);
    }

}
