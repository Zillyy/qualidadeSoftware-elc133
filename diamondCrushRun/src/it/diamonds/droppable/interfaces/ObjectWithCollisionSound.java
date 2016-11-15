package it.diamonds.droppable.interfaces;


import it.diamonds.engine.audio.Sound;


public interface ObjectWithCollisionSound
{
    void setCollisionSound(Sound sound);


    Sound getCollisionSound();
}
