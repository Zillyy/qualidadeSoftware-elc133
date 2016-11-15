package it.diamonds.tests.playfield;


import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.droppable.Droppable;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.video.LayerManager;
import it.diamonds.playfield.PlayField;
import it.diamonds.playfield.PlayFieldDescriptor;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;


public class TestIncomingStones extends AbstractGridTestCase
{
    private PlayField playField;

    private PlayField opponentPlayField;


    public void setUp() throws IOException
    {
        int[] randomSequence = { 99, 0 };

        super.setUp();

        InputReactor inputReactor = new InputReactor(Input.create(
            environment.getKeyboard(), environment.getTimer()),
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));
        playField = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(randomSequence),
            PlayFieldDescriptor.createForPlayerOne(environment));

        opponentPlayField = PlayField.createPlayField(environment,
            inputReactor, new MockRandomGenerator(randomSequence),
            PlayFieldDescriptor.createForPlayerTwo(environment));

        dropAndRemoveGemsPair(playField);
        dropAndRemoveGemsPair(opponentPlayField);

        opponentPlayField.setOpponentPlayField(playField);
        playField.setOpponentPlayField(opponentPlayField);
    }


    private void dropAndRemoveGemsPair(PlayField playField)
    {
        controller = playField.getGridController();
        controller.getGemsPair().getPivot().getFallingObject().drop();
        controller.getGemsPair().getSlave().getFallingObject().drop();

        grid = controller.getGrid();
        grid.removeDroppableFromGrid(grid.getDroppableAt(0, 4));
        grid.removeDroppableFromGrid(grid.getDroppableAt(1, 4));
    }


    private void insertAndUpdateInPlayField(PlayField playField, Droppable gem,
        int row, int column)
    {
        grid = playField.getGridController().getGrid();
        insertAndUpdate(gem, row, column);
    }


    private void update(PlayField playField)
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());
    }


    private void insertGemsIntoPlayFieldsForCounterAttack()
    {
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 11, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 10, 4);
    }


    public void testWarningBoxCrushNumberResetAfterDrop()
    {
        playField.addIncomingStones(3);
        assertEquals(3, playField.getWarningBox().getCounter());
        playField.getGridController().setIncomingStones(0);
        playField.addIncomingStones(4);
        assertEquals(4, playField.getWarningBox().getCounter());
    }


    public void testWarningBoxIsShown()
    {
        playField.addIncomingStones(1);
        assertFalse("The WarningBox must be shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxIsHideAfterPairGemsInsert()
    {
        playField.getWarningBox().show();

        playField.getGridController().insertNewGemsPair();

        update(playField);

        assertTrue("The WarningBox must be not shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxIsNotHiddenBeforeGemsPairInsert()
    {
        playField.addIncomingStones(1);
        playField.getWarningBox().show();
        playField.getGridController().insertNewGemsPair();
        update(playField);
        assertFalse("WarningBox is not shown after inserting a new gempair",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxHiddenAfterStonesFall()
    {
        playField.addIncomingStones(1);
        makeAllGemsFall();

        for(int i = 0; i < 42; i++)
        {
            update(playField);

        }
        assertFalse("WarningBox must be shown ",
            playField.getWarningBox().isHidden());
        update(playField);
        assertTrue("WarningBox must be hidden",
            playField.getWarningBox().isHidden());

    }


    public void testWarningBoxWithoutCrush()
    {
        playField.getWarningBox().hide();

        update(opponentPlayField);

        assertTrue("The WarningBox must be not shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxAfterTwoGemCrush()
    {
        playField.getWarningBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        update(opponentPlayField);

        assertFalse("The WarningBox must be shown",
            playField.getWarningBox().isHidden());
        assertEquals("WarningBox displays wrong number of crushes", 2,
            playField.getWarningBox().getCounter());
    }


    public void testWarningBoxIsNotShownAfterFlashCrush()
    {
        playField.getWarningBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createFlashingGem(), 11,
            4);

        update(opponentPlayField);

        assertTrue("The WarningBox must be not shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxScoreAfterOneCrush()
    {
        playField.getWarningBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 3);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 13,
            2);

        update(opponentPlayField);

        assertTrue("The WarningBox must be not shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxIsNotShownAfterChestCrush()
    {
        playField.getWarningBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 13,
            4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 12,
            4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        update(opponentPlayField);

        assertTrue("The WarningBox must be not shown",
            playField.getWarningBox().isHidden());
    }


    public void testWarningBoxAfterBigGemCrush()
    {
        playField.getWarningBox().hide();

        grid = opponentPlayField.getGridController().getGrid();

        insert2x2BlockOfGems(DIAMOND, 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        grid.updateBigGems();
        update(opponentPlayField);

        assertFalse("The WarningBox must be shown",
            playField.getWarningBox().isHidden());
        assertEquals("WarningBox displays wrong number of crushes", 8,
            playField.getWarningBox().getCounter());
    }


    public void testNoIncomingStoneIfCrushedGem()
    {
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 11, 4);

        update(opponentPlayField);
        update(playField);

        assertEquals("The number of stones must be 0", 0,
            playField.getGridController().getIncomingStones());
    }


    public void testIncomingStoneIfCrushedGem()
    {
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 12, 4);

        update(opponentPlayField);
        update(playField);

        assertEquals("The number of stones must be 2", 2,
            playField.getGridController().getIncomingStones());
    }


    public void testStonesToSendIfCrushedGem()
    {
        insertGemsIntoPlayFieldsForCounterAttack();

        update(opponentPlayField);
        update(playField);

        assertEquals("The number of stones must be 1", 1,
            opponentPlayField.getGridController().getIncomingStones());
        assertEquals("Stone to send must be reset", 0,
            playField.getGridController().getStonesToSend());
    }


    public void testCounterBoxShowWhenStonesToSendIfCrushedGem()
    {
        playField.getCounterBox().hide();

        insertGemsIntoPlayFieldsForCounterAttack();

        update(opponentPlayField);
        update(playField);

        assertFalse("The CounterBox must be shown",
            playField.getCounterBox().isHidden());

        environment.getTimer().advance(
            environment.getConfig().getInteger("counterBoxUpdateRate"));
        playField.update(environment.getTimer().getTime());

        assertTrue("The CounterBox must not be shown",
            playField.getCounterBox().isHidden());
    }


    public void testCounterBoxIsDrawn()
    {
        LayerManager layerManager = new LayerManager();

        playField.fillLayerManager(layerManager);
        layerManager.drawLayers(environment.getEngine());

        int numberOfQuadsDrawn = ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn();

        playField.getCounterBox().hide();

        insertGemsIntoPlayFieldsForCounterAttack();

        update(opponentPlayField);
        update(playField);

        environment.getEngine().clearDisplay();
        layerManager.drawLayers(environment.getEngine());

        assertEquals("CounterBox must be drawn", numberOfQuadsDrawn + 1 + 2,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testCounterShowCommand()
    {
        assertFalse("The CounterBox is not to Show",
            playField.getGridController().isCounterBoxToShow());
        playField.getGridController().setCounterBoxVisibility(true);
        assertTrue("The CounterBox is to Show",
            playField.getGridController().isCounterBoxToShow());
        playField.getGridController().setCounterBoxVisibility(false);
        assertFalse("The CounterBox is not to Show",
            playField.getGridController().isCounterBoxToShow());
    }


    public void testCounterBoxNotShowIfOnlyCrushedGem()
    {
        playField.getCounterBox().hide();

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 11, 4);
        update(playField);

        assertTrue("The CounterBox must not be shown",
            playField.getCounterBox().isHidden());
    }


    public void testCounterBoxShowIfEqualsCrushedGem()
    {
        playField.getCounterBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 11,
            4);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 11, 4);

        update(opponentPlayField);
        update(playField);

        assertFalse("The CounterBox must be shown",
            playField.getCounterBox().isHidden());
    }


    public void testCounterBoxShowsIfIncomingStonesDecrease()
    {
        playField.getCounterBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 11, 4);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 10,
            4);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 4);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 4);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 11, 4);

        update(opponentPlayField);
        update(playField);

        assertFalse("The CounterBox must be shown",
            playField.getCounterBox().isHidden());
    }


    public void testWarningBoxNotShowsIfIncomingStonesDecrease()
    {
        playField.getCounterBox().hide();

        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 13, 6);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 12, 6);
        insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND), 11, 6);
        insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND), 10,
            6);

        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 13, 6);
        insertAndUpdateInPlayField(playField, createGem(DIAMOND), 12, 6);
        insertAndUpdateInPlayField(playField, createChest(DIAMOND), 11, 6);

        update(opponentPlayField);
        update(playField);

        assertTrue("The WarningBox must be hidden",
            playField.getWarningBox().isHidden());
    }


    private void makeAllGemsFall(PlayField playField)
    {
        while(droppedGemCanMoveDown(playField.getGridController().getGrid()))
        {
            update(playField);
        }
        update(playField);
    }


    public void testPatternDoesNotChange()
    {
        for(int i = 0; i < 2; i++)
        {
            insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND),
                13, 1);
            insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND),
                12, 1);
            insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND),
                13, 2);
            insertAndUpdateInPlayField(opponentPlayField, createGem(DIAMOND),
                12, 2);
            insertAndUpdateInPlayField(opponentPlayField, createChest(DIAMOND),
                11, 1);

            if(droppedGemCanMoveDown(opponentPlayField.getGridController().getGrid()))
            {
                makeAllGemsFall(opponentPlayField);
                update(playField);
                makeAllGemsFall(playField);
            }
            else
            {
                update(opponentPlayField);
                update(playField);
            }

            makeAllGemsFall(playField);
            update(opponentPlayField);
        }

        for(int column = 0; column < 4; column++)
        {

            Droppable stone1 = playField.getGridController().getGrid().getDroppableAt(
                13, column);
            Droppable stone2 = playField.getGridController().getGrid().getDroppableAt(
                12, column);

            assertEquals("The Pattern in two insertion must be equals",
                stone1.getGridObject().getColor(),
                stone2.getGridObject().getColor());
        }
    }

    /*
     * TODO: sistemare al più presto questo test: in teoria attualmente NON DEVE fallire!
     */

    /*
     * TODO: questo test deve essere trasferito in TestStoneFallState e aggiornato a
     * dovere.
     */

    /*
     * public void testStoneNumberOfFrame() { ArrayList<Droppable> stones = new ArrayList<Droppable>(8);
     * stones.add(Stone.create(DroppableColor.EMERALD)); controller.insertStones(0,
     * stones); Droppable stone = grid.getGemAt(0, 0); assertEquals(8,
     * stone.getNumberOfFrames()); try { for (int frame = 0; frame < 8; frame++) {
     * stone.setCurrentFrame(frame); } } catch(Exception e) { fail("the animation frame is
     * not initialized correctly"); } }
     */

}
