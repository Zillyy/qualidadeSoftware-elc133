package it.diamonds.tests.grid;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.Rectangle;
import it.diamonds.engine.audio.Sound;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.grid.Grid;
import it.diamonds.grid.GridController;
import it.diamonds.tests.mocks.MockDroppableGenerator;

import java.io.IOException;


public class TestCellsideCollision extends AbstractGridTestCase
{
    private Rectangle bounds;

    private Droppable gem;

    private Input input;

    private InputReactor inputReactor;


    public void setUp() throws IOException
    {
        super.setUp();

        gem = Gem.createForTesting(environment.getEngine());
        input = Input.create(environment.getKeyboard(), environment.getTimer());
        input.setEventMappings(EventMappings.createForTesting());

        /*
         * TODO: check if inputReactor can be eliminated; in case, also remove
         * reinitialization for GridController
         */
        inputReactor = new InputReactor(input, 200, 50);

        bounds = new Rectangle(40, 40, 295, 487);
        grid = new Grid(environment, bounds);
        controller = new GridController(environment, grid, inputReactor,
            new MockDroppableGenerator(environment.getEngine()));

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        controller.getGemsPair().setPivot(controller.getGemsPair().getSlave());

        environment.getTimer().advance(inputReactor.getNormalRepeatDelay());
    }


    public void testGridGravity()
    {
        grid.setGravity(1.0f);
        grid.insertDroppable(gem, 11, 4);

        float oldPosY = (grid.getDroppableAt(11, 4)).getSprite().getPosition().getY();
        grid.updateDroppable(gem);

        assertEquals(gem.getSprite().getPosition().getY(), oldPosY + 1.0f,
            0.001f);

        grid.setGravity(2.0f);
        grid.updateDroppable(gem);
        assertEquals(gem.getSprite().getPosition().getY(), oldPosY + 3.0f);
    }


    public void testVerticalMoveByYStep()
    {
        grid.setGravity(32);
        grid.insertDroppable(gem, 1, 4);

        grid.updateDroppable(gem);
        assertTrue(!grid.isDroppableAt(1, 4));
        assertTrue(grid.isDroppableAt(2, 4));
    }


    public void testVerticalMoveNotRow()
    {
        grid.setGravity(1);
        grid.insertDroppable(gem, 11, 4);

        grid.updateDroppable(gem);
        grid.updateDroppable(gem);
        assertTrue(grid.isDroppableAt(12, 4) && !grid.isDroppableAt(13, 4));
    }


    public void testReactToInput()
    {
        float yStep = bounds.getHeight() / 14;

        grid.setGravity(yStep);
        grid.insertDroppable(gem, 11, 4);
        controller.getGemsPair().setPivot(gem);

        controller.update(environment.getTimer().getTime());
        inputReactor.reactToInput(environment.getTimer().getTime());

        input.notify(Event.create(Code.LEFT, State.PRESSED));

        controller.update(environment.getTimer().getTime());
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertTrue(grid.isDroppableAt(13, 3) && !grid.isDroppableAt(12, 3));
    }


    public void testHorizontalMove()
    {
        grid.setGravity(1.0f);
        grid.insertDroppable(gem, 11, 4);

        grid.updateDroppable(gem);
        inputReactor.reactToInput(environment.getTimer().getTime());

        float oldYPosition = gem.getSprite().getPosition().getY();
        input.notify(Event.create(Code.LEFT, State.PRESSED));

        grid.updateDroppable(gem);
        inputReactor.reactToInput(environment.getTimer().getTime());

        assertEquals(gem.getSprite().getPosition().getY(), oldYPosition + 1.0f,
            0.0001f);
    }


    public void testBottomCollision()
    {
        float yStep = bounds.getHeight() / 14;

        grid.setGravity(yStep);
        grid.insertDroppable(gem, 12, 4);

        float oldYPosition = gem.getSprite().getPosition().getY();

        grid.updateDroppable(gem);
        grid.updateDroppable(gem);

        assertTrue(grid.isDroppableAt(13, 4));
        assertEquals(
            grid.getDroppableAt(13, 4).getSprite().getPosition().getY(),
            oldYPosition + yStep, 0.0001f);
    }


    public void testBottomCollisionSound()
    {
        Sound sound = environment.getAudio().createSound("diamond");

        gem.getObjectWithCollisionSound().setCollisionSound(sound);

        grid.setGravity(1);
        grid.insertDroppable(gem, 13, 4);

        grid.updateDroppable(gem);

        assertTrue(sound.wasPlayed());
    }


    public void testMiddleNotCollisionSound()
    {
        Sound sound = environment.getAudio().createSound("diamond");
        float yStep = bounds.getHeight() / 14;

        gem.getObjectWithCollisionSound().setCollisionSound(sound);
        grid.setGravity(yStep);
        grid.insertDroppable(gem, 5, 4);

        grid.updateDroppable(gem);
        assertFalse(sound.wasPlayed());
    }
}
