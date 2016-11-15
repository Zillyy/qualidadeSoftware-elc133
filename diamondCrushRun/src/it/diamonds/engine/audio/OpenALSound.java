package it.diamonds.engine.audio;


import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;


public final class OpenALSound implements Sound
{
    private static final String BASEALERRORMESSAGE = "OpenAl error: ";

    private static Map<Integer, String> errors = new HashMap<Integer, String>();

    static
    {
        errors.put(AL10.AL_INVALID_NAME, BASEALERRORMESSAGE + "AL_INVALID_NAME");
        errors.put(AL10.AL_INVALID_ENUM, BASEALERRORMESSAGE + "AL_INVALID_ENUM");
        errors.put(AL10.AL_INVALID_VALUE, BASEALERRORMESSAGE
            + "AL_INVALID_VALUE");
        errors.put(AL10.AL_INVALID_OPERATION, BASEALERRORMESSAGE
            + "AL_INVALID_OPERATION");
        errors.put(AL10.AL_OUT_OF_MEMORY, BASEALERRORMESSAGE
            + "AL_OUT_OF_MEMORY");
    }

    private String soundDir = "data/";

    private String soundExtension = ".wav";

    private IntBuffer source = BufferUtils.createIntBuffer(1);

    private IntBuffer buffer = BufferUtils.createIntBuffer(1);

    private boolean wasPlayed;

    private String name;


    private OpenALSound(String fileName)
    {
        this.name = fileName;
        initSource(fileName);
    }


    public static Sound create(String fileName)
    {
        return new OpenALSound(fileName);
    }


    public Object getName()
    {
        return name;
    }


    public void play()
    {
        AL10.alSourcePlay(source);

        wasPlayed = true;
    }


    public boolean wasPlayed()
    {
        return wasPlayed;
    }


    public void reset()
    {
        wasPlayed = false;
    }


    public void freeMemory()
    {
        source.position(0).limit(1);
        AL10.alDeleteSources(source);

        buffer.position(0).limit(1);
        AL10.alDeleteBuffers(buffer);

        wasPlayed = false;
    }


    private void initSource(String fileName)
    {
        if(AL.isCreated())
        {
            buffer.position(0).limit(1);
            AL10.alGenBuffers(buffer);
            checkErrors();
            readSoundFile(fileName);

            source.position(0).limit(1);
            AL10.alGenSources(source);
            checkErrors();

            initSource();
            checkErrors();
        }
    }


    private void readSoundFile(String fileName)
    {
        FileInputStream inputFile = null;

        try
        {
            inputFile = new FileInputStream(soundDir + fileName
                + soundExtension);
        }
        catch(IOException e)
        {
            throw new RuntimeException("i can not find the sound file `"
                + soundDir + fileName + soundExtension + "'");
        }

        WaveData waveFile = WaveData.create(new BufferedInputStream(inputFile));

        if(waveFile == null)
        {
            throw new RuntimeException("waveFile is null");
        }
        AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data,
            waveFile.samplerate);
        waveFile.dispose();
    }


    private void checkErrors()
    {
        int error = AL10.alGetError();
        if(error != AL_NO_ERROR)
        {
            throw new RuntimeException(getALErrorString(error));
        }
    }


    private static String getALErrorString(int err)
    {
        String result = errors.get(err);
        if(result == null)
        {
            throw new IllegalArgumentException("Bad error code!!");
        }
        return result;
    }


    private void initSource()
    {
        AL10.alSourcei(source.get(0), AL_BUFFER, buffer.get(0));
        AL10.alSourcef(source.get(0), AL_PITCH, 1.0f);
        AL10.alSourcef(source.get(0), AL_GAIN, 1.0f);
        AL10.alSource3f(source.get(0), AL_POSITION, 0.0f, 0.0f, 0.0f);
        AL10.alSource3f(source.get(0), AL_VELOCITY, 0.0f, 0.0f, 0.0f);
    }

}
