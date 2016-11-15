package it.diamonds.engine.audio;


public interface AudioInterface
{

    boolean isCreated();


    boolean isInitialised();


    Sound createSound(String name);


    void openMusic(String name);


    void playMusic();


    void stopMusic();


    boolean isMusicPlaying();


    boolean isMusicLoaded();
}
