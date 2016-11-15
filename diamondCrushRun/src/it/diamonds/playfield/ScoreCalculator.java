package it.diamonds.playfield;


import it.diamonds.droppable.Droppable;


public class ScoreCalculator
{
    private int partialScore;

    private int score;

    private int bonusPercentage;


    public ScoreCalculator(int bonusPercentage)
    {
        partialScore = 0;
        score = 0;
        this.bonusPercentage = bonusPercentage;
    }


    public int getScore()
    {
        return score;
    }


    public void addScoreForGem(Droppable gem)
    {
        int area = gem.getCell().getHeight() * gem.getCell().getWidth();

        if(area == 1)
        {
            partialScore += gem.getScore();
        }
        else
        {
            // FIXME: questa riga non e' testata
            partialScore += (area * gem.getScore() * bonusPercentage) / 100;
        }
    }


    public void closeChain(int multiplier)
    {
        score += partialScore * multiplier;
        resetPartialScore();
    }


    private void resetPartialScore()
    {
        partialScore = 0;
    }
}
