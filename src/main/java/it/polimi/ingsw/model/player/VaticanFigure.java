package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exception.FigureAlreadyActivatedException;
import it.polimi.ingsw.model.exception.FigureAlreadyDiscardedException;

/**
 * class to model the Pope's Favor tiles on the faith track
 */
public class VaticanFigure {
    private FigureState state;
    private final int level;

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
     * method that changes the state of the figure when a player reaches a checkpoint on the faithpath
     *
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void activate() {
        if(state== FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        if(state== FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        this.state = FigureState.ACTIVE;
    }

    /**
     * method that changes the state of the figure when a player reaches a checkpoint on the faithpath.
     *
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void discard() {
        if(state== FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        if(state== FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        this.state = FigureState.DISCARDED;
    }

    /**
     * Manually sets the state of a figure, only if it's inactive
     *
     * @param newState the state to set the figure to
     * @throws FigureAlreadyDiscardedException if the figure is discarded
     * @throws FigureAlreadyActivatedException if the figure is activated
     */
    public void setState(FigureState newState) {
        if(this.state== FigureState.ACTIVE) throw new FigureAlreadyActivatedException();
        if(this.state== FigureState.DISCARDED) throw new FigureAlreadyDiscardedException();
        this.state = newState;
    }
}
