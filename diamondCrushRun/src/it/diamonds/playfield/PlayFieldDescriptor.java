package it.diamonds.playfield;


import it.diamonds.engine.Environment;
import it.diamonds.engine.Point;


/* TODO: questa classe non  testata */

public final class PlayFieldDescriptor
{
    private final Point gridOrigin;

    private final Point nextGemsPanelOrigin;

    private final Point scoreOrigin;

    private final Point gameOverOrigin;

    private final CrushBox crushBox;

    private final WarningBox warningBox;

    private final CounterBox counterBox;


    private PlayFieldDescriptor(Point gridOrigin, Point nextGemsPanelOrigin,
        Point scoreOrigin, Point gameOverOrigin, CrushBox crushBox,
        WarningBox warningBox, CounterBox counterBox)
    {
        this.gridOrigin = gridOrigin;
        this.nextGemsPanelOrigin = nextGemsPanelOrigin;
        this.scoreOrigin = scoreOrigin;
        this.crushBox = crushBox;
        this.warningBox = warningBox;
        this.counterBox = counterBox;
        this.gameOverOrigin = gameOverOrigin;
    }


    public static PlayFieldDescriptor createForTesting(Environment environment)
    {
        return new PlayFieldDescriptor(new Point(0, 0), new Point(0, 0),
            new Point(0, 0), new Point(20, 224),
            CrushBox.createForPlayerOne(environment),
            WarningBox.createForPlayerOne(environment.getEngine()),
            CounterBox.createForPlayerOne(environment.getEngine()));
    }


    public static PlayFieldDescriptor createForPlayerOne(Environment environment)
    {
        return new PlayFieldDescriptor(new Point(20, 32), new Point(292, 32),
            new Point(291, 421), new Point(20, 224),
            CrushBox.createForPlayerOne(environment),
            WarningBox.createForPlayerOne(environment.getEngine()),
            CounterBox.createForPlayerOne(environment.getEngine()));
    }


    public static PlayFieldDescriptor createForPlayerTwo(Environment environment)
    {
        return new PlayFieldDescriptor(new Point(524, 32), new Point(476, 32),
            new Point(419, 421), new Point(524, 224),
            CrushBox.createForPlayerTwo(environment),
            WarningBox.createForPlayerTwo(environment.getEngine()),
            CounterBox.createForPlayerTwo(environment.getEngine()));
    }


    public Point gridOrigin()
    {
        return gridOrigin;
    }


    public Point nextGemsPanelOrigin()
    {
        return nextGemsPanelOrigin;
    }


    public Point scoreOrigin()
    {
        return scoreOrigin;
    }


    public CrushBox getCrushBox()
    {
        return crushBox;
    }


    public WarningBox getWarningBox()
    {
        return warningBox;
    }


    public CounterBox getCounterBox()
    {
        return counterBox;
    }


    public Point getGameOverOrigin()
    {
        return gameOverOrigin;
    }

}
