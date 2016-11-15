package it.diamonds.engine.input;


import it.diamonds.engine.Config;
import it.diamonds.engine.input.Event.Code;

import java.util.HashMap;


public class EventMappings
{

    private HashMap<Code, Code> eventMappings = new HashMap<Code, Code>();


    public static EventMappings create()
    {
        return new EventMappings();
    }


    public static EventMappings createForPlayerOne(Config config)
    {
        EventMappings mappings = new EventMappings();

        mappings.setMapping(Code.KEY_ESCAPE, Code.ESCAPE);
        mappings.setMapping(Code.valueOf(config.getKey("P1.UP")), Code.UP);
        mappings.setMapping(Code.valueOf(config.getKey("P1.DOWN")), Code.DOWN);
        mappings.setMapping(Code.valueOf(config.getKey("P1.LEFT")), Code.LEFT);
        mappings.setMapping(Code.valueOf(config.getKey("P1.RIGHT")), Code.RIGHT);
        mappings.setMapping(Code.valueOf(config.getKey("P1.BUTTON1")),
            Code.BUTTON1);
        mappings.setMapping(Code.valueOf(config.getKey("P1.BUTTON2")),
            Code.BUTTON2);
        mappings.setMapping(Code.valueOf(config.getKey("P1.BUTTON3")),
            Code.BUTTON3);

        return mappings;
    }


    public static EventMappings createForPlayerTwo(Config config)
    {
        EventMappings mappings = new EventMappings();

        mappings.setMapping(Code.KEY_ESCAPE, Code.ESCAPE);
        mappings.setMapping(Code.valueOf(config.getKey("P2.UP")), Code.UP);
        mappings.setMapping(Code.valueOf(config.getKey("P2.DOWN")), Code.DOWN);
        mappings.setMapping(Code.valueOf(config.getKey("P2.LEFT")), Code.LEFT);
        mappings.setMapping(Code.valueOf(config.getKey("P2.RIGHT")), Code.RIGHT);
        mappings.setMapping(Code.valueOf(config.getKey("P2.BUTTON1")),
            Code.BUTTON1);
        mappings.setMapping(Code.valueOf(config.getKey("P2.BUTTON2")),
            Code.BUTTON2);
        mappings.setMapping(Code.valueOf(config.getKey("P2.BUTTON3")),
            Code.BUTTON3);

        return mappings;
    }


    public static EventMappings createForGameLoop(Config config)
    {
        EventMappings mappings = new EventMappings();

        mappings.setMapping(Code.KEY_ENTER, Code.ENTER);
        mappings.setMapping(Code.KEY_ESCAPE, Code.ESCAPE);
        mappings.setMapping(Code.KEY_UP, Code.UP);
        mappings.setMapping(Code.KEY_DOWN, Code.DOWN);

        return mappings;
    }


    public static EventMappings createForTesting()
    {
        EventMappings mappings = new EventMappings();

        mappings.setMapping(Code.ESCAPE, Code.ESCAPE);
        mappings.setMapping(Code.ENTER, Code.ENTER);
        mappings.setMapping(Code.UP, Code.UP);
        mappings.setMapping(Code.DOWN, Code.DOWN);
        mappings.setMapping(Code.LEFT, Code.LEFT);
        mappings.setMapping(Code.RIGHT, Code.RIGHT);
        mappings.setMapping(Code.BUTTON1, Code.BUTTON1);
        mappings.setMapping(Code.BUTTON2, Code.BUTTON2);
        mappings.setMapping(Code.BUTTON3, Code.BUTTON3);

        return mappings;
    }


    public void setMapping(Code sourceCode, Code targetCode)
    {
        eventMappings.put(sourceCode, targetCode);
    }


    public boolean containsMapping(Code code)
    {
        return eventMappings.containsKey(code);
    }


    public Code translateEvent(Code sourceCode)
    {
        if(!eventMappings.containsKey(sourceCode))
        {
            return Code.UNKNOWN;
        }

        return eventMappings.get(sourceCode);
    }


    public static EventMappings createAdvanceForPlayerOne(Config config)
    {
        EventMappings mappings = createForPlayerOne(config);
        return mappings;
    }


    public static EventMappings createAdvanceForPlayerTwo(Config config)
    {
        EventMappings mappings = createForPlayerTwo(config);
        return mappings;
    }
}
