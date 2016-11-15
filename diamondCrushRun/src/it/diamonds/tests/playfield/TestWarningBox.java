package it.diamonds.tests.playfield;


import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.playfield.PlayField;
import it.diamonds.playfield.PlayFieldDescriptor;
import it.diamonds.playfield.WarningBox;
import it.diamonds.tests.engine.AbstractEnvironmentTestCase;
import it.diamonds.tests.mocks.MockEngine;
import it.diamonds.tests.mocks.MockRandomGenerator;

import java.io.IOException;


public class TestWarningBox extends AbstractEnvironmentTestCase
{
    private static final int MOCK_SEQUENCE[] = { 12, 21, 43, 36 };

    private WarningBox warningBox;

    private PlayField playField1;

    private PlayField playField2;

    private Input input;


    public void setUp() throws IOException
    {
        super.setUp();

        input = Input.create(environment.getKeyboard(), environment.getTimer());

        InputReactor inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        PlayFieldDescriptor playerOnePlayFieldDescriptor = PlayFieldDescriptor.createForPlayerOne(environment);

        playField1 = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(MOCK_SEQUENCE),
            playerOnePlayFieldDescriptor);

        playField2 = PlayField.createPlayField(environment, inputReactor,
            new MockRandomGenerator(MOCK_SEQUENCE),
            PlayFieldDescriptor.createForPlayerTwo(environment));

        playField1.setOpponentPlayField(playField2);
        playField2.setOpponentPlayField(playField1);

        warningBox = playerOnePlayFieldDescriptor.getWarningBox();
    }


    public void testHiddenAfterCreation()
    {
        assertTrue("new WarningBox must be hidden", warningBox.isHidden());
    }


    public void testNumberCorrectlyDrawn()
    {
        warningBox.setCounter(2);
        warningBox.show();

        warningBox.draw(environment.getEngine());

        assertEquals("Number of crushes not correctly drawn", 2,
            ((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn());
    }


    public void testWarningBoxPosition()
    {

        assertEquals(
            "WarningBox displays number of crushes at the wrong horizontal position",
            (int)playField2.getWarningBox().getPosition().getX() - 54,
            playField2.getWarningBox().getCrushNumberX());
        assertEquals(
            "WarningBox displays number of crushes at the wrong vertical position",
            (int)playField2.getWarningBox().getPosition().getY() + 24,
            playField2.getWarningBox().getCrushNumberY());
    }
}
