package it.diamonds.tests.droppable;


import junit.framework.TestCase;


public class TestAbstractDroppable extends TestCase
{
    private AbstractDroppableForTesting abstractDroppable;


    public void setUp()
    {
        abstractDroppable = new AbstractDroppableForTesting();
    }


    public void testAbstractDroppable()
    {
        assertEquals(abstractDroppable, abstractDroppable.getAnimatedObject());
    }


    public void testMoveableObjectInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable, abstractDroppable.getMoveableObject());
    }


    public void testGridObjectInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable, abstractDroppable.getGridObject());
    }


    public void testObjectWithCollisionSoundInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable,
            abstractDroppable.getObjectWithCollisionSound());
    }


    public void testAnimatedObjectInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable, abstractDroppable.getAnimatedObject());
    }


    public void testFallingObjectInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable, abstractDroppable.getFallingObject());
    }


    public void testMovingDownObjectInterfaceReturnsWell()
    {
        assertEquals(abstractDroppable, abstractDroppable.getMovingDownObject());
    }


    public void testExtensibleObjectInterfaceReturnsWell()
    {
        assertNull(abstractDroppable.getExtensibleObject());
    }


    public void testMergingObjectInterfaceReturnsWell()
    {
        assertNull(abstractDroppable.getMergingObject());
    }
}
