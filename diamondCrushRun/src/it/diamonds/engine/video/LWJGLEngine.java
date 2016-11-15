package it.diamonds.engine.video;


import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_NORMALIZE;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import it.diamonds.engine.Point;
import it.diamonds.engine.Rectangle;


public final class LWJGLEngine extends AbstractEngine implements
    DisplayInterface
{
    private LWJGLEngine(int width, int height, boolean fullscreen)
    {
        initialize(width, height, fullscreen);
        initOpenGL();
    }


    public static AbstractEngine create(int width, int height, String title,
        boolean fullscreen)
    {
        LWJGLEngine engine = new LWJGLEngine(width, height, fullscreen);

        engine.setWindowTitle(title);

        return engine;
    }


    private void initialize(int width, int height, boolean fullscreen)
    {
        try
        {
            System.out.println("Display Adapter: " + Display.getAdapter());

            DisplayMode currentMode = findDisplayMode(width, height);

            if(currentMode != null)
            {
                Display.setFullscreen(fullscreen);
                Display.setDisplayMode(currentMode);
                if(currentMode.getBitsPerPixel() == 16)
                {
                    Display.create(new PixelFormat(16, 0, 0, 0, 0));
                }
                else
                {
                    Display.create(new PixelFormat(24, 0, 0, 0, 0));
                }
            }
        }
        catch(Exception e)
        {
            throw new DisplayException(
                "The current display mode is not available due to " + e);
        }
    }


    private void initOpenGL()
    {
        glEnable(GL_NORMALIZE);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, getDisplayWidth(), 0, getDisplayHeight(), -1f, 1f);
        glTranslatef(0f, getDisplayHeight(), 0f);
        glScalef(1f, -1f, 1f);
        glMatrixMode(GL_MODELVIEW);
    }


    private static DisplayMode findDisplayMode(int width, int height)
    {
        DisplayMode[] modes;

        DisplayMode bestMode = new DisplayMode(0, 0);

        try
        {
            modes = org.lwjgl.opengl.Display.getAvailableDisplayModes();

            System.out.println("List of available display modes:");

            for(int i = 0; i < modes.length; i++)
            {
                System.out.println(modes[i].toString());

                if(!displayModeMatches(modes[i], width, height))
                {
                    continue;
                }

                if(bestMode.getWidth() == 0)
                {
                    bestMode = modes[i];
                }
                else if(displayModeIsBetter(modes[i], bestMode))
                {
                    bestMode = modes[i];
                }
            }
        }
        catch(LWJGLException e)
        {
            throw new DisplayException(e.toString());
        }

        System.out.println("Best mode: " + bestMode.toString());

        return bestMode;
    }


    private static boolean displayModeMatches(DisplayMode mode, int width,
        int height)
    {
        return mode.getWidth() == width && mode.getHeight() == height;
    }


    private static boolean displayModeIsBetter(DisplayMode mode,
        DisplayMode bestMode)
    {
        return bestMode.getBitsPerPixel() < mode.getBitsPerPixel()
            || bestMode.getFrequency() < mode.getFrequency();
    }


    public int getDisplayWidth()
    {
        return Display.getDisplayMode().getWidth();
    }


    public int getDisplayHeight()
    {
        return Display.getDisplayMode().getHeight();
    }


    public void shutDown()
    {
        cleanupImages();
        Display.destroy();
    }


    public boolean isWindowClosed()
    {
        return Display.isCloseRequested();
    }


    public void setWindowTitle(String title)
    {
        Display.setTitle(title);
    }


    public void updateDisplay()
    {
        Display.update();
    }


    public void clearDisplay()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
    }


    public Image createImage(String name, String otherType)
    {
        return LWJGLImage.create(name, otherType);
    }


    public void drawImage(Point position, float width, float height,
        Image image, Rectangle imageRect)
    {
        if(image == null)
        {
            return;
        }

        image.enable();

        float realPosx = position.getX();
        float realPosy = position.getY();

        glPushMatrix();

        glTranslatef(realPosx, realPosy, 0f);

        glBegin(GL_TRIANGLES);

        float u0 = (float)imageRect.left() / image.getWidth();
        float v0 = (float)imageRect.top() / image.getHeight();
        float u1 = (float)(imageRect.left() + imageRect.getWidth())
            / image.getWidth();
        float v1 = (float)(imageRect.top() + imageRect.getHeight())
            / image.getHeight();

        // Front
        glTexCoord2f(u1, v0);
        glVertex3f(width, 0, 1);
        glTexCoord2f(u1, v1);
        glVertex3f(width, height, 1);
        glTexCoord2f(u0, v1);
        glVertex3f(0, height, 1);

        glTexCoord2f(u1, v0);
        glVertex3f(width, 0, 1);
        glTexCoord2f(u0, v1);
        glVertex3f(0, height, 1);
        glTexCoord2f(u0, v0);
        glVertex3f(0, 0, 1);

        glEnd();

        glPopMatrix();
    }
}
