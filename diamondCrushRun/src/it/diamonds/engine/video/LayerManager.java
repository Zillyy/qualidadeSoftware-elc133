package it.diamonds.engine.video;


import static it.diamonds.engine.video.LayerType.Opaque;


public class LayerManager
{
    private LayerList opaqueLayers;

    private LayerList transparentLayers;

    private Layer currentLayer;

    private LayerType currentLayerType;


    public LayerManager()
    {
        opaqueLayers = new LayerList();
        transparentLayers = new LayerList();
    }


    public int getLayersCount()
    {
        return opaqueLayers.size() + transparentLayers.size();
    }


    public void openNewLayer(LayerType type)
    {
        currentLayer = new Layer();
        currentLayerType = type;
    }


    public void closeCurrentLayer()
    {
        if(currentLayerType == Opaque)
        {
            opaqueLayers.add(currentLayer);
        }
        else
        {
            transparentLayers.add(currentLayer);
        }
    }


    public void addToLayer(DrawableInterface item)
    {
        currentLayer.add(item);
    }


    public void drawLayers(AbstractEngine engine)
    {
        for(Layer layer : opaqueLayers)
        {
            layer.drawLayer(engine);
        }

        for(Layer layer : transparentLayers)
        {
            layer.drawLayer(engine);
        }
    }
}
