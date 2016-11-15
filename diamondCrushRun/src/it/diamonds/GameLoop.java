package it.diamonds;


import static it.diamonds.engine.video.LayerType.Opaque;
import static it.diamonds.engine.video.LayerType.Transparent;
import it.diamonds.engine.Environment;
import it.diamonds.engine.RandomGenerator;
import it.diamonds.engine.TimerInterface;
import it.diamonds.engine.input.Event;
import it.diamonds.engine.input.EventMappings;
import it.diamonds.engine.input.Input;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.input.InputListenerInterface;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.video.Background;
import it.diamonds.engine.video.LayerManager;
import it.diamonds.handlers.EscapeCommandHandler;
import it.diamonds.menu.MainMenu;
import it.diamonds.menu.MenuItem;
import it.diamonds.playfield.PlayField;
import it.diamonds.playfield.PlayFieldDescriptor;

import java.io.IOException;


public final class GameLoop implements InputListenerInterface
{
    private static final String GFX = "gfx/";

    private static final String COMMON = GFX + "common/";

    private static final String COMMON_CRUSH = COMMON + "crush/";

    private static final String DROPPABLES = GFX + "droppables/";

    private static final String BOXES = DROPPABLES + "boxes/";

    private static final String FLASHING = DROPPABLES + "flashing/";

    private static final String GEMS = DROPPABLES + "gems/";

    private static final String STONES = DROPPABLES + "stones/";

    private static final String TILES = DROPPABLES + "tiles/";

    private static final String LAYOUT = GFX + "layout/";

    private static final String ICONS = LAYOUT + "icons/";

    // Total textures: 43
    private static final String TEXTURES_TO_PRELOAD[] = { "back000.jpg",
        "grid-background",

        COMMON + "font_14x29", COMMON + "font_8x8", COMMON + "gameover",
        COMMON + "main.jpg", COMMON + "main_menu", COMMON + "score_16x24",

        COMMON_CRUSH + "02", COMMON_CRUSH + "03", COMMON_CRUSH + "04",
        COMMON_CRUSH + "05", COMMON_CRUSH + "06", COMMON_CRUSH + "07",
        COMMON_CRUSH + "08", COMMON_CRUSH + "09", COMMON_CRUSH + "over",

        BOXES + "diamond", BOXES + "emerald", BOXES + "ruby",
        BOXES + "sapphire", BOXES + "topaz",

        FLASHING + "nocolor",

        GEMS + "diamond", GEMS + "emerald", GEMS + "ruby", GEMS + "sapphire",
        GEMS + "topaz",

        STONES + "diamond", STONES + "emerald", STONES + "ruby",
        STONES + "sapphire", STONES + "topaz",

        TILES + "diamond", TILES + "emerald", TILES + "ruby",
        TILES + "sapphire", TILES + "topaz",

        LAYOUT + "counter", LAYOUT + "warning",

        ICONS + "clock", ICONS + "desperation", ICONS + "tnt" };

    private Environment environment;

    private LayerManager layerManager;

    private EventMappings eventMappings;

    private PlayField playFieldOne;

    private PlayField playFieldTwo;

    private Input playerOneInput;

    private Input playerTwoInput;

    private boolean quit;

    private boolean isMenuRunning = true;

    private boolean inGameLoop;

    private long lastRender;

    private long haltOnGameOver;

    private long restartGameDelay;

    private boolean gameOver;

    private MainMenu mainMenu;

    private boolean menuActionExecuted;

    private long loopTimestamp;


    private GameLoop(Environment environment)
    {
        this(environment, new LayerManager());
    }


    private GameLoop(Environment environment, LayerManager layerManager)
    {
        this.environment = environment;
        this.layerManager = layerManager;

        loadTextures();

        playerOneInput = Input.create(environment.getKeyboard(),
            environment.getTimer());
        playerTwoInput = Input.create(environment.getKeyboard(),
            environment.getTimer());

        eventMappings = EventMappings.createForGameLoop(environment.getConfig());
        environment.getKeyboard().addListener(this);
        playerOneInput.setEventMappings(eventMappings);
        playerTwoInput.setEventMappings(eventMappings);

        layMenuBackground();

        restartGameDelay = environment.getConfig().getInteger(
            "RestartGameDelay");

        gameOver = false;
    }


    private void loadTextures()
    {
        for(String textureName : TEXTURES_TO_PRELOAD)
        {
            environment.getEngine().createImage(textureName);
        }
    }


    private void createPlayFieldOne(long timeStamp, long randomSeed)
    {
        playFieldOne = createPlayField(
            PlayFieldDescriptor.createForPlayerOne(environment),
            playerOneInput, timeStamp, randomSeed);
    }


    private void createPlayFieldTwo(long timeStamp, long randomSeed)
    {
        playFieldTwo = createPlayField(
            PlayFieldDescriptor.createForPlayerTwo(environment),
            playerTwoInput, timeStamp, randomSeed);
    }


    private void initPlayFields(TimerInterface timer)
    {
        long timeStamp = timer.getTime();
        long randomSeed = System.nanoTime();

        createPlayFieldOne(timeStamp, randomSeed);
        createPlayFieldTwo(timeStamp, randomSeed);

        playFieldOne.setOpponentPlayField(playFieldTwo);
        playFieldTwo.setOpponentPlayField(playFieldOne);
    }


    public static GameLoop create(Environment environment) throws IOException
    {
        return new GameLoop(environment);
    }


    public static GameLoop createForTesting(Environment environment,
        LayerManager layerManager) throws IOException
    {
        GameLoop gameLoop = new GameLoop(environment);
        gameLoop.reinitialize(layerManager);
        return gameLoop;
    }


    public static GameLoop createForTesting(Environment environment)
        throws IOException
    {
        return createForTesting(environment, new LayerManager());
    }


    private void layBackground()
    {
        Background background = new Background(environment, "back000", ".jpg");

        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(background);
        layerManager.closeCurrentLayer();
    }


    private void layMenuBackground()
    {
        Background background = new Background(environment, "gfx/common/main",
            ".jpg");

        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(background);
        layerManager.closeCurrentLayer();
    }


    private void setUpMainMenu()
    {
        mainMenu = new MainMenu(environment.getEngine());

        mainMenu.selectMenuItem(MenuItem.VERSUS_MODE);

        layerManager.openNewLayer(Transparent);
        layerManager.addToLayer(mainMenu.getSprite());
        layerManager.closeCurrentLayer();
    }


    /* FIXME: i seguenti 5 getters sono usati solo dai test - sono da __RIMUOVERE__ */
    public MainMenu getMainMenu()
    {
        return mainMenu;
    }


    public PlayField getPlayFieldOne()
    {
        return playFieldOne;
    }


    public PlayField getPlayFieldTwo()
    {
        return playFieldTwo;
    }


    public Input getPlayerOneInput()
    {
        return playerOneInput;
    }


    public Input getPlayerTwoInput()
    {
        return playerTwoInput;
    }


    private void updateFields()
    {
        if(gameOver)
        {
            processRestartDelay();
        }
        else
        {
            playFieldOne.update(loopTimestamp);
            playFieldTwo.update(loopTimestamp);

            checkAndShowGameOverMessage(playFieldOne);
            checkAndShowGameOverMessage(playFieldTwo);
        }
    }


    private void processRestartDelay()
    {
        if(restartDelayElapsed())
        {
            restart();
        }
    }


    private boolean restartDelayElapsed()
    {
        return environment.getTimer().getTime() >= haltOnGameOver
            + restartGameDelay;
    }


    private void checkAndShowGameOverMessage(PlayField playField)
    {
        if(playField.getGridController().isGameOver())
        {
            playField.showGameOverMessage();
            initRestartGame();
        }
    }


    private void restart()
    {
        gameOver = false;
        reinitialize(new LayerManager());
    }


    private void reinitialize(LayerManager layerManager)
    {
        this.layerManager = layerManager;

        layBackground();
        initPlayFields(environment.getTimer());

        playerOneInput.flushEvents();
        playerTwoInput.flushEvents();
    }


    private void initRestartGame()
    {
        haltOnGameOver = environment.getTimer().getTime();
        gameOver = true;
    }


    private void sleepOneMillisecond()
    {
        environment.getTimer().advance(1);
    }


    private void processInput()
    {
        environment.getKeyboard().update();

        if(!gameOver && inGameLoop)
        {
            playFieldOne.reactToInput(loopTimestamp);
            playFieldTwo.reactToInput(loopTimestamp);
        }
    }


    private void processWindow()
    {
        if(environment.getEngine().isWindowClosed())
        {
            quit = true;
            isMenuRunning = false;
        }
    }


    private void render()
    {
        long timeStamp = environment.getTimer().getTime();

        if(timeStamp - lastRender >= environment.getConfig().getInteger(
            "FrameRate"))
        {
            lastRender = timeStamp;

            environment.getEngine().clearDisplay();
            layerManager.drawLayers(environment.getEngine());
            environment.getEngine().updateDisplay();
        }
    }


    // FIXME: rendere privato il seguente metodo (oltre a GameLoop lo usano solo i test)
    public void initBeforeGameLoop()
    {
        inGameLoop = true;
        environment.getAudio().playMusic();
        restart();
        render();
        environment.getTimer().advance(500);
        playFieldOne.resetTimeStamps(environment.getTimer().getTime());
        playFieldTwo.resetTimeStamps(environment.getTimer().getTime());

        playerOneInput.setEventMappings(EventMappings.createForPlayerOne(environment.getConfig()));
        playerTwoInput.setEventMappings(EventMappings.createForPlayerTwo(environment.getConfig()));
    }


    public void loop()
    {
        if(!quit)
        {
            initBeforeGameLoop();

            while(!quit)
            {
                doOneStep();
            }
        }
    }


    public void menuLoop()
    {
        layMenuBackground();
        setUpMainMenu();

        while(isMenuRunning)
        {
            render();
            processInput();
            processWindow();
            sleepOneMillisecond();
        }
    }


    // FIXME: rendere privato il seguente metodo (oltre a GameLoop lo usano solo i test)
    public void doOneStep()
    {
        loopTimestamp = environment.getTimer().getTime();

        updateFields();

        render();
        processInput();
        processWindow();

        sleepOneMillisecond();
    }


    public void quit()
    {
        environment.getAudio().stopMusic();
        environment.shutDownAll();
    }


    // FIXME: rendere privato il seguente metodo (oltre a GameLoop lo usano solo i test)
    public PlayField createPlayField(PlayFieldDescriptor descriptor,
        Input input, long timeStamp, long randomSeed)
    {
        InputReactor inputReactor = new InputReactor(input,
            environment.getConfig().getInteger("NormalRepeatDelay"),
            environment.getConfig().getInteger("FastRepeatDelay"));

        inputReactor.addHandler(Code.ESCAPE, new EscapeCommandHandler(this));

        PlayField field;
        field = PlayField.createPlayField(environment, inputReactor,
            new RandomGenerator(randomSeed), descriptor);

        field.resetTimeStamps(timeStamp);
        field.fillLayerManager(layerManager);

        return field;
    }


    // FIXME: i 2 getters seguenti sono usati solo dai test: sono da rimuovere
    public boolean inGameLoop()
    {
        return inGameLoop;
    }


    public boolean isFinished()
    {
        return quit;
    }


    public void resetFinished()
    {
        quit = false;
    }


    // FIXME: questo getter e' usato solo dai test - e' da rimuovere
    public boolean isMenuFinished()
    {
        return !isMenuRunning;
    }


    public void stopLoop()
    {
        quit = true;
    }


    public void stopMenuLoop()
    {
        isMenuRunning = false;
    }


    public void notify(Event event)
    {
        Event translatedEvent = event.copyAndChange(eventMappings.translateEvent(event.getCode()));
        if(translatedEvent.isPressed())
        {
            if(translatedEvent.is(Code.DOWN))
            {
                mainMenu.selectNextItem();
            }
            if(translatedEvent.is(Code.UP))
            {
                mainMenu.selectPreviousItem();
            }
        }

        if((translatedEvent.is(Code.ESCAPE)) && inGameLoop)
        {
            quit = true;
        }

        if(translatedEvent.is(Code.ENTER) && translatedEvent.isReleased())
        {
            menuActionExecuted = true;
            mainMenu.executeSelectedItem(this);
        }
    }


    // FIXME: il seguente metodo e' usato solo dai test - va rimosso
    public boolean checkMenuActionExecuted()
    {
        return menuActionExecuted;
    }

}
