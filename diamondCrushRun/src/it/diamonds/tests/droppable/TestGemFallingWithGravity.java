package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import static it.diamonds.droppable.DroppableColor.TOPAZ;
import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.pair.DroppablesPair;
import it.diamonds.grid.Cell;
import it.diamonds.tests.grid.AbstractGridTestCase;


public class TestGemFallingWithGravity extends AbstractGridTestCase
{
    public void testGemFallingWithGravity()
    {
        Droppable gem = createGem(EMERALD);

        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createChest(DIAMOND), 13, 3);
        insertAndUpdate(gem, 12, 2);

        float gemY = gem.getSprite().getPosition().getY();

        dropGemsPair();

        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY
            + (float)environment.getConfig().getInteger(
                "StrongestGravityMultiplier") * 0.5f,
            gem.getSprite().getPosition().getY(), 0.001f);
    }


    private void dropGemsPair()
    {
        DroppablesPair gemsPair = controller.getGemsPair();

        gemsPair.getSlave().getFallingObject().drop();
        gemsPair.getPivot().getFallingObject().drop();

        makeAllGemsFall();
    }


    public void testBigGemFallingWithGravity()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(createGem(TOPAZ), 12, 3);
        insertAndUpdate(createGem(TOPAZ), 12, 4);
        insertAndUpdate(createGem(TOPAZ), 11, 3);
        insertAndUpdate(createGem(TOPAZ), 11, 4);

        grid.updateBigGems();
        Droppable bigGem = grid.getDroppableAt(12, 3);

        float gemY = bigGem.getSprite().getPosition().getY();
        dropGemsPair();

        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY
            + (float)environment.getConfig().getInteger(
                "StrongestGravityMultiplier") * 0.5f,
            bigGem.getSprite().getPosition().getY(), 0.001f);
    }


    public void testBigGemFallingFromHigherWithGravity()
    {
        insert2x2BlockOfGems(DIAMOND, 12, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 5);

        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(EMERALD), 11, 4);
        insertAndUpdate(createGem(EMERALD), 10, 3);
        insertAndUpdate(createGem(EMERALD), 10, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(11, 3);

        float gemY = bigGem.getSprite().getPosition().getY();

        dropGemsPair();

        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY
            + (float)environment.getConfig().getInteger(
                "StrongestGravityMultiplier") * 0.5f,
            bigGem.getSprite().getPosition().getY(), 0.001f);
    }


    public void testGemFallingWithGravityToGridBottom()
    {
        Droppable gem = createGem(EMERALD);

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(gem, 12, 3);
        float gemY = grid.getDroppableAt(13, 4).getSprite().getPosition().getY();

        dropGemsPair();

        controller.update(environment.getTimer().getTime());

        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY,
            gem.getSprite().getPosition().getY(), 0.001f);
    }


    public void testGemFallingWithGravityFromHigherToGridBottom()
    {
        Droppable gem1 = createGem(EMERALD);
        Droppable gem2 = createGem(DIAMOND);

        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(gem2, 12, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(gem1, 11, 3);

        float gemY = grid.getDroppableAt(13, 4).getSprite().getPosition().getY();

        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));
        controller.getGemsPair().setPivot(gem1);
        controller.getGemsPair().setSlave(gem2);

        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY,
            gem1.getSprite().getPosition().getY(), 0.001f);
    }


    public void testBigGemFallingWithGravityToGridBottom()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 4);
        insertAndUpdate(createChest(DIAMOND), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 3);
        insertAndUpdate(createGem(EMERALD), 12, 4);
        insertAndUpdate(createGem(EMERALD), 11, 3);
        insertAndUpdate(createGem(EMERALD), 11, 4);

        grid.updateBigGems();

        Droppable bigGem = grid.getDroppableAt(11, 3);

        float gemY = grid.getRowUpperBound(12);

        dropGemsPair();
        makeAllGemsFall();

        controller.update(environment.getTimer().getTime());

        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());
        controller.update(environment.getTimer().getTime());

        assertEquals("Gem must be moved", gemY,
            bigGem.getSprite().getPosition().getY(), 0.001f);
    }


    public void testBigGemNotCreateWhileFall()
    {
        insertAndUpdate(createGem(RUBY), 13, 2);

        insertAndUpdate(createGem(EMERALD), 13, 4);
        insertAndUpdate(createGem(EMERALD), 13, 3);
        insertAndUpdate(createGem(EMERALD), 12, 4);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createChest(DIAMOND), 12, 2);
        insertAndUpdate(createGem(EMERALD), 11, 3);

        dropGemsPair();

        controller.update(environment.getTimer().getTime());
        Droppable droppable = grid.getDroppableAt(13, 3);

        assertSame("No big gem created", null, droppable.getExtensibleObject());
    }


    public void testDroppedGemCanMoveDown()
    {
        insertAndUpdate(createGem(RUBY), 13, 2);
        grid.setGravity(10);

        Droppable gem = createGem(EMERALD);
        insertAndUpdate(gem, 11, 2);

        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));

        gem.getSprite().setPosition(
            gem.getSprite().getPosition().getX(),
            grid.getDroppableAt(13, 2).getSprite().getPosition().getY()
                - Cell.SIZE - 2 * grid.getActualGravity());

        grid.updateDroppable(gem);

        assertEquals(12, gem.getCell().getTopRow());

        gem.getFallingObject().drop();

        assertTrue(droppedGemCanMoveDown(grid));
    }
}
