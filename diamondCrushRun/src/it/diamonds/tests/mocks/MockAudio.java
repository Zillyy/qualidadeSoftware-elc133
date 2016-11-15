package it.diamonds.tests.mocks;


import it.diamonds.engine.audio.AudioInterface;
import it.diamonds.engine.audio.Sound;

import java.util.Hashtable;


public final class MockAudio implements AudioInterface
{
    private Hashtable<String, Sound> sounds = new Hashtable<String, Sound>();

    private boolean created;

    private boolean audioStatus;

    private boolean isMusicPlaying;

    private boolean isMusicLoaded;


    private MockAudio()
    {
        created = true;
        isMusicLoaded = false;
        isMusicPlaying = false;
    }


    public static AudioInterface create()
    {
        return new MockAudio();
    }


    public boolean isCreated()
    {
        return created;
    }


    public void shutDown()
    {
        audioStatus = false;
        created = false;
    }


    public boolean isInitialised()
    {
        return created || audioStatus;
    }


    public Sound createSound(String name)
    {
        Sound sound = sounds.get(name);

        if(sound == null)
        {
            sound = MockSound.create(name);
            sounds.put(name, sound);
        }

        return sound;
    }


    public void playMusic()
    {
        isMusicPlaying = true;
    }


    public boolean isMusicPlaying()
    {
        return isMusicPlaying;
    }


    public void stopMusic()
    {
        isMusicPlaying = false;
    }


    public void openMusic(String name)
    {
        isMusicLoaded = true;
    }


    public boolean isMusicLoaded()
    {
        return isMusicLoaded;
    }

}
