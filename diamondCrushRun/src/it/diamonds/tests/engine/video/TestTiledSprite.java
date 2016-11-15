package it.diamonds.tests.engine.video;


import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.video.AbstractEngine;
import it.diamonds.engine.video.TiledSprite;
import it.diamonds.grid.Cell;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestTiledSprite extends TestCase
{
    private static final String GEMGROUP_PATH = "gfx/droppables/tiles/";

    private TiledSprite sprite;

    private AbstractEngine engine;

    private Cell dimensions = new Cell(3, 11, 5, 13); // 3x3

    private Point position = new Point(0, 0);


    protected void setUp() throws Exception
    {
        engine = new MockEngine(800, 600);
        sprite = new TiledSprite(position.getX(), position.getY(),
            engine.createImage(GEMGROUP_PATH + "diamond"), dimensions);
    }


    private int getNumberOfSingleCell(Cell cell)
    {
        return cell.getHeight() * cell.getWidth();
    }


    public void testTextureAreaHeight()
    {
        assertEquals("TextureArea height must be equals to Cell.SIZE",
            Cell.SIZE, sprite.getTextureArea().getHeight());
    }


    public void testTextureAreaWidth()
    {
        assertEquals(Cell.SIZE, sprite.getTextureArea().getWidth());
    }


    public void testNumberOfQuadsDrawnSameAsCellDimensions()
    {
        sprite.draw(engine);

        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();
        int singleCellNumber = getNumberOfSingleCell(dimensions);

        assertEquals(
            "NumberOfQuadsDrawn must be the same as number of single Cell in TiledSprite",
            singleCellNumber, quadsDrawn);
    }


    public void testNumberOfQuadsDrawnAsCellDimensionsChange()
    {
        sprite.draw(engine);
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        dimensions.resizeToContain(dimensions.getTopRow() - 1,
            dimensions.getRightColumn() + 1);

        sprite.draw(engine);
        quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn() - quadsDrawn;

        int singleCellNumber = getNumberOfSingleCell(dimensions);

        assertEquals(
            "NumberOfQuadsDrawn must be the same as number of single Cell in TiledSprite",
            singleCellNumber, quadsDrawn);
    }


    public void testTilesDrawInCells()
    {
        sprite.draw(engine);

        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        for(int i = 0; i < quadsDrawn; i++)
        {
            Point quadPosition = ((MockEngine)engine).getQuadPosition(quadsDrawn
                - 1 - i);

            int column = i % dimensions.getWidth();
            int row = (int)Math.ceil(i / dimensions.getWidth());

            assertEquals(column * Cell.SIZE, quadPosition.getX(), 0);
            assertEquals(row * Cell.SIZE, quadPosition.getY(), 0);
        }
    }


    public void testTileDrawPosition()
    {
        float posX = 5.2f;
        float posY = 2.5f;

        sprite.setPosition(posX, posY);

        sprite.draw(engine);

        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();
        Point quadPosition = ((MockEngine)engine).getQuadPosition(quadsDrawn - 1);

        assertEquals(posX, quadPosition.getX(), 0);
        assertEquals(posY, quadPosition.getY(), 0);
    }


    public void testSameXPosBeforeAndAfterDraw()
    {
        float posX = sprite.getPosition().getX();
        sprite.draw(engine);
        assertEquals(posX, sprite.getPosition().getX(), 0);
    }


    public void testSameYPosBeforeAndAfterDraw()
    {
        float posY = sprite.getPosition().getY();
        sprite.draw(engine);
        assertEquals(posY, sprite.getPosition().getY(), 0);
    }


    public void testBottomRightTileDraw()
    {
        sprite.draw(engine);

        int bottomRightIndex = (dimensions.getWidth() * dimensions.getHeight()) - 1;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - bottomRightIndex);

        assertEquals("Wrong ImageRect vertical position", Cell.SIZE * 2,
            imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", Cell.SIZE * 2,
            imageRect.left());
    }


    public void testRightTileDraw()
    {
        sprite.draw(engine);

        int rightIndex = (dimensions.getWidth() * (dimensions.getHeight() - 1)) - 1;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - rightIndex);

        assertEquals("Wrong ImageRect vertical position", Cell.SIZE,
            imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", Cell.SIZE * 2,
            imageRect.left());
    }


    public void testTopRightTileDraw()
    {
        sprite.draw(engine);

        int topRightIndex = (dimensions.getWidth() * 1) - 1;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - topRightIndex);

        assertEquals("Wrong ImageRect vertical position", 0, imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", Cell.SIZE * 2,
            imageRect.left());
    }


    public void testTopLeftTileDraw()
    {
        sprite.draw(engine);

        int topLeftIndex = (1 * 1) - 1;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - topLeftIndex);

        assertEquals("Wrong ImageRect vertical position", 0, imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", 0, imageRect.left());
    }


    public void testTopTileDraw()
    {
        sprite.draw(engine);

        int topIndex = ((dimensions.getWidth() - 1) * 1) - 1;
        ;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - topIndex);

        assertEquals("Wrong ImageRect vertical position", 0, imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", Cell.SIZE,
            imageRect.left());
    }


    public void testBrightDrawing()
    {
        int brightOffset = Cell.SIZE * 3;

        sprite.useBrighterImage();

        sprite.draw(engine);

        int topLeftIndex = (1 * 1) - 1;
        int quadsDrawn = ((MockEngine)engine).getNumberOfQuadsDrawn();

        Rectangle imageRect = ((MockEngine)engine).getImageRect(quadsDrawn - 1
            - topLeftIndex);

        assertEquals("Wrong ImageRect vertical position", 0, imageRect.top());
        assertEquals("Wrong ImageRect horizontal position", brightOffset,
            imageRect.left());
    }
}
