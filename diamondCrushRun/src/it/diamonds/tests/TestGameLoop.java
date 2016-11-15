package it.diamonds.tests;


import it.diamonds.GameLoop;
import it.diamonds.engine.Environment;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;
import it.diamonds.engine.video.Background;
import it.diamonds.engine.video.Image;
import it.diamonds.engine.video.LayerManager;
import it.diamonds.engine.video.Number;
import it.diamonds.menu.MainMenu;
import it.diamonds.menu.MenuItem;
import it.diamonds.tests.mocks.MockEngine;

import java.io.IOException;

import junit.framework.TestCase;


public class TestGameLoop extends TestCase
{
    private Environment environment;

    private LayerManager layerManager;

    private GameLoop gameLoop;


    public void setUp() throws IOException
    {
        environment = Environment.createForTesting(800, 600, "");
        layerManager = new LayerManager();
        gameLoop = GameLoop.createForTesting(environment, layerManager);
    }


    public void testEnvironmentFieldsInitialized()
    {
        assertNotNull(environment.getEngine());
        assertNotNull(environment.getAudio());
        assertNotNull(environment.getKeyboard());
        assertNotNull(environment.getTimer());
    }


    public void testIsPlayerOneInputSet()
    {
        assertNotNull(gameLoop.getPlayerOneInput());
    }


    public void testIsPlayerTwoInputSet()
    {
        assertNotNull(gameLoop.getPlayerTwoInput());
    }


    public void testPlayerFieldOneIsSet()
    {
        assertNotNull("PlayField two is null", gameLoop.getPlayFieldOne());
    }


    public void testPlayerFieldTwoIsSet()
    {
        assertNotNull("PlayField two is null", gameLoop.getPlayFieldTwo());
    }


    public void testPlayFieldsAreAttached()
    {
        assertEquals("playfield1 and opponent of playfield2 are not the same",
            gameLoop.getPlayFieldTwo().getOpponentPlayField(),
            gameLoop.getPlayFieldOne());

        assertEquals("playfield2 and opponent of playfield1 are not the same",
            gameLoop.getPlayFieldOne().getOpponentPlayField(),
            gameLoop.getPlayFieldTwo());
    }


    public void testMenuLoopFinished()
    {
        assertFalse("The Menu Loop must be running", gameLoop.isMenuFinished());
        gameLoop.stopMenuLoop();
        assertTrue("The Menu Loop must be stopped", gameLoop.isMenuFinished());
    }


    public void testGameLoopFinishing()
    {
        assertFalse("The GameLoop must be running", gameLoop.isFinished());
        gameLoop.stopLoop();
        assertTrue("The GameLoop must be stopped", gameLoop.isFinished());
    }


    /*
     * One of several tests that now reflects a behaviour verified in a specific menu
     * selection
     */
    public void testEscKeyMustBePressedAfterEnterKeyToWork() throws IOException
    {
        gameLoop.notify(Event.create(Code.ESCAPE, State.RELEASED));
        assertFalse(
            "The GameLoop must be running if key is pressed before start the game loop",
            gameLoop.isFinished());

        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        gameLoop.notify(Event.create(Code.ENTER, State.RELEASED));
        gameLoop.initBeforeGameLoop();
        gameLoop.notify(Event.create(Code.ESCAPE, State.RELEASED));

        assertTrue(
            "The GameLoop must be stopped if key is pressed before start the game loop",
            gameLoop.isFinished());
    }


    /*
     * One of several tests that now reflects a behaviour verified in a specific menu
     * selection
     */
    public void testMenuLoopFinishedAfterEnterPressed() throws IOException
    {
        assertFalse("The Menu Loop must be running", gameLoop.isMenuFinished());

        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        gameLoop.notify(Event.create(Code.ENTER, State.RELEASED));

        gameLoop.doOneStep();

        assertTrue("The Menu Loop must be stopped", gameLoop.isMenuFinished());
    }


    public void testMenuBackgroundIsShown()
    {
        gameLoop.menuLoop();

        layerManager.drawLayers(environment.getEngine());

        Background background = new Background(environment, "gfx/common/main",
            ".jpg");

        Image texture = background.getSprite().getTexture();

        assertTrue(((MockEngine)environment.getEngine()).wasImageDrawn(texture));
    }


    public void testMainMenuIsShown()
    {
        gameLoop.menuLoop();

        layerManager.drawLayers(environment.getEngine());

        Image texture = environment.getEngine().createImage(
            "gfx/common/main_menu");

        assertTrue(((MockEngine)environment.getEngine()).wasImageDrawn(texture));
    }


    public void testGameLoopStartedAfterMenuLoopStopped() throws IOException
    {
        assertFalse("The GameLoop must be stopped", gameLoop.inGameLoop());
        assertFalse("The Menu Loop must be running", gameLoop.isMenuFinished());

        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        gameLoop.notify(Event.create(Code.ENTER, State.RELEASED));

        gameLoop.initBeforeGameLoop();

        assertTrue("The GameLoop must be running", gameLoop.inGameLoop());
        assertTrue("The Menu Loop must be stopped", gameLoop.isMenuFinished());
    }


    public void testGameLoopFinishedAfterEscapePlayerOne() throws IOException
    {
        gameLoop.getPlayerOneInput().notify(
            Event.create(Code.ESCAPE, State.RELEASED));

        gameLoop.doOneStep();

        assertTrue("The GameLoop must be stopped", gameLoop.isFinished());
    }


    public void testGameLoopFinishAfterEscapePlayerTwo() throws IOException
    {
        gameLoop.getPlayerTwoInput().notify(
            Event.create(Code.ESCAPE, State.RELEASED));

        gameLoop.doOneStep();

        assertTrue("The GameLoop must be stopped", gameLoop.isFinished());
    }


    public void testWaitHalfSecondBeforeStartPlaying() throws IOException
    {
        gameLoop.getPlayerOneInput().notify(
            Event.create(Code.ESCAPE, State.RELEASED));
        gameLoop.loop();
        assertEquals(501, environment.getTimer().getTime());
        gameLoop.doOneStep();
        assertEquals(502, environment.getTimer().getTime());
    }


    public void testAllTexturesLoadedBeforeStartPlaying()
    {
        assertEquals(43, environment.getEngine().getPoolSize());
    }


    // TODO: cercare di correggere questo test
    /*
     * public void testGameOversAreDisplayingCorrectly() { MockEngine engine = new
     * MockEngine(); gameLoop.setEngine(engine); LayerManager layerManager =
     * gameLoop.getLayerManager(); gameLoop.doOneStep(); engine.clearDisplay();
     * layerManager.drawLayers(engine); int numberOfQuadsDrawn =
     * engine.getNumberOfQuadsDrawn();
     * gameLoop.getPlayFieldOne().getGridController().getGemsPair().getPivotGem().drop();
     * gameLoop.getPlayFieldOne().getGridController().getGemsPair().getSlaveGem().drop();
     * gameLoop.getPlayFieldTwo().getGridController().getGemsPair().getPivotGem().drop();
     * gameLoop.getPlayFieldTwo().getGridController().getGemsPair().getSlaveGem().drop();
     * gameLoop.doOneStep(); engine.clearDisplay(); layerManager.drawLayers(engine);
     * assertEquals("Game over message must be shown", numberOfQuadsDrawn + 2,
     * engine.getNumberOfQuadsDrawn()); }
     */

    public void testBackgroundMusicIsStoppingOnQuit() throws IOException
    {
        gameLoop.initBeforeGameLoop();
        assertTrue("Music must be playing",
            environment.getAudio().isMusicPlaying());
        gameLoop.quit();
        assertFalse("Music must be stopped",
            environment.getAudio().isMusicPlaying());
    }


    public void testBackgroundIsDrawnBeforeScore()
    {
        Background background = new Background(environment, "back000", ".jpg");

        layerManager.drawLayers(environment.getEngine());

        int scoreDrawOrder = ((MockEngine)environment.getEngine()).getImageDrawOrder(Number.create16x24(
            environment.getEngine(), 0.0f, 0.0f).getDigitSprite(0).getTexture());
        int backgroundDrawOrder = ((MockEngine)environment.getEngine()).getImageDrawOrder(background.getSprite().getTexture());

        assertTrue(scoreDrawOrder > backgroundDrawOrder);
    }


    public void testGetMainMenu()
    {
        assertNull("MainMenu must not be initialized before menuLoop",
            gameLoop.getMainMenu());
        gameLoop.menuLoop();
        assertNotNull("MainMenu must be initialized", gameLoop.getMainMenu());
    }


    public void testMainMenuReactionToDownInput()
    {
        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();

        assertEquals(MenuItem.VERSUS_MODE, mainMenu.getSelectedItem());

        gameLoop.notify(Event.create(Code.KEY_DOWN, State.PRESSED));
        assertEquals(MenuItem.QUIT, mainMenu.getSelectedItem());

        gameLoop.notify(Event.create(Code.KEY_DOWN, State.PRESSED));
        assertEquals(MenuItem.VERSUS_MODE, mainMenu.getSelectedItem());
    }


    public void testMainMenuReactionToUpInput()
    {
        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.QUIT);

        gameLoop.notify(Event.create(Code.KEY_UP, State.PRESSED));
        assertEquals(MenuItem.VERSUS_MODE, mainMenu.getSelectedItem());

        gameLoop.notify(Event.create(Code.KEY_UP, State.PRESSED));
        assertEquals(MenuItem.QUIT, mainMenu.getSelectedItem());

    }


    public void testFromLowerMenuItemToHigherMenuItem()
    {
        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.QUIT);

        gameLoop.notify(Event.create(Code.KEY_DOWN, State.PRESSED));
        assertEquals(MenuItem.VERSUS_MODE, mainMenu.getSelectedItem());
    }


    public void testFromHigherMenuItemToLowerMenuItem()
    {
        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        gameLoop.notify(Event.create(Code.KEY_UP, State.PRESSED));
        assertEquals(MenuItem.QUIT, mainMenu.getSelectedItem());
    }


    public void testChangeMenuItemPressAndRealease()
    {
        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();

        assertEquals(MenuItem.VERSUS_MODE/* STORY_MODE */,
            mainMenu.getSelectedItem());

        gameLoop.notify(Event.create(Code.KEY_DOWN, State.PRESSED));
        gameLoop.notify(Event.create(Code.KEY_DOWN, State.RELEASED));
        assertEquals(MenuItem.QUIT, mainMenu.getSelectedItem());

        gameLoop.notify(Event.create(Code.KEY_UP, State.PRESSED));
        gameLoop.notify(Event.create(Code.KEY_UP, State.RELEASED));
        assertEquals(MenuItem.VERSUS_MODE, mainMenu.getSelectedItem());
    }


    public void testSelectVersusModeMenuItem() throws IOException
    {
        assertFalse("The GameLoop must be stopped", gameLoop.inGameLoop());
        assertFalse("The Menu Loop must be running", gameLoop.isMenuFinished());

        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        gameLoop.notify(Event.create(Code.KEY_ENTER, State.PRESSED));
        gameLoop.notify(Event.create(Code.KEY_ENTER, State.RELEASED));

        assertTrue("The MenuAction method must have been entered",
            mainMenu.wasMenuActionEntered());
        assertTrue("The MenuAction must have returned successfully",
            gameLoop.checkMenuActionExecuted());
        assertTrue("The Menu Loop must be stopped", gameLoop.isMenuFinished());
        assertFalse("The GameLoop must be running", gameLoop.isFinished());

        gameLoop.initBeforeGameLoop();
        assertTrue("The GameLoop must be running", gameLoop.inGameLoop());
    }


    /*
     * // Maybe this test will be restored together with Advanced Mode // public void
     * testSelectAdvancedModeMenuItem() throws IOException { assertFalse("The GameLoop
     * must be stopped", gameLoop.inGameLoop()); assertFalse("The Menu Loop must be
     * running", gameLoop.isMenuFinished()); gameLoop.menuLoop(); MainMenu mainMenu =
     * gameLoop.getMainMenu(); mainMenu.selectMenuItem(MenuItem.ADVANCED_MODE);
     * gameLoop.notify(Event.create(Code.KEY_ENTER, State.PRESSED));
     * gameLoop.notify(Event.create(Code.KEY_ENTER, State.RELEASED)); assertTrue("The
     * MenuAction method must have been entered", mainMenu.wasMenuActionEntered());
     * assertTrue("The MenuAction must have returned successfully",
     * gameLoop.checkMenuActionExecuted()); assertTrue("The Menu Loop must be stopped",
     * gameLoop.isMenuFinished()); assertFalse("The GameLoop must be running",
     * gameLoop.isFinished()); gameLoop.initBeforeGameLoop(); assertTrue("The GameLoop
     * must be running", gameLoop.inGameLoop()); }
     */

    public void testSelectQuitMenuItem()
    {
        assertFalse("The GameLoop must be stopped", gameLoop.inGameLoop());
        assertFalse("The Menu Loop must be running", gameLoop.isMenuFinished());

        gameLoop.menuLoop();
        MainMenu mainMenu = gameLoop.getMainMenu();
        mainMenu.selectMenuItem(MenuItem.QUIT);

        gameLoop.notify(Event.create(Code.KEY_ENTER, State.PRESSED));
        gameLoop.notify(Event.create(Code.KEY_ENTER, State.RELEASED));

        assertTrue("The MenuAction method must have been entered",
            mainMenu.wasMenuActionEntered());
        assertTrue("The MenuAction must have returned successfully",
            gameLoop.checkMenuActionExecuted());
        assertTrue("The Menu Loop must be stopped", gameLoop.isMenuFinished());
        assertTrue("The GameLoop must be stopped", gameLoop.isFinished());
    }


    public void testPlayFieldTimeStampsResetInInitBeforeGameLoop()
        throws IOException
    {
        gameLoop.initBeforeGameLoop();

        gameLoop.getPlayFieldOne().reactToInput(
            environment.getTimer().getTime());
        gameLoop.getPlayFieldOne().reactToInput(
            environment.getTimer().getTime());

        assertEquals(0, gameLoop.getPlayFieldOne().wasInputServed());

        gameLoop.getPlayFieldTwo().reactToInput(
            environment.getTimer().getTime());
        gameLoop.getPlayFieldTwo().reactToInput(
            environment.getTimer().getTime());

        assertEquals(0, gameLoop.getPlayFieldTwo().wasInputServed());
    }


    public void testSleepHalfASecondInInitBeforeGameLoop() throws IOException
    {
        long timeStamp = environment.getTimer().getTime();

        gameLoop.initBeforeGameLoop();

        assertEquals(500, environment.getTimer().getTime() - timeStamp);
    }


    public void testFieldDrawnInInitBeforeGameLoop() throws IOException
    {
        environment.getTimer().advance(
            environment.getConfig().getInteger("FrameRate"));

        gameLoop.initBeforeGameLoop();

        assertTrue(((MockEngine)environment.getEngine()).getNumberOfQuadsDrawn() > 0);
    }

}
