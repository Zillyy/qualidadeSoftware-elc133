package it.diamonds.grid.action;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.DroppableList;
import it.diamonds.grid.Grid;


public class UpdateStoneAction extends AbstractAction
{
    public UpdateStoneAction()
    {
        ;
    }


    // CheckStyle_Can_You_Stop_Being_So_Pedantic_For_A_Second

    public void apply(Grid grid, DroppableList gridElements)
    {
        forEachDroppable(grid);
    }


    // CheckStyle_Ok_Now_You_Can_Go_Back_To_Work

    protected void applyOn(Droppable gem)
    {
        if(!gem.getGridObject().getType().isStone())
        {
            return;
        }

        int currentFrame = gem.getAnimatedObject().getCurrentFrame();

        if(currentFrame < 5)
        {
            gem.getAnimatedObject().setCurrentFrame(currentFrame + 1);
        }
    }
}
