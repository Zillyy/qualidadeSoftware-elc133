package it.diamonds.engine;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Zilly
 */
public class RectangleTest {
        
    /**
     * Testa se o construtor seta os valores corretos.
     */
    @Test
    public void testRectangleConstructor() {
        System.out.println("* RectangleTest: - testRectangleConstructor()");
        
        Rectangle rectangle = new Rectangle(20, 30, 40, 50);
        
        assertEquals(20, rectangle.left());
        assertEquals(30, rectangle.top());
        assertEquals(40, rectangle.right());
        assertEquals(50, rectangle.bottom());
    }
    
    /**
     * Testa se a largura retornada está correta.
     */
    @Test
    public void testRectangleWidth() {
        System.out.println("* RectangleTest: - testRectangleWidth()");
        
        Rectangle rectangle = new Rectangle(20, 35, 40, 55);
        
        assertEquals(21, rectangle.getWidth());
    }
    
    /**
     * Testa se a altura retornada está correta.
     */
    @Test
    public void testRectangleHeight() {
        System.out.println("* RectangleTest: - testRectangleHeight()");
        
        Rectangle rectangle = new Rectangle(20, 40, 40, 57);
        
        assertEquals(18, rectangle.getHeight());
    }
    
}
