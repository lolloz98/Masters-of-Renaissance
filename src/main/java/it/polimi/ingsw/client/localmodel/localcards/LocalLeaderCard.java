package it.polimi.ingsw.client.localmodel.localcards;

public class LocalLeaderCard extends LocalCard{
    protected int victoryPoints;

    public synchronized int getVictoryPoints() {
        return victoryPoints;
    }

    public LocalLeaderCard(int id, int victoryPoints) {
        super(id);
        this.victoryPoints = victoryPoints;
    }

    public synchronized void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
