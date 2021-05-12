package it.polimi.ingsw.client.localmodel.localcards;

public class LocalLeaderCard extends LocalCard{
    protected final int victoryPoints;

    public synchronized int getVictoryPoints() {
        return victoryPoints;
    }

    public LocalLeaderCard(int id, int victoryPoints) {
        super(id);
        this.victoryPoints = victoryPoints;
    }
}
