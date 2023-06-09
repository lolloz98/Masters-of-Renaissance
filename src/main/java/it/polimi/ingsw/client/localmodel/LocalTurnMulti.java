package it.polimi.ingsw.client.localmodel;

import java.io.Serializable;
import java.util.ArrayList;

public class LocalTurnMulti extends LocalTurn implements Serializable  {
    private static final long serialVersionUID = 21L;

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
