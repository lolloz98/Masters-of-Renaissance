package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.FigureAlreadyActivatedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyDiscardedException;
import it.polimi.ingsw.server.model.exception.ModelException;
import org.junit.Test;

import static org.junit.Assert.*;

public class VaticanFigureTest {

    @Test
    public void testActivation() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.activate();
        assertEquals(FigureState.ACTIVE, vf.getState());
    }

    @Test
    public void testDiscarding() throws ModelException {
        VaticanFigure vf = new VaticanFigure(4);
        vf.discard();
        assertEquals(FigureState.DISCARDED, vf.getState());
    }

    @Test
    public void testSetState1() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.setState(FigureState.ACTIVE);
        assertEquals(FigureState.ACTIVE, vf.getState());
    }

    @Test
    public void testSetState2() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.setState(FigureState.DISCARDED);
        assertEquals(FigureState.DISCARDED, vf.getState());
    }

    @Test (expected = FigureAlreadyActivatedException.class)
    public void testActivationAlreadyActivated() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.activate();
        vf.activate();
    }

    @Test (expected = FigureAlreadyDiscardedException.class)
    public void testDiscardAlreadyDiscarded() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.discard();
        vf.discard();
    }

    @Test (expected = FigureAlreadyActivatedException.class)
    public void testDiscardAlreadyActivated() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.activate();
        vf.discard();
    }

    @Test (expected = FigureAlreadyDiscardedException.class)
    public void testActivationAlreadyDiscarded() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.discard();
        vf.activate();
    }

    @Test (expected = FigureAlreadyDiscardedException.class)
    public void testSetStateAlreadyDiscarded() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.discard();
        vf.setState(FigureState.ACTIVE);
    }

    @Test (expected = FigureAlreadyActivatedException.class)
    public void testSetStateAlreadyActivated() throws ModelException {
        VaticanFigure vf = new VaticanFigure(2);
        vf.activate();
        vf.setState(FigureState.ACTIVE);
    }
}