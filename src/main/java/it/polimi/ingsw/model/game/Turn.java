package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.exception.MainActionAlreadyOccurredException;

public abstract class Turn {
    private boolean mainActionOccurred;

    public boolean isMainActionOccurred() {
        return mainActionOccurred;
    }

    public void setMainActionOccurred(boolean mainActionOccurred) {
        if (this.mainActionOccurred) throw new MainActionAlreadyOccurredException();
        this.mainActionOccurred = mainActionOccurred;
    }

    public abstract Turn nextTurn(Game game);

    public Turn(){
        mainActionOccurred = false;
    }
}
