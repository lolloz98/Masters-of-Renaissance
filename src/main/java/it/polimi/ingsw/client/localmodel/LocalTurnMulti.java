package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;

public class LocalTurnMulti extends LocalTurn implements Serializable  {
    private LocalPlayer currentPlayer;

    public LocalTurnMulti(){
    }

    public LocalTurnMulti(boolean mainActionOccurred, boolean productionsActivated, boolean marketActivated, LocalPlayer currentPlayer) {
        super(mainActionOccurred, productionsActivated, marketActivated);
        this.currentPlayer = currentPlayer;
    }

    public synchronized LocalPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(LocalPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


}
