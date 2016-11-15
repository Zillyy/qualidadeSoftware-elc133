package it.diamonds.tests.grid.state;


import it.diamonds.grid.state.AbstractControllerState;
import it.diamonds.grid.state.GemFallStrategy;
import it.diamonds.grid.state.NormalGemFallState;
import it.diamonds.tests.grid.AbstractGridTestCase;


public class TestGemFallStateStrategy extends AbstractGridTestCase
{
    public void testReturnCrushState()
    {
        GemFallStrategy returnWaitNextCrushState = new NormalGemFallState();
        AbstractControllerState state = returnWaitNextCrushState.returnState(
            environment, 10);
        int delayBeforeNextCrush = environment.getConfig().getInteger(
            "DelayBetweenCrushes");

        state = state.update(10 + delayBeforeNextCrush - 1, controller);
        assertTrue(state.isCurrentState("WaitNextCrush"));

        state = state.update(10 + delayBeforeNextCrush, controller);
        assertFalse(state.isCurrentState("WaitNextCrush"));
    }

}
