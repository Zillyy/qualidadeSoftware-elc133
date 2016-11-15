package it.diamonds.engine.video;


public interface Image
{

    boolean isLoaded();


    void cleanup();


    String getName();


    int getWidth();


    int getHeight();


    void enable();

}
