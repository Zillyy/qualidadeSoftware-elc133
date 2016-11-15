package it.diamonds.engine.modifiers;


import it.diamonds.engine.video.AbstractEngine;


public interface DrawModifier
{
    void updateModifierState();


    void draw(AbstractEngine engine);


    boolean ended();
}
