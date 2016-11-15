package it.diamonds.engine.input;


public final class Event
{
    public enum Code
    {
        UNKNOWN, ESCAPE, ENTER, UP, DOWN, LEFT, RIGHT, BUTTON1, BUTTON2, BUTTON3, BUTTON4, QUIT, KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J, KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T, KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z, KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT, KEY_ESCAPE, KEY_ENTER, KEY_COMMA, KEY_PERIOD, KEY_SLASH
    };

    public enum State
    {
        RELEASED, PRESSED
    };

    private Code code;

    private State state;

    private long timestamp;


    private Event(Code code, State state, long timestamp)
    {
        this.code = code;
        this.state = state;
        this.timestamp = timestamp;
    }


    public static Event create(Code code, State state, long timestamp)
    {
        return new Event(code, state, timestamp);
    }


    public static Event create(Code code, State state)
    {
        return new Event(code, state, 0);
    }


    public Event copyAndChange(Code newCode)
    {
        return Event.create(newCode, this.state, this.timestamp);
    }


    public Event copyAndChange(long timestamp)
    {
        return Event.create(this.code, this.state, timestamp);
    }


    public Event copyAndChange(Code newCode, long timestamp)
    {
        return Event.create(newCode, this.state, timestamp);
    }


    public Code getCode()
    {
        return code;
    }


    public State getState()
    {
        return state;
    }


    public long getTimestamp()
    {
        return timestamp;
    }


    public boolean is(Code code)
    {
        return this.code == code;
    }


    public boolean isPressed()
    {
        return state == State.PRESSED;
    }


    public boolean isReleased()
    {
        return state == State.RELEASED;
    }

}
