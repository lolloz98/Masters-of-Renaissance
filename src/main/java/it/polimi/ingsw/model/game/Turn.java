package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;

public abstract class Turn {
    private boolean mainActionOccurred;

    public boolean isMainActionOccurred() {
        return mainActionOccurred;
    }

    /**
     * Sets the mainActionOccurred flag.
     *
     * @throws MainActionAlreadyOccurredException if it's called twice, as the main action can only occur once in every turn.
     */
    public void setMainActionOccurred() {
        if (this.mainActionOccurred) throw new MainActionAlreadyOccurredException();
        this.mainActionOccurred = true;
    }

    public abstract Turn nextTurn(Game<? extends Turn> game);

    public Turn(){
        mainActionOccurred = false;
    }
}
