package it.diamonds.tests.droppable;


import static it.diamonds.droppable.DroppableColor.EMERALD;
import it.diamonds.droppable.Droppable;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.grid.Cell;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;

import java.io.IOException;


public class TestBigGemDrawing extends AbstractGridTestCase
{
    private AbstractEngine engine;


    public void setUp() throws IOException
    {
        super.setUp();
        engine = environment.getEngine();
    }


    public void testGridDrawsBigGem()
    {
        insertAndUpdate(createGem(EMERALD), 13, 1);
        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(EMERALD), 13, 3);
        insertAndUpdate(createGem(EMERALD), 12, 1);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createGem(EMERALD), 12, 3);
        insertAndUpdate(createGem(EMERALD), 11, 1);
        insertAndUpdate(createGem(EMERALD), 11, 2);
        insertAndUpdate(createGem(EMERALD), 11, 3);

        grid.updateBigGems();

        grid.draw(engine);

        assertEquals("GemGroup wasn't drawn correctly", 10,
            ((MockEngine)engine).getNumberOfQuadsDrawn());
        assertEquals("GemGroup must be drawn with the correct Texture",
            grid.getDroppableAt(13, 1).getSprite().getTexture().getName(),
            ((MockEngine)engine).getImage().getName());
    }


    public void testBigGemDrawnInCorrectPosition()
    {
        insert2x2BigGem(13, 5);

        grid.updateBigGems();

        grid.draw(engine);

        Droppable bottomRight = grid.getDroppableAt(13, 6);

        assertEquals("GemGroup wasn't drawn in correct position (X)",
            bottomRight.getSprite().getPosition().getX(),
            ((MockEngine)engine).getQuadPosition(3).getX());
        assertEquals("GemGroup wasn't drawn in correct position (Y)",
            bottomRight.getSprite().getPosition().getY(),
            ((MockEngine)engine).getQuadPosition(3).getY());
    }


    public void testBigGemDrawsCorrectTile()
    {
        insert2x2BigGem(13, 5);

        grid.updateBigGems();

        grid.draw(engine);

        assertEquals("bad texture drawn", Cell.SIZE * 2,
            ((MockEngine)engine).getImageRect().left());
        assertEquals("bad texture drawn", Cell.SIZE * 2,
            ((MockEngine)engine).getImageRect().top());
    }


    public void testDrawAfterVerticalMerge()
    {
        insert2x2BigGem(13, 5);
        grid.updateBigGems();

        insert2x2BigGem(11, 5);
        grid.updateBigGems();

        grid.draw(engine);

        assertEquals(456, ((MockEngine)engine).getQuadPosition().getY(), 0.001f);
    }


    private void insert2x2BigGem(int row, int column)
    {
        insertAndUpdate(createGem(EMERALD), row, column);
        insertAndUpdate(createGem(EMERALD), row, column + 1);
        insertAndUpdate(createGem(EMERALD), row - 1, column);
        insertAndUpdate(createGem(EMERALD), row - 1, column + 1);
    }


    public void testNonDroppedGems()
    {
        insertAndUpdate(createGem(EMERALD), 13, 4);
        insertAndUpdate(createGem(EMERALD), 13, 5);
        insertAndUpdate(createGem(EMERALD), 12, 4);
        insertAndUpdate(createGem(EMERALD), 12, 5);
        insertAndUpdate(createGem(EMERALD), 13, 6);
        insertAndUpdate(createGem(EMERALD), 11, 6);
        grid.updateBigGems();
        assertFalse(grid.isCellInAExtensibleObject(12, 6));
    }
}
