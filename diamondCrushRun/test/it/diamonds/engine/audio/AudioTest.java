package it.diamonds.engine.audio;

import it.diamonds.tests.mocks.MockAudio;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Zilly
 */
public class AudioTest {
    
    private AudioInterface audio;
    
    @Before
    public void setUp() {
        audio = MockAudio.create();
    }
    
    /**
     * Testa se os dois sons criados são iguais.
     */
    @Test
    public void testCreateSameSoundTwice() {
        System.out.println("* AudioTest: - testCreateSameSoundTwice()");
        
        Sound sound = audio.createSound("test_sound");
        Sound sameSound = audio.createSound("test_sound");

        assertSame(sameSound, sound);
    }
    
    /**
     * Testa se os sons criados são diferentes.
     */
    @Test
    public void testCreateTwoDifferentSounds() {
        System.out.println("* AudioTest: - testCreateTwoDifferentSounds()");
        
        Sound sound = audio.createSound("test_sound");
        Sound differentSound = audio.createSound("test_different_sound");

        assertNotSame(differentSound, sound);
    }
    
}
