package it.diamonds.tests.engine;


import it.diamonds.engine.video.Layer;


public class TestLayer extends AbstractLayerTestCase
{
    private Layer layer;


    public void setUp()
    {
        super.setUp();
        layer = new Layer();
    }


    public void testOneItemDrawn()
    {
        layer.add(sprite);
        layer.drawLayer(engine);
        assertEquals(1, engine.getNumberOfQuadsDrawn());
    }


    public void testTwoItemsDrawn()
    {
        layer.add(sprite);
        layer.add(sprite);
        layer.drawLayer(engine);
        assertEquals(2, engine.getNumberOfQuadsDrawn());
    }

}
