package it.diamonds.tests.playfield;


import static it.diamonds.droppable.DroppableColor.EMERALD;
import static it.diamonds.droppable.DroppableColor.RUBY;
import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.playfield.CrushBox;
import it.diamonds.playfield.PlayField;
import it.diamonds.playfield.PlayFieldDescriptor;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;


public class TestCrushBox extends AbstractGridTestCase
{
    private static final int MOCK_SEQUENCE[] = { 0, 0, 0, 0 };

    private CrushBox crushBox;

    private PlayField playField;

    private InputReactor inputReactor;

    private int crushBoxSpeed;


    public void setUp() throws IOException
    {
        super.setUp();

        Input input = Input.create(environment.getKeyboard(),
            environment.getTimer());

        inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        playField = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(MOCK_SEQUENCE),
            PlayFieldDescriptor.createForPlayerOne(environment));

        controller = playField.getGridController();
        grid = controller.getGrid();

        crushBoxSpeed = environment.getConfig().getInteger("crushBoxSpeed");

        crushBox = CrushBox.create(environment.getEngine(), new Point(20, 20),
            crushBoxSpeed, environment.getConfig().getInteger("width"));
    }


    private void playFieldUpdateOfDelayBetweenCrushes()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("DelayBetweenCrushes"));
        playField.update(environment.getTimer().getTime());
    }


    public void testIsDrawn()
    {
        crushBox.show();
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes not correctly drawn", 1,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testIsDrawnInPlayField()
    {
        crushBox = playField.getCrushBox();
        crushBox.show();
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes not correctly drawn", 1,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testIsDrawnInCorrectPosition()
    {
        crushBox.show();
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes drawn in wrong position", new Point(20,
            20), ((MockEngine)environment.getEngine()).getQuadPosition());
    }


    public void testIsDrawnWithCorrectTexture()
    {
        crushBox.show();
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes drawn with wrong texture",
            "gfx/common/crush/02",
            ((MockEngine)environment.getEngine()).getImage().getName());

        crushBox.setCrushCounter(4);
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes drawn with wrong texture",
            "gfx/common/crush/04",
            ((MockEngine)environment.getEngine()).getImage().getName());
    }


    public void testSetCrushCounterBounds()
    {
        try
        {
            crushBox.setCrushCounter(1);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return;
        }

        fail("setCrushCounter of 1 not throw ArrayIndexOutOfBoundsException");

    }


    public void testIsDrawnWithCorrectRectangle()
    {
        crushBox.show();
        crushBox.draw(environment.getEngine());

        assertEquals("Number of crushes drawn with wrong Rectangle",
            new Rectangle(0, 0, 256, 64),
            ((MockEngine)environment.getEngine()).getImageRect());
    }


    public void testCrushBoxCounterWithoutCrush()
    {
        playField.update(environment.getTimer().getTime());

        assertTrue("CrushBox created without crushes",
            playField.getCrushBox().isHidden());
    }


    public void testCrushBoxCounterAfterTwoCrushes()
    {
        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(RUBY), 12, 2);
        insertAndUpdate(createChest(RUBY), 11, 2);
        insertAndUpdate(createChest(EMERALD), 10, 2);

        dropGemsPair();
        makeAllGemsFallByField();
        playFieldUpdateOfDelayBetweenCrushes();

        assertTrue(playField.getCrushBox().isHidden());

        makeAllGemsFallByField();
        playFieldUpdateOfDelayBetweenCrushes();

        assertFalse(playField.getCrushBox().isHidden());
        assertEquals("CrushBox non correctly updated", "gfx/common/crush/02",
            playField.getCrushBox().getTexture().getName());
    }


    protected void dropGemsPair()
    {
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();
    }


    protected void makeAllGemsFallByField()
    {
        makeAllGemsFall();
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());
    }


    public void testCrushBoxCounterPositionAfterTwoCrushes()
    {
        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(RUBY), 12, 2);
        insertAndUpdate(createChest(RUBY), 11, 2);
        insertAndUpdate(createChest(EMERALD), 10, 2);

        dropGemsPair();
        makeAllGemsFallByField();

        playFieldUpdateOfDelayBetweenCrushes();

        assertTrue(playField.getCrushBox().isHidden());

        makeAllGemsFallByField();
        playFieldUpdateOfDelayBetweenCrushes();

        assertFalse(playField.getCrushBox().isHidden());

        assertEquals("Wrong crushBox horizontal position",
            CrushBox.getOriginForPlayerOne().getX() - crushBoxSpeed,
            playField.getCrushBox().getPosition().getX());
        assertEquals("Wrong crushBox vertical position",
            CrushBox.getOriginForPlayerOne().getY(),
            playField.getCrushBox().getPosition().getY());
    }


    public void testCrushBoxCounterAfterThreeCrushes()
    {
        insertAndUpdate(createGem(RUBY), 13, 2);
        insertAndUpdate(createGem(EMERALD), 12, 2);
        insertAndUpdate(createGem(RUBY), 11, 2);
        insertAndUpdate(createChest(RUBY), 10, 2);
        insertAndUpdate(createChest(EMERALD), 9, 2);
        insertAndUpdate(createChest(RUBY), 8, 2);

        dropGemsPair();
        makeAllGemsFallByField();

        playFieldUpdateOfDelayBetweenCrushes();

        assertTrue(playField.getCrushBox().isHidden());

        makeAllGemsFallByField();

        playFieldUpdateOfDelayBetweenCrushes();

        assertFalse(playField.getCrushBox().isHidden());
        assertEquals("CrushBox non correctly updated", "gfx/common/crush/02",
            playField.getCrushBox().getTexture().getName());

        makeAllGemsFallByField();

        playFieldUpdateOfDelayBetweenCrushes();

        assertFalse(playField.getCrushBox().isHidden());
        assertEquals("CrushBox non correctly updated", "gfx/common/crush/03",
            playField.getCrushBox().getTexture().getName());
    }


    public void testCrushBoxCounterAfterTenCrushes()
    {
        crushBox.setCrushCounter(10);

        assertEquals("CrushBox non correctly updated", "gfx/common/crush/over",
            crushBox.getTexture().getName());
    }


    private void crushBoxMoveInit()
    {
        crushBox = playField.getCrushBox();

        insertAndUpdate(createGem(EMERALD), 13, 2);
        insertAndUpdate(createGem(RUBY), 12, 2);
        insertAndUpdate(createChest(RUBY), 11, 2);
        insertAndUpdate(createChest(EMERALD), 10, 2);

        dropGemsPair();
        makeAllGemsFallByField();
        playFieldUpdateOfDelayBetweenCrushes();
        assertTrue(crushBox.isHidden());

        makeAllGemsFallByField();
        playFieldUpdateOfDelayBetweenCrushes();
        assertFalse(crushBox.isHidden());

        int countMoves = environment.getConfig().getInteger("width");

        while(!crushBox.isHidden() && (countMoves >= 0))
        {
            playFieldUpdateOfDelayBetweenCrushes();
            countMoves--;
        }
    }


    public void testFirstPlayFieldCrushBoxMovingLeft()
    {
        crushBoxMoveInit();

        float lastX = crushBox.getPosition().getX();
        playFieldUpdateOfDelayBetweenCrushes();

        assertTrue("crushBox hasn't moved left",
            crushBox.getPosition().getX() < lastX);
    }


    public void testSecondPlayFieldCrushBoxMovingRight() throws IOException
    {
        playField = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(MOCK_SEQUENCE),
            PlayFieldDescriptor.createForPlayerTwo(environment));

        controller = playField.getGridController();
        grid = playField.getGridController().getGrid();
        crushBoxMoveInit();

        float lastX = crushBox.getPosition().getX();
        playFieldUpdateOfDelayBetweenCrushes();

        assertTrue("crushBox hasn't moved right",
            crushBox.getPosition().getX() > lastX);
    }


    public void testOffScreenCrushBoxIsNotMoving()
    {
        testFirstPlayFieldCrushBoxMovingLeft();
        int countMoves = environment.getConfig().getInteger("width");

        while(!crushBox.isOffScreen() && (countMoves >= 0))
        {
            playFieldUpdateOfDelayBetweenCrushes();
            countMoves--;
        }

        float lastX = crushBox.getPosition().getX();
        playFieldUpdateOfDelayBetweenCrushes();

        assertEquals("Off screen crushBox has moved!", lastX,
            crushBox.getPosition().getX());
    }


    /*
     * public void testCrushBoxZeroPulsesWhenCreated() { crushBox =
     * CrushBox.create(environment.getEngine(), new Point(2, 2), 10, 600);
     * crushBox.setPulsationLength(2); crushBox.setSizeMultiplier(4.0f); crushBox.show();
     * crushBox.startPulsing(); assertEquals(0, crushBox.getNumberOfPulses()); } public
     * void testCrushBoxPulseAtLeastOneTime() { crushBox =
     * CrushBox.create(environment.getEngine(), new Point(2, 2), 10, 600);
     * crushBox.setPulsationLength(6); crushBox.setSizeMultiplier(4.0f); crushBox.show();
     * crushBox.startPulsing(); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); assertEquals(0,
     * crushBox.getNumberOfPulses()); crushBox.draw(environment.getEngine());
     * assertEquals(1, crushBox.getNumberOfPulses()); } public void
     * testCrushBoxIsPulsingNTimes() { crushBox = CrushBox.create(environment.getEngine(),
     * new Point(2, 2), 10, 600); crushBox.setPulsationLength(7);
     * crushBox.setSizeMultiplier(4.0f); crushBox.show(); crushBox.startPulsing(); for(int
     * i = 0; i < 35; i++) { crushBox.draw(environment.getEngine()); } assertEquals(5,
     * crushBox.getNumberOfPulses()); for(int i = 0; i < 35; i++) {
     * crushBox.draw(environment.getEngine()); } assertEquals(10,
     * crushBox.getNumberOfPulses()); } public void
     * testCrushBoxIsNotPulsingAfterOnePulse() { crushBox = playField.getCrushBox();
     * crushBox.setPulsationLength(6); crushBox.setSizeMultiplier(4.0f); crushBox.show();
     * crushBox.startPulsing(); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); crushBox.draw(environment.getEngine());
     * crushBox.draw(environment.getEngine()); crushBox.draw(environment.getEngine());
     * assertEquals(1, crushBox.getNumberOfPulses()); insertAndUpdate(createGem(EMERALD),
     * 13, 2); insertAndUpdate(createGem(RUBY), 12, 2); insertAndUpdate(createChest(RUBY),
     * 11, 2); insertAndUpdate(createChest(EMERALD), 10, 2); stopGemsPair();
     * makeAllGemsFallByField(); playFieldUpdateOfDelayBetweenCrushes();
     * makeAllGemsFallByField(); playFieldUpdateOfDelayBetweenCrushes();
     * assertFalse(crushBox.isPulsing()); }
     */

    public void testOriginForPlayerOne()
    {
        Point originForPlayerOne = new Point(-50, 192);

        assertEquals(originForPlayerOne, CrushBox.getOriginForPlayerOne());
    }


    public void testOriginForPlayerTwo()
    {
        Point originForPlayerTwo = new Point(594, 192);

        assertEquals(originForPlayerTwo, CrushBox.getOriginForPlayerTwo());
    }
}
