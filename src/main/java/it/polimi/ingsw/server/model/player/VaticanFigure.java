package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.exception.FigureAlreadyActivatedException;
import it.polimi.ingsw.server.model.exception.FigureAlreadyDiscardedException;

import java.io.Serializable;

/**
 * class to model the Pope's Favor tiles on the faith track.
 */
public class VaticanFigure implements Serializable {
    private static final long serialVersionUID = 1033L;

    private FigureState state;

    private final int level;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VaticanFigure) {
            VaticanFigure tmp = (VaticanFigure) obj;
            return state.equals(tmp.state) && level == tmp.level;
        }
        return false;
    }

    public VaticanFigure(int level) {
        this.state = FigureState.INACTIVE;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public FigureState getState() {
        return state;
    }

    public boolean isActive() {
        return state.equals(FigureState.ACTIVE);
    }

    public boolean isInactive() {
        return state.equals(FigureState.INACTIVE);
    }

    /**
     * method that changes the state of the figure when a player reaches a checkpoint on the faith track.
     *
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void activate() throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        if (state == FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        if (state == FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        this.state = FigureState.ACTIVE;
    }

    /**
     * method that changes the state of the figure when a player reaches a checkpoint on the faith track.
     *
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void discard() throws FigureAlreadyActivatedException, FigureAlreadyDiscardedException {
        if (state == FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        if (state == FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        this.state = FigureState.DISCARDED;
    }

    /**
     * Manually sets the state of a figure, only if it's inactive.
     *
     * @param newState the state to set the figure to
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void setState(FigureState newState) throws FigureAlreadyDiscardedException, FigureAlreadyActivatedException {
        if (this.state == FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        if (this.state == FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        this.state = newState;
    }
}
