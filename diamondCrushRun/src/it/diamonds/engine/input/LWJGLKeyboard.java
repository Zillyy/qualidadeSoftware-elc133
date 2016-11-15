package it.diamonds.engine.input;


import static org.lwjgl.input.Keyboard.KEY_A;
import static org.lwjgl.input.Keyboard.KEY_B;
import static org.lwjgl.input.Keyboard.KEY_C;
import static org.lwjgl.input.Keyboard.KEY_COMMA;
import static org.lwjgl.input.Keyboard.KEY_D;
import static org.lwjgl.input.Keyboard.KEY_DOWN;
import static org.lwjgl.input.Keyboard.KEY_E;
import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.input.Keyboard.KEY_F;
import static org.lwjgl.input.Keyboard.KEY_G;
import static org.lwjgl.input.Keyboard.KEY_H;
import static org.lwjgl.input.Keyboard.KEY_I;
import static org.lwjgl.input.Keyboard.KEY_J;
import static org.lwjgl.input.Keyboard.KEY_K;
import static org.lwjgl.input.Keyboard.KEY_L;
import static org.lwjgl.input.Keyboard.KEY_LEFT;
import static org.lwjgl.input.Keyboard.KEY_M;
import static org.lwjgl.input.Keyboard.KEY_N;
import static org.lwjgl.input.Keyboard.KEY_O;
import static org.lwjgl.input.Keyboard.KEY_P;
import static org.lwjgl.input.Keyboard.KEY_PERIOD;
import static org.lwjgl.input.Keyboard.KEY_Q;
import static org.lwjgl.input.Keyboard.KEY_R;
import static org.lwjgl.input.Keyboard.KEY_RETURN;
import static org.lwjgl.input.Keyboard.KEY_RIGHT;
import static org.lwjgl.input.Keyboard.KEY_S;
import static org.lwjgl.input.Keyboard.KEY_SLASH;
import static org.lwjgl.input.Keyboard.KEY_T;
import static org.lwjgl.input.Keyboard.KEY_U;
import static org.lwjgl.input.Keyboard.KEY_UP;
import static org.lwjgl.input.Keyboard.KEY_V;
import static org.lwjgl.input.Keyboard.KEY_W;
import static org.lwjgl.input.Keyboard.KEY_X;
import static org.lwjgl.input.Keyboard.KEY_Y;
import static org.lwjgl.input.Keyboard.KEY_Z;
import it.diamonds.engine.ComponentInterface;
import it.diamonds.engine.input.Event.Code;
import it.diamonds.engine.input.Event.State;

import java.util.HashMap;
import java.util.LinkedList;


public final class LWJGLKeyboard implements InputDeviceInterface,
    ComponentInterface
{
    private LinkedList<InputListenerInterface> listeners;

    private HashMap<Integer, Code> keyMappings;

    private HashMap<Boolean, State> stateMappings;


    private LWJGLKeyboard()
    {
        try
        {
            org.lwjgl.input.Keyboard.create();
            listeners = new LinkedList<InputListenerInterface>();
            keyMappings = new HashMap<Integer, Code>();
            stateMappings = new HashMap<Boolean, State>();
            fillAlphaKeys();
            fillSpecialKeys();
            fillStates();
        }
        catch(Exception e)
        {
            throw new RuntimeException(
                "Something happened while I was initializing the keyboard: "
                    + e);
        }

    }


    private void fillAlphaKeys()
    {
        keyMappings.put(KEY_A, Code.KEY_A);
        keyMappings.put(KEY_B, Code.KEY_B);
        keyMappings.put(KEY_C, Code.KEY_C);
        keyMappings.put(KEY_D, Code.KEY_D);
        keyMappings.put(KEY_E, Code.KEY_E);
        keyMappings.put(KEY_F, Code.KEY_F);
        keyMappings.put(KEY_G, Code.KEY_G);
        keyMappings.put(KEY_H, Code.KEY_H);
        keyMappings.put(KEY_I, Code.KEY_I);
        keyMappings.put(KEY_J, Code.KEY_J);
        keyMappings.put(KEY_K, Code.KEY_K);
        keyMappings.put(KEY_L, Code.KEY_L);
        keyMappings.put(KEY_M, Code.KEY_M);
        keyMappings.put(KEY_N, Code.KEY_N);
        keyMappings.put(KEY_O, Code.KEY_O);
        keyMappings.put(KEY_P, Code.KEY_P);
        keyMappings.put(KEY_Q, Code.KEY_Q);
        keyMappings.put(KEY_R, Code.KEY_R);
        keyMappings.put(KEY_S, Code.KEY_S);
        keyMappings.put(KEY_T, Code.KEY_T);
        keyMappings.put(KEY_U, Code.KEY_U);
        keyMappings.put(KEY_V, Code.KEY_V);
        keyMappings.put(KEY_W, Code.KEY_W);
        keyMappings.put(KEY_X, Code.KEY_X);
        keyMappings.put(KEY_Y, Code.KEY_Y);
        keyMappings.put(KEY_Z, Code.KEY_Z);
    }


    private void fillSpecialKeys()
    {
        keyMappings.put(KEY_UP, Code.KEY_UP);
        keyMappings.put(KEY_DOWN, Code.KEY_DOWN);
        keyMappings.put(KEY_LEFT, Code.KEY_LEFT);
        keyMappings.put(KEY_RIGHT, Code.KEY_RIGHT);
        keyMappings.put(KEY_ESCAPE, Code.KEY_ESCAPE);
        keyMappings.put(KEY_RETURN, Code.KEY_ENTER);
        keyMappings.put(KEY_COMMA, Code.KEY_COMMA);
        keyMappings.put(KEY_PERIOD, Code.KEY_PERIOD);
        keyMappings.put(KEY_SLASH, Code.KEY_SLASH);
    }


    private void fillStates()
    {
        stateMappings.put(false, State.RELEASED);
        stateMappings.put(true, State.PRESSED);
    }


    public static LWJGLKeyboard create()
    {
        return new LWJGLKeyboard();
    }


    public void shutDown()
    {
        org.lwjgl.input.Keyboard.destroy();
    }


    public void update()
    {
        org.lwjgl.input.Keyboard.poll();

        while(org.lwjgl.input.Keyboard.next())
        {
            int code = org.lwjgl.input.Keyboard.getEventKey();
            if(keyMappings.containsKey(code))
            {
                notify(Event.create(
                    keyMappings.get(code),
                    stateMappings.get(org.lwjgl.input.Keyboard.getEventKeyState())));
            }
        }
    }


    public void addListener(InputListenerInterface listener)
    {
        listeners.add(listener);
    }


    public void notify(Event event)
    {
        for(InputListenerInterface listener : listeners)
        {
            listener.notify(event);
        }
    }


    public HashMap<Integer, Code> getKeyMappings()
    {
        return keyMappings;
    }

}
