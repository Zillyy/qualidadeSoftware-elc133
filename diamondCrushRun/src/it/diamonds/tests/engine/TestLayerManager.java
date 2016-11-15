package it.diamonds.tests.engine;


import static it.diamonds.engine.video.LayerType.Opaque;
import static it.diamonds.engine.video.LayerType.Transparent;
import it.diamonds.engine.video.LayerManager;


public class TestLayerManager extends AbstractLayerTestCase
{
    private LayerManager layerManager;


    public void setUp()
    {
        super.setUp();
        layerManager = new LayerManager();
    }


    public void testNoLayers()
    {
        assertEquals(0, layerManager.getLayersCount());
    }


    public void testOneNewLayer()
    {
        layerManager.openNewLayer(Opaque);
        assertEquals(0, layerManager.getLayersCount());
    }


    public void testCloseLayer()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.closeCurrentLayer();
        assertEquals(1, layerManager.getLayersCount());
    }


    public void testTwoLayers()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.closeCurrentLayer();

        layerManager.openNewLayer(Opaque);
        layerManager.closeCurrentLayer();

        assertEquals(2, layerManager.getLayersCount());
    }


    public void testOneLayerDrawn()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.drawLayers(engine);
        assertEquals(1, engine.getNumberOfQuadsDrawn());
    }


    public void testLayerWithTwoItemsDrawn()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.drawLayers(engine);
        assertEquals(2, engine.getNumberOfQuadsDrawn());
    }


    public void testOpenLayerNotDrawn()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);

        layerManager.drawLayers(engine);
        assertEquals(0, engine.getNumberOfQuadsDrawn());
    }


    public void testTwoLayersDrawn()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.drawLayers(engine);
        assertEquals(2, engine.getNumberOfQuadsDrawn());
    }


    public void testLayersAreDrawnInOrder()
    {
        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(otherSprite);
        layerManager.closeCurrentLayer();

        layerManager.drawLayers(engine);
        spriteOrderTestHelper();
    }


    public void testOpaqueLayersAreDrawnBefore()
    {
        layerManager.openNewLayer(Transparent);
        layerManager.addToLayer(otherSprite);
        layerManager.closeCurrentLayer();

        layerManager.openNewLayer(Opaque);
        layerManager.addToLayer(sprite);
        layerManager.closeCurrentLayer();

        layerManager.drawLayers(engine);
        spriteOrderTestHelper();
    }
}
