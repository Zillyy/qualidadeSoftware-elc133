package it.diamonds.playfield;


import static it.diamonds.engine.video.LayerType.Opaque;
import static it.diamonds.engine.video.LayerType.Transparent;
import it.diamonds.engine.Environment;
import it.diamonds.engine.RandomGeneratorInterface;
import it.diamonds.engine.input.InputReactor;
import it.diamonds.engine.modifiers.SinglePulsation;
import it.diamonds.engine.video.DrawableInterface;
import it.diamonds.engine.video.LayerManager;
import it.diamonds.engine.video.Number;
import it.diamonds.grid.GridController;


public final class PlayField
{
    private GridController gridController;

    private long lastInputReactionTimeStamp = 0;

    private long lastUpdateTimeStamp = 0;

    private long lastCrushBoxUpdate;

    private long lastCounterBoxUpdate;

    private NextGemsPanel nextGemsPanel;

    private Number score;

    private int inputServed;

    private long sessionSeed;

    private int updateRate;

    private int inputRate;

    private int crushBoxUpdateRate;

    private int counterBoxUpdateRate;

    private float crushBoxSizeMultiplier;

    private int crushBoxPulsationLength;

    private boolean stopPulsingState;

    private CrushBox crushBox;

    private WarningBox warningBox;

    private CounterBox counterBox;

    private GameOverBox gameOverBox;

    private PlayField opponentPlayField;

    private boolean wasGameOverDrawn = false;


    private PlayField(Environment environment, InputReactor inputReactor,
        RandomGeneratorInterface randomGenerator,
        PlayFieldDescriptor descriptor, GridController gridController)
    {
        this.gridController = gridController;

        nextGemsPanel = new NextGemsPanel(gridController.getGemGenerator(),
            descriptor.nextGemsPanelOrigin());

        score = Number.create16x24(environment.getEngine(),
            descriptor.scoreOrigin().getX(), descriptor.scoreOrigin().getY());

        gameOverBox = new GameOverBox(environment.getEngine(),
            descriptor.getGameOverOrigin());

        sessionSeed = randomGenerator.getSeed();

        updateRate = environment.getConfig().getInteger("UpdateRate");
        inputRate = environment.getConfig().getInteger("InputRate");
        crushBoxUpdateRate = environment.getConfig().getInteger(
            "crushBoxUpdateRate");
        counterBoxUpdateRate = environment.getConfig().getInteger(
            "counterBoxUpdateRate");

        crushBox = descriptor.getCrushBox();
        warningBox = descriptor.getWarningBox();
        counterBox = descriptor.getCounterBox();

        crushBoxSizeMultiplier = (float)environment.getConfig().getInteger(
            "CrushBoxSizeMultiplier");
        crushBoxPulsationLength = environment.getConfig().getInteger(
            "CrushBoxPulsationLength");
        stopPulsingState = false;
    }


    public static PlayField createPlayField(Environment environment,
        InputReactor inputReactor, RandomGeneratorInterface randomGenerator,
        PlayFieldDescriptor descriptor)
    {
        GridController gridController = GridController.create(environment,
            inputReactor, randomGenerator, descriptor.gridOrigin());
        return new PlayField(environment, inputReactor, randomGenerator,
            descriptor, gridController);
    }


    public GridController getGridController()
    {
        return gridController;
    }


    public void update(long timestamp)
    {
        if(lastUpdateTimeStamp + updateRate <= timestamp)
        {
            updatePlayField(timestamp);

            /*
             * FIXME: perche' non assegnargli direttamente il valore di timestamp ?
             */
            lastUpdateTimeStamp += updateRate;

            score.setValue(getGridController().getGrid().computeTotalScore());
            updateWarningBox();
            updateCrushBox(timestamp);
            updateCounterBox(timestamp);
        }
    }


    public void addIncomingStones(int stonesNumber)
    {
        int incomingStones = gridController.getIncomingStones() + stonesNumber;
        gridController.setIncomingStones(incomingStones);
        warningBox.setCounter(incomingStones);
        warningBox.show();
    }


    private void updateWarningBox()
    {
        if(!warningBox.isHidden() && gridController.getIncomingStones() == 0)
        {
            warningBox.hide();
        }

        if(opponentPlayField != null)
        {
            int stonesToSend = gridController.getStonesToSend();
            if(stonesToSend != 0)
            {
                opponentPlayField.addIncomingStones(stonesToSend);
                gridController.setStonesToSend(0);
            }
        }
    }


    private void updateCounterBox(long timestamp)
    {
        if(gridController.isCounterBoxToShow())
        {
            warningBox.hide();
            counterBox.show();
            lastCounterBoxUpdate = timestamp;
            gridController.setCounterBoxVisibility(false);
        }

        if(timestamp >= lastCounterBoxUpdate + counterBoxUpdateRate)
        {
            counterBox.hide();
        }
    }


    /*
     * FIXME: le complessita' ciclomatiche non devono mai superare 3 -- 'sto metodo fa
     * ridere
     */
    private void updateCrushBox(long timestamp)
    {
        if(gridController.getGrid().getChainCounter() >= 2)
        {
            crushBox.setCrushCounter(gridController.getGrid().getChainCounter());

            if(!stopPulsingState)
            {
                crushBox.show();
                startCrushBoxPulsation();
            }

            lastCrushBoxUpdate = timestamp;
        }
        if(crushBox.getDrawModifier() != null)
        {
            if(crushBox.getDrawModifier().ended())
            {
                crushBox.removeDrawModifier();
                stopPulsingState = true;
            }
        }
        if(!crushBox.isHidden()
            && timestamp >= lastCrushBoxUpdate + crushBoxUpdateRate)
        {
            crushBox.hide();
            stopPulsingState = false;
        }
        else if(!crushBox.isOffScreen())
        {
            crushBox.updatePosition();
        }
    }


    private void startCrushBoxPulsation()
    {
        if(crushBox.getDrawModifier() == null)
        {
            crushBox.setDrawModifier(new SinglePulsation(crushBox,
                crushBoxPulsationLength, crushBoxSizeMultiplier));
        }
    }


    private void updatePlayField(long timestamp)
    {
        gridController.update(timestamp);

    }


    public void reactToInput(long timestamp)
    {
        if(timestamp >= lastInputReactionTimeStamp + inputRate)
        {
            gridController.reactToInput(timestamp);

            /*
             * FIXME: perche' non assegnargli direttamente il valore di timestamp ?
             */
            lastInputReactionTimeStamp += inputRate;

            ++inputServed;
        }
    }


    private void addTransparentLayer(LayerManager layerManager,
        DrawableInterface layer)
    {
        layerManager.openNewLayer(Transparent);
        layerManager.addToLayer(layer);
        layerManager.closeCurrentLayer();
    }


    public void fillLayerManager(LayerManager layerManager)
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(score);
        layerManager.closeCurrentLayer();

        addTransparentLayer(layerManager, gridController.getGrid());
        addTransparentLayer(layerManager, nextGemsPanel);
        addTransparentLayer(layerManager, warningBox);
        addTransparentLayer(layerManager, counterBox);
        addTransparentLayer(layerManager, crushBox);
        addTransparentLayer(layerManager, gameOverBox);
    }


    public void showGameOverMessage()
    {
        if(wasGameOverDrawn)
        {
            return;
        }

        gameOverBox.show();

        wasGameOverDrawn = true;
    }


    public void resetTimeStamps(long timeStamp)
    {
        lastUpdateTimeStamp = timeStamp;
        lastInputReactionTimeStamp = timeStamp;
    }


    public int wasInputServed()
    {
        return inputServed;
    }


    public long getSeed()
    {
        return sessionSeed;
    }


    public CrushBox getCrushBox()
    {
        return crushBox;
    }


    public CounterBox getCounterBox()
    {
        return counterBox;
    }


    public WarningBox getWarningBox()
    {
        return warningBox;
    }


    public PlayField getOpponentPlayField()
    {
        return opponentPlayField;
    }


    public void setOpponentPlayField(PlayField opponentPlayField)
    {
        this.opponentPlayField = opponentPlayField;
    }

}
