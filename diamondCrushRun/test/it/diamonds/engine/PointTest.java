package it.diamonds.engine;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Zilly
 */
public class PointTest {
    
    /**
     * Testa se o construtor seta os valores corretos.
     */
    @Test
    public void testPointConstructor() {
        System.out.println("* PointTest: - testPointConstructor()");
        
        Point point = new Point(10, 20);

        assertEquals(10, point.getX(), 0.0001f);
        assertEquals(20, point.getY(), 0.0001f);
    }

    /**
     * Testa se os setters da classe Point estão setando o valor correto.
     */
    @Test
    public void testPointSetters() {
        System.out.println("* PointTest: - testPointSetters()");
        
        Point point = new Point(50, 50);

        assertEquals(50, point.getX(), 0.0001f);
        assertEquals(50, point.getY(), 0.0001f);

        point.setX(20);
        point.setY(15);

        assertEquals(20, point.getX(), 0.0001f);
        assertEquals(15, point.getY(), 0.0001f);
    }

    /**
     * Testa se dois pontos possuem os mesmos valores de x e y.
     */
    @Test
    public void testPointEquals() {
        System.out.println("* PointTest: - testPointEquals()");
        
        Point point1 = new Point(10, 25);
        Point point2 = new Point(10, 5);
        Point point3 = new Point(10, 25);

        assertNotSame(point1, point3);
        
        assertTrue("point1 deve ter os mesmos valores que point3", point1.equals(point3));
        assertFalse("point1 não deve ter os mesmo valores que point2", point1.equals(point2));
    }
    
}
