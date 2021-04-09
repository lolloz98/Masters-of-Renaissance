package it.polimi.ingsw.model.player;

/**
 * class to model the Pope's Favor tiles on the Faith path
 */
public class VaticanFigure {
    private Figurestate state;
    private final int level;


    public VaticanFigure(int level) {
        this.state = Figurestate.INACTIVE;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public Figurestate getState() {
        return state;
    }

    public boolean isActive() {
        return state.equals(Figurestate.ACTIVE);
    }

    public boolean isInactive() {
        return state.equals(Figurestate.INACTIVE);
    }

    /**
     * method that changes the state of the figure when a player reaches a checkpoint on the faithpath
     */
    public void activate() {
        this.state = Figurestate.ACTIVE;
    }

    /**
     * method that changes the state of the figure when a player reaches a checkpoint on the faithpath.
     */
    public void discard() {
        this.state = Figurestate.DISCARDED;
    }
}
