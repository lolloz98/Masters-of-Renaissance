package it.polimi.ingsw.client.localmodel;

public class LocalTurnMulti extends LocalTurn {
    private LocalPlayer currentPlayer;

    public synchronized LocalPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void setCurrentPlayer(LocalPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public LocalTurnMulti(){

    }
}
