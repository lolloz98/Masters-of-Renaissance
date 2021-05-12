package it.polimi.ingsw.client.localmodel.localcards;

public class LocalLeaderCard extends LocalCard{
    protected int victoryPoints;
    protected int id;

    public synchronized int getVictoryPoints() {
        return victoryPoints;
    }

    public LocalLeaderCard(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public synchronized void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
