package it.diamonds.tests.engine.audio;


import it.diamonds.engine.audio.AudioInterface;
import it.diamonds.engine.audio.Sound;
import it.diamonds.tests.mocks.MockAudio;
import junit.framework.TestCase;


public class TestAudio extends TestCase
{
    private AudioInterface audio;


    public void setUp()
    {
        audio = MockAudio.create();
    }


    public void testCreateSound()
    {
        Sound sound = audio.createSound("test_sound");

        assertEquals("test_sound", sound.getName());
    }


    public void testCreateSameSoundTwice()
    {
        Sound sound = audio.createSound("test_sound");
        Sound sameSound = audio.createSound("test_sound");

        assertSame(sameSound, sound);
    }


    public void testCreateTwoDifferentSounds()
    {
        Sound sound = audio.createSound("test_sound");
        Sound differentSound = audio.createSound("test_different_sound");

        assertNotSame(differentSound, sound);
    }


    public void testMusicPlay()
    {
        assertFalse(audio.isMusicPlaying());
        audio.playMusic();
        assertTrue(audio.isMusicPlaying());
    }


    public void testMusicOpened()
    {
        assertFalse(audio.isMusicLoaded());
        audio.openMusic("theme_rock");
        assertTrue(audio.isMusicLoaded());
    }


    public void testMusicStop()
    {
        audio.playMusic();
        audio.stopMusic();
        assertFalse(audio.isMusicPlaying());
    }

}
