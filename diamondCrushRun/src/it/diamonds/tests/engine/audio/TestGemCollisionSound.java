package it.diamonds.tests.engine.audio;


import it.diamonds.droppable.Droppable;
import it.diamonds.droppable.gems.Gem;
import it.diamonds.engine.audio.Sound;
import it.diamonds.tests.mocks.MockAudio;
import it.diamonds.tests.mocks.MockEngine;
import junit.framework.TestCase;


public class TestGemCollisionSound extends TestCase
{
    private Sound sound;

    private Droppable gem;


    public void setUp()
    {
        gem = Gem.createForTesting(new MockEngine(800, 600));
        sound = MockAudio.create().createSound("diamond");
    }


    public void testCollisionSoundNotSet()
    {
        assertFalse("Collision sound not set",
            gem.getObjectWithCollisionSound().getCollisionSound() != null);
    }


    public void testSetCollisionSound()
    {
        gem.getObjectWithCollisionSound().setCollisionSound(sound);

        assertTrue("Collision sound not set",
            gem.getObjectWithCollisionSound().getCollisionSound() != null);
    }


    public void testNullCollisionSound()
    {
        try
        {
            gem.getObjectWithCollisionSound().setCollisionSound(null);
        }
        catch(Exception e)
        {
            return;
        }

        fail("Exception not thrown");
    }


    public void testGetCollisionSound()
    {
        gem.getObjectWithCollisionSound().setCollisionSound(sound);
        assertEquals("collision sound is wrong",
            gem.getObjectWithCollisionSound().getCollisionSound(), sound);
    }


    public void testSoundBeforeCollision()
    {
        gem.getObjectWithCollisionSound().setCollisionSound(sound);

        assertFalse("Sound must not be played before a collision",
            sound.wasPlayed());
    }

}
