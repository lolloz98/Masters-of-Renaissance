package it.polimi.ingsw.client.localmodel;

public class LocalTrack extends Observable{

    // todo add figures
    private int position;

    public synchronized int getFaithTrackScore() {
        return position;
    }

    public synchronized void setFaithTrackScore(int faithTrackScore) {
        this.position = faithTrackScore;
        notifyObserver();
    }
}
