package it.diamonds.engine.input;


public interface InputDeviceInterface
{

    void addListener(InputListenerInterface listener);


    void update();


    void notify(Event event);

}
