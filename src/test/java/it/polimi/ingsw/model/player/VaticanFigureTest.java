package it.polimi.ingsw.model.player;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class VaticanFigureTest {

    @Test
    public void testActivation() {
        VaticanFigure vf = new VaticanFigure(2);
        vf.activate();
        assertEquals(Figurestate.ACTIVE, vf.getState());
    }

    @Test
    public void testDiscarding() {
        VaticanFigure vf = new VaticanFigure(4);
        vf.discard();
        assertEquals(Figurestate.DISCARDED, vf.getState());
    }

}