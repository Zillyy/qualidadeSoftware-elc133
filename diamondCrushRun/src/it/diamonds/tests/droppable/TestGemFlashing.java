package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import static it.diamonds.droppable.DroppableType.CHEST;
import static it.diamonds.droppable.DroppableType.FLASHING_GEM;
import static it.diamonds.droppable.DroppableType.GEM;
import it.diamonds.tests.grid.AbstractGridTestCase;

import java.io.IOException;


public class TestGemFlashing extends AbstractGridTestCase
{

    public void setUp() throws IOException
    {
        super.setUp();

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        grid.removeDroppableFromGrid(controller.getGemsPair().getSlave());
    }


    public void testFlashDeleteOrderDown()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 1);
        insertAndUpdate(createFlashingGem(), 12, 1);
        insertAndUpdate(createGem(TOPAZ), 11, 1);

        insertAndUpdate(createGem(EMERALD), 13, 0);
        insertAndUpdate(createGem(EMERALD), 12, 0);

        insertAndUpdate(createGem(RUBY), 13, 2);
        insertAndUpdate(createGem(RUBY), 12, 2);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertEquals(grid.getDroppableAt(12, 1).getGridObject().getColor(),
            TOPAZ);
        assertNull(grid.getDroppableAt(13, 1));

        assertEquals(grid.getDroppableAt(13, 0).getGridObject().getColor(),
            EMERALD);
        assertEquals(grid.getDroppableAt(12, 0).getGridObject().getColor(),
            EMERALD);
        assertEquals(grid.getDroppableAt(13, 2).getGridObject().getColor(),
            RUBY);
        assertEquals(grid.getDroppableAt(12, 2).getGridObject().getColor(),
            RUBY);
    }


    public void testFlashDeleteOrderSx()
    {
        insertAndUpdate(createFlashingGem(), 13, 1);
        insertAndUpdate(createGem(TOPAZ), 12, 1);

        insertAndUpdate(createGem(EMERALD), 13, 0);

        insertAndUpdate(createGem(RUBY), 13, 2);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertEquals(grid.getDroppableAt(13, 1).getGridObject().getColor(),
            TOPAZ);
        assertNull(grid.getDroppableAt(12, 1));

        assertNull(grid.getDroppableAt(13, 0));

        assertEquals(grid.getDroppableAt(13, 2).getGridObject().getColor(),
            RUBY);
    }


    public void testFlashDeleteOrderDx()
    {
        insertAndUpdate(createFlashingGem(), 13, 1);
        insertAndUpdate(createGem(TOPAZ), 12, 1);

        insertAndUpdate(createGem(RUBY), 13, 2);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertEquals(grid.getDroppableAt(13, 1).getGridObject().getColor(),
            TOPAZ);
        assertNull(grid.getDroppableAt(12, 1));

        assertNull(grid.getDroppableAt(13, 2));
    }


    public void testFlashDeleteOrderUp()
    {
        insertAndUpdate(createFlashingGem(), 13, 1);
        insertAndUpdate(createGem(TOPAZ), 12, 1);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertNull(grid.getDroppableAt(13, 1));
        assertNull(grid.getDroppableAt(12, 1));
    }


    public void testFlashStartByChest()
    {
        insertAndUpdate(createChest(TOPAZ), 13, 1);
        insertAndUpdate(createFlashingGem(), 12, 1);
        insertAndUpdate(createChest(TOPAZ), 13, 7);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertNull(grid.getDroppableAt(13, 1));
        assertNull(grid.getDroppableAt(12, 1));
        assertNull(grid.getDroppableAt(13, 7));
    }


    public void testFlashDeleteChest()
    {
        insertAndUpdate(createGem(TOPAZ), 13, 1);
        insertAndUpdate(createFlashingGem(), 12, 1);

        insertAndUpdate(createChest(TOPAZ), 13, 7);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertNull(grid.getDroppableAt(13, 1));
        assertNull(grid.getDroppableAt(12, 1));
        assertNull(grid.getDroppableAt(13, 7));
    }


    public void testFlashNotDeleteNothing()
    {
        insertAndUpdate(createFlashingGem(), 13, 1);

        insertAndUpdate(createChest(TOPAZ), 13, 7);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(DIAMOND), 13, 5);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertEquals(grid.getDroppableAt(13, 1).getGridObject().getType(),
            FLASHING_GEM);
        assertEquals(grid.getDroppableAt(13, 7).getGridObject().getType(),
            CHEST);
        assertEquals(grid.getDroppableAt(13, 6).getGridObject().getType(), GEM);
        assertEquals(grid.getDroppableAt(13, 5).getGridObject().getType(), GEM);
    }


    /*
     * public void testFlashGemDeleteFlashGem() { insertAndUpdate(createFlashingGem(), 13,
     * 0); insertAndUpdate(createFlashingGem(), 12, 0);
     * insertAndUpdate(createGem(DIAMOND), 11, 0); insertAndUpdate(createGem(DIAMOND), 13,
     * 7); insertAndUpdate(createFlashingGem(), 13, 2); insertAndDropGemsPair();
     * makeAllGemsFall(); controller.update(environment.getTimer().getTime());
     * assertNull(grid.getDroppableAt(13, 2)); assertNull(grid.getDroppableAt(13, 0));
     * assertEquals(grid.getDroppableAt(12, 0).getGridObject().getColor(), DIAMOND);
     * assertEquals(grid.getDroppableAt(13, 7).getGridObject().getColor(), DIAMOND); }
     */

    public void testFlashGemStartOnBigGem()
    {
        insertBigGem(DIAMOND, 12, 0, 13, 1);
        insertAndUpdate(createFlashingGem(), 11, 0);

        insertAndUpdate(createGem(DIAMOND), 13, 7);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertNull(grid.getDroppableAt(13, 0));
        assertNull(grid.getDroppableAt(12, 0));
        assertNull(grid.getDroppableAt(13, 1));
        assertNull(grid.getDroppableAt(12, 1));
        assertNull(grid.getDroppableAt(11, 0));
        assertNull(grid.getDroppableAt(13, 7));

        assertNull(grid.getDroppableAt(13, 0));
    }


    public void testFlashGemDeleteBigGem()
    {
        insertBigGem(DIAMOND, 12, 0, 13, 1);

        insertAndUpdate(createGem(DIAMOND), 13, 7);
        insertAndUpdate(createFlashingGem(), 12, 7);

        insertAndDropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        assertNull(grid.getDroppableAt(13, 0));
        assertNull(grid.getDroppableAt(12, 0));
        assertNull(grid.getDroppableAt(13, 1));
        assertNull(grid.getDroppableAt(12, 1));
        assertNull(grid.getDroppableAt(12, 7));
        assertNull(grid.getDroppableAt(13, 7));

        assertNull(grid.getDroppableAt(13, 0));
    }

}
