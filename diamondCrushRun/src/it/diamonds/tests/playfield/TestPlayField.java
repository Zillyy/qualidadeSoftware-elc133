package it.diamonds.tests.playfield;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableColor;
import it.diamonds.droppable.DroppableType;
import it.diamonds.droppable.gems.Chest;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.engine.video.LayerManager;
import it.diamonds.grid.Grid;
import it.diamonds.playfield.PlayField;
import it.diamonds.playfield.PlayFieldDescriptor;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;


public class TestPlayField extends AbstractGridTestCase
{
    private static final int MOCK_SEQUENCE[] = { 99, 0, 99, 0 };

    private PlayField playField;

    private Input input;

    private Droppable pivot;


    public void setUp() throws IOException
    {
        super.setUp();

        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());

        InputReactor inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        playField = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(MOCK_SEQUENCE),
            PlayFieldDescriptor.createForTesting(environment));

        pivot = playField.getGridController().getGemsPair().getPivot();
    }


    public void testGridCreation()
    {
        Droppable slave = playField.getGridController().getGemsPair().getPivot();

        assertEquals("pivot gem must be a diamond", DroppableColor.DIAMOND,
            pivot.getGridObject().getColor());
        assertEquals("pivot must be a gem", DroppableType.GEM,
            pivot.getGridObject().getType());

        assertEquals("slave gem must be a diamond", DroppableColor.DIAMOND,
            slave.getGridObject().getColor());
        assertEquals("pivot must be a gem", DroppableType.GEM,
            slave.getGridObject().getType());
    }


    public void testNoInputReactionOnTimingNotElapsed()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate") - 1);
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem moved", 4, pivot.getCell().getLeftColumn());
    }


    public void testInputReactionOnTimingElapsed()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem did not move", 3, pivot.getCell().getLeftColumn());
    }


    public void testNoSecondInputReactionOnTimingNotElapsed()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate") - 1);
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem must be on column 3", 3,
            pivot.getCell().getLeftColumn());
    }


    public void testSecondInputReactionOnTimingElapsed()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem must be on column 2", 2,
            pivot.getCell().getLeftColumn());
    }


    public void testDoubleInputReactionOnTwoTimingElapsed()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate") * 2);
        playField.reactToInput(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem must be on column 1", 1,
            pivot.getCell().getLeftColumn());
    }


    public void testNoUpdateOnTimingNotElapsed()
    {
        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate") - 1);
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertFalse("gem moved", oldY < newY);
    }


    public void testUpdateOnTimingElapsed()
    {
        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertTrue("gem did not move", oldY < newY);
    }


    public void testNoSecondUpdateOnTimingNotElapsed()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate") - 1);
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertFalse("gem moved", oldY < newY);
    }


    public void testSecondUpdateOnTimingElapsed()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertTrue("gem did not move", oldY < newY);
    }


    public void testDoubleUpdateOnTwoTimingElapsed()
    {
        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        float yStep = oldY
            - playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();
        oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate") * 2);
        playField.update(environment.getTimer().getTime());
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertEquals("gem did not move two times", 2 * yStep, oldY - newY,
            0.001);
    }


    public void testInsertionInLayerManager()
    {
        LayerManager layerManager = new LayerManager();

        playField.fillLayerManager(layerManager);

        assertEquals("layers not filled", 7, layerManager.getLayersCount());
    }


    public void testGameOverMessageIsDrawn()
    {
        LayerManager layerManager = new LayerManager();

        playField.fillLayerManager(layerManager);

        layerManager.drawLayers(environment.getEngine());
        int numberOfQuadsDrawn = ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn();

        playField.showGameOverMessage();

        environment.getEngine().clearDisplay();
        layerManager.drawLayers(environment.getEngine());

        assertEquals("Game Over Box must be drawn", numberOfQuadsDrawn + 1,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testGameOverMessageIsDrawnOnce()
    {
        LayerManager layerManager = new LayerManager();

        playField.fillLayerManager(layerManager);

        layerManager.drawLayers(environment.getEngine());
        int numberOfQuadsDrawn = ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn();

        playField.showGameOverMessage();
        playField.showGameOverMessage();

        environment.getEngine().clearDisplay();
        layerManager.drawLayers(environment.getEngine());

        assertEquals("Game Over Box must be drawn", numberOfQuadsDrawn + 1,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testScoreChange()
    {
        Grid grid = playField.getGridController().getGrid();
        int initialScore = grid.computeTotalScore();

        Droppable drop = Gem.create(environment.getEngine(),
            DroppableColor.DIAMOND, 3500);
        drop.getFallingObject().drop();
        grid.insertDroppable(drop, 13, 3);

        drop = Chest.create(environment.getEngine(), DroppableColor.DIAMOND,
            3500);
        drop.getFallingObject().drop();
        grid.insertDroppable(drop, 13, 2);

        grid.updateCrushes();
        grid.closeChain();

        assertTrue("Total score was not correct",
            initialScore < grid.computeTotalScore());
    }


    public void testTimeStampsResetOnUpdate()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate"));
        playField.update(environment.getTimer().getTime());

        environment.getTimer().advance(1);
        playField.resetTimeStamps(environment.getTimer().getTime());

        float oldY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        environment.getTimer().advance(
            environment.getConfig().getInteger("UpdateRate") - 1);
        playField.update(environment.getTimer().getTime());

        float newY = playField.getGridController().getGemsPair().getPivot().getSprite().getPosition().getY();

        assertEquals("gem moved", oldY, newY, 0.001);
    }


    public void testTimeStampsResetOnInputReaction()
    {
        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(1);
        playField.resetTimeStamps(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));
        input.notify(Event.create(Code.LEFT, State.RELEASED));

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate") - 1);
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("gem must be on column 3", 3,
            pivot.getCell().getLeftColumn());
    }


    public void testInputServedInitialization()
    {
        assertEquals("inputServed must be 0", 0, playField.wasInputServed());
    }


    public void testInputServedAfterFirstInputReaction()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("inputServed must be 1", 1, playField.wasInputServed());
    }


    public void testInputServedBeforeFirstInputReaction()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate") - 1);
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("inputServed must be 0", 0, playField.wasInputServed());
    }


    public void testInputServedAfterSecondInputReaction()
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        environment.getTimer().advance(
            environment.getConfig().getInteger("InputRate"));
        playField.reactToInput(environment.getTimer().getTime());

        assertEquals("inputServed must be 2", 2, playField.wasInputServed());
    }


    public void testSetControllerIncomingStones()
    {
        playField.addIncomingStones(5);
        assertEquals(5, playField.getGridController().getIncomingStones());
    }


    public void testAdditiveIncomingStones()
    {
        playField.addIncomingStones(5);
        assertEquals(5, playField.getGridController().getIncomingStones());
        playField.addIncomingStones(5);
        assertEquals(10, playField.getGridController().getIncomingStones());
    }
}
