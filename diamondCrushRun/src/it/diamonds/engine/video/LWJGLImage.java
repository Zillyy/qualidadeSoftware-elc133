package it.diamonds.engine.video;


import static org.lwjgl.devil.IL.IL_BYTE;
import static org.lwjgl.devil.IL.IL_IMAGE_FORMAT;
import static org.lwjgl.devil.IL.IL_IMAGE_HEIGHT;
import static org.lwjgl.devil.IL.IL_IMAGE_WIDTH;
import static org.lwjgl.devil.IL.IL_RGB;
import static org.lwjgl.devil.IL.IL_RGBA;
import static org.lwjgl.devil.IL.ilBindImage;
import static org.lwjgl.devil.IL.ilConvertImage;
import static org.lwjgl.devil.IL.ilGenImages;
import static org.lwjgl.devil.IL.ilGetData;
import static org.lwjgl.devil.IL.ilGetInteger;
import static org.lwjgl.devil.IL.ilLoadImage;
import static org.lwjgl.devil.ILU.iluFlipImage;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.devil.IL;
import org.lwjgl.devil.ILU;


public final class LWJGLImage implements Image
{
    private static String imageDir = "data/";

    private int texID;

    private int format = GL_RGBA;

    private int width;

    private int height;

    private ByteBuffer data;

    private String name;

    private boolean loaded;


    private LWJGLImage(String name, String otherType)
    {
        this.name = name;
        loadImageFromFile(this.name + otherType);
        setup();
    }


    public static LWJGLImage create(String name, String otherType)
    {
        return new LWJGLImage(name, otherType);
    }


    private void loadImageFromFile(String fileName) throws RuntimeException
    {
        try
        {
            IL.create();
            ILU.create();

            generateImage();

            loaded = ilLoadImage(imageDir + fileName);

            if(!isLoaded())
            {
                throw new RuntimeException("Unable to find the image "
                    + imageDir + fileName);
            }

            convertImage();

            iluFlipImage();

            height = ilGetInteger(IL_IMAGE_HEIGHT);
            width = ilGetInteger(IL_IMAGE_WIDTH);

            if(!isPowerOfTwo((int)height) || !isPowerOfTwo((int)width))
            {
                throw new RuntimeException(
                    "The image width or height is not a power of two");
            }

            data = ilGetData();

        }
        catch(LWJGLException e)
        {
            throw new RuntimeException("Image loading error due to " + e);
        }
    }


    private void generateImage()
    {
        IntBuffer image = ByteBuffer.allocateDirect(4).order(
            ByteOrder.nativeOrder()).asIntBuffer();
        ilGenImages(image);
        ilBindImage(image.get(0));
    }


    private void convertImage()
    {
        if(ilGetInteger(IL_IMAGE_FORMAT) == IL_RGB)
        {
            ilConvertImage(IL_RGB, IL_BYTE);
            format = GL_RGB;
        }
        else
        {
            if(ilGetInteger(IL_IMAGE_FORMAT) == IL_RGBA)
            {
                ilConvertImage(IL_RGBA, IL_BYTE);
                format = GL_RGBA;
            }
        }
    }


    private boolean isPowerOfTwo(int n)
    {
        int test = n;
        for(; (0 == (test % 2)) && (test != 1); test /= 2)
        {
            ;
        }
        return test == 1;
    }


    private void setup()
    {
        initOpenGLStates();
        setupOpenGLStates();
    }


    private void initOpenGLStates()
    {
        try
        {
            IntBuffer texture = BufferUtils.createIntBuffer(16);
            glGenTextures(texture);
            texID = texture.get(0);
        }
        catch(NullPointerException e)
        {
        }
    }


    private void setupOpenGLStates()
    {
        try
        {
            // Assign OpenGL Texture
            glBindTexture(GL_TEXTURE_2D, texID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format,
                GL_UNSIGNED_BYTE, data);
        }
        catch(NullPointerException e)
        {
        }
    }


    public boolean isLoaded()
    {
        return loaded;
    }


    public void cleanup()
    {
        if(texID != 0)
        {
            final IntBuffer textures = BufferUtils.createIntBuffer(16);
            textures.put(texID);
            glDeleteTextures(textures);
            texID = 0;
        }

        loaded = false;
    }


    public String getName()
    {
        return name;
    }


    public int getWidth()
    {
        return width;
    }


    public int getHeight()
    {
        return height;
    }


    public void enable()
    {
        if(format == GL_RGBA)
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
        else
        {
            glDisable(GL_BLEND);
        }

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texID);
        glMatrixMode(GL_TEXTURE);
        glLoadIdentity();
        glScalef(1f, -1f, 1f);
        glMatrixMode(GL_MODELVIEW);
    }

}
