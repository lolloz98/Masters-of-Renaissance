package it.polimi.ingsw.client.localmodel.localcards;

public class LocalLeaderCard extends LocalCard{
    protected final int victoryPoints;
    private boolean isActive;
    private boolean isDiscarded;


    public synchronized int getVictoryPoints() {
        return victoryPoints;
    }

    public LocalLeaderCard(int id, int victoryPoints, boolean isActive, boolean isDiscarded) {
        super(id);
        this.victoryPoints = victoryPoints;
        this.isActive = isActive;
        this.isDiscarded = isDiscarded;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDiscarded() {
        return isDiscarded;
    }

    public void setDiscarded(boolean discarded) {
        isDiscarded = discarded;
    }
}
