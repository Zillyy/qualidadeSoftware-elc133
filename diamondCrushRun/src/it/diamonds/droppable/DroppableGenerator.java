package it.diamonds.droppable;


public interface DroppableGenerator
{
    Droppable extract();


    Droppable getGemAt(int index);

}
