package it.diamonds.state;

import static it.diamonds.droppable.DroppableColor.DIAMOND;
import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.CrushState;
import it.diamonds.tests.grid.AbstractGridTestCase;
import it.diamonds.grid.state.CrushState;
import java.io.IOException;
import static junit.framework.TestCase.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ghaleon
 */
public class StateTest extends AbstractGridTestCase{
    private AbstractControllerState state;
    
    
    
    @Before
    public void setUp() throws IOException
    {
        super.setUp();
        state = new CrushState(environment);
        setDiamondsGemsPair(grid, controller.getGemsPair());
        
    }
     
    /**
     * Testa se o nome do estado atual está correto.
     */
    @Test
    public void testStateRightName(){
        assertTrue(state.isCurrentState("Crush"));
    }
    
    /**
     * Testa se a gema que já caiu pode ser movimentada.
     */
    @Test
    public void testIfDroppedGemCanMove(){
        insertAndUpdate(createGem(DIAMOND), 5, 1);
        state = state.update(environment.getTimer().getTime(), controller);
        assertTrue("not correct state returned", state.isCurrentState("GemFall"));
    }
}
