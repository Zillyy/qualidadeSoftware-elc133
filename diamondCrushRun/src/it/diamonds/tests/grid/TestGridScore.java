package it.diamonds.tests.grid;


import static it.diamonds.droppable.DroppableColor.DIAMOND;

import java.io.IOException;


public class TestGridScore extends AbstractGridTestCase
{

    public void setUp() throws IOException
    {
        super.setUp();

        grid.removeDroppableFromGrid(controller.getGemsPair().getPivot());
        grid.removeDroppableFromGrid(controller.getGemsPair().getSlave());
    }


    public void testNoScoreOnInsertion()
    {
        insertAndUpdate(createGem(DIAMOND), 0, 0);

        assertEquals("Total Score must be 0", 0, grid.computeTotalScore());
    }


    public void testScoreOnDeletion()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createChest(DIAMOND), 13, 2);

        grid.updateCrushes();
        grid.closeChain();

        assertEquals("Total score was not correct",
            createGem(DIAMOND).getScore(), grid.computeTotalScore());
    }


    public void testBigGemsAddedWithBonus()
    {
        insertAndUpdate(createGem(DIAMOND), 13, 3);
        insertAndUpdate(createGem(DIAMOND), 13, 2);
        insertAndUpdate(createGem(DIAMOND), 12, 3);
        insertAndUpdate(createGem(DIAMOND), 12, 2);
        insertAndUpdate(createChest(DIAMOND), 11, 2);

        grid.updateBigGems();

        grid.updateCrushes();
        grid.closeChain();

        int realScore = createGem(DIAMOND).getScore() * 4
            * environment.getConfig().getInteger("BonusPercentage") / 100;

        assertEquals("Total score was not correct", realScore,
            grid.computeTotalScore());
    }

    // public void testBigGemsAddedWithBonus2()
    // {
    //        
    // insertAndUpdate(createGem(DIAMOND), 13, 1);
    // insertAndUpdate(createGem(DIAMOND), 13, 2);
    // insertAndUpdate(createGem(DIAMOND), 12, 1);
    // insertAndUpdate(createGem(DIAMOND), 12, 2);
    //
    // insertAndUpdate(createChest(DIAMOND), 9, 2);
    //        
    // insertAndDropGemsPair();
    // makeAllGemsFall();
    // controller.update(environment.getTimer().getTime());
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //        
    // int realScore = createGem(DIAMOND).getScore() * 4
    // * environment.getConfig().getInteger("BonusPercentage") / 100;
    // int gridScore = grid.computeTotalScore();
    //        
    // assertEquals("Total score was not correct: is " + gridScore
    // + " instead of " + realScore, realScore, gridScore);
    // }
    //
    //
    // public void testScoreOnMultipleCrushes()
    // {
    // insertAndUpdate(createGem(EMERALD), 13, 2);
    // insertAndUpdate(createGem(DIAMOND), 12, 2);
    // insertAndUpdate(createChest(DIAMOND), 11, 2);
    // insertAndUpdate(createChest(EMERALD), 10, 2);
    //
    // insertAndUpdate(createGem(DIAMOND), 9, 2);
    //
    // insertAndDropGemsPair();
    // makeAllGemsFall();
    //
    // controller.update(environment.getTimer().getTime());
    // assertEquals(5, grid.getNumberOfDroppables());
    // assertEquals(1, grid.getCrushedGemsCounter());
    // assertEquals(1, grid.getChainCounter());
    //
    // makeAllGemsFall();
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // assertEquals(3, grid.getNumberOfDroppables());
    // assertEquals(2, grid.getCrushedGemsCounter());
    // assertEquals(2, grid.getChainCounter());
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // grid.closeChain();
    //
    // int realScore = (createGem(DIAMOND).getScore() + createGem(EMERALD).getScore()) *
    // 2;
    // assertEquals(realScore, grid.computeTotalScore());
    // }
    //
    //
    // public void testScoreOnMultipleCrushesOfBigGem()
    // {
    // insert2x2BlockOfGems(DIAMOND, 12, 1);
    // insert2x2BlockOfGems(EMERALD, 10, 1);
    //
    // insertAndUpdate(createChest(EMERALD), 9, 1);
    // insertAndUpdate(createChest(DIAMOND), 9, 2);
    //
    // insertAndUpdate(createGem(EMERALD), 8, 2);
    //
    // insertAndDropGemsPair();
    // makeAllGemsFall();
    //
    // controller.update(environment.getTimer().getTime());
    //
    // assertEquals(8, grid.getNumberOfDroppables());
    // assertEquals(8, grid.getCrushedGemsCounter());
    // assertEquals(1, grid.getChainCounter());
    //
    // makeAllGemsFall();
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // assertEquals(3, grid.getNumberOfDroppables());
    // assertEquals(16, grid.getCrushedGemsCounter());
    // assertEquals(2, grid.getChainCounter());
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // grid.closeChain();
    //
    // int realScore = (createGem(DIAMOND).getScore() + createGem(EMERALD).getScore()) * 4
    // * 4;
    // assertEquals(realScore, grid.computeTotalScore());
    // }
    //
    //
    // public void testScoreOnFlashAndChestCrush()
    // {
    // insertAndUpdate(createGem(RUBY), 13, 1);
    // insertAndUpdate(createGem(EMERALD), 12, 1);
    // insertAndUpdate(createFlashingGem(), 13, 0);
    // insertAndUpdate(createChest(EMERALD), 13, 2);
    //
    // insertAndUpdate(createGem(DIAMOND), 12, 2);
    //
    // insertAndDropGemsPair();
    // makeAllGemsFall();
    //
    // controller.update(environment.getTimer().getTime());
    //
    // assertEquals(5, grid.getNumberOfDroppables());// emerald, emerald chest, diamond
    // // e 2 gemme
    // // della gemspair(OK)
    // assertEquals(0, grid.getCrushedGemsCounter());
    // assertEquals(0, grid.getChainCounter());// (OK)
    //
    // makeAllGemsFall();
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // assertEquals(3, grid.getNumberOfDroppables());// 2 gem della gemspair +
    // // diamond(OK)
    // assertEquals(1, grid.getCrushedGemsCounter());// la emerals
    // assertEquals(1, grid.getChainCounter());
    //
    // environment.getTimer().advance(
    // environment.getConfig().getInteger("DelayBetweenCrushes"));
    // controller.update(environment.getTimer().getTime());
    //
    // grid.closeChain();
    //
    // int realScore = createGem(EMERALD).getScore();// 40
    // assertEquals(realScore, grid.computeTotalScore());// restituisce 90, ossia 40 +
    // // 50(ruby)
    // }
}
